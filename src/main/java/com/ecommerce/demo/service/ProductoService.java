package com.ecommerce.demo.service;

import com.ecommerce.demo.DTO.ProductoDTO;
import com.ecommerce.demo.entity.Producto;
import com.ecommerce.demo.exception.BadRequestException;
import com.ecommerce.demo.exception.ResourceNotFoundException;
import com.ecommerce.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProductoDTO> listar() {
        return productoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtener(Long id) {
        return toDTO(buscar(id));
    }

    @Transactional
    public ProductoDTO crear(ProductoDTO dto) {
        Producto p = new Producto(dto.getNombre().trim(), dto.getCategoria().trim(), dto.getPrecio(), dto.getStock());
        return toDTO(productoRepository.save(p));
    }

    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto p = buscar(id);
        p.setNombre(dto.getNombre().trim());
        p.setCategoria(dto.getCategoria().trim());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        return toDTO(productoRepository.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto p = buscar(id);
        productoRepository.delete(p);
    }

    // Named Query: Producto.findByCategoriaNamed
    @Transactional(readOnly = true)
    public List<ProductoDTO> porCategoria(String categoria) {
        return productoRepository.findByCategoriaNamed(categoria).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    // JPQL con parametro nombrado
    @Transactional(readOnly = true)
    public List<ProductoDTO> porPrecioMaximo(BigDecimal precioMax) {
        if (precioMax == null || precioMax.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El precio maximo debe ser mayor a cero");
        }
        return productoRepository.buscarPorPrecioMaximoJPQL(precioMax).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
    }

    private ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setCategoria(p.getCategoria());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        return dto;
    }
}
