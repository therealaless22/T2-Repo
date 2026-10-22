package com.ecommerce.demo.service;

import com.ecommerce.demo.DTO.ItemPedidoRequestDTO;
import com.ecommerce.demo.DTO.PedidoRequestDTO;
import com.ecommerce.demo.DTO.PedidoResponseDTO;
import com.ecommerce.demo.DTO.PedidoResponseDTO.DetalleResponseDTO;
import com.ecommerce.demo.entity.DetallePedido;
import com.ecommerce.demo.entity.Pedido;
import com.ecommerce.demo.entity.Producto;
import com.ecommerce.demo.exception.BadRequestException;
import com.ecommerce.demo.exception.ResourceNotFoundException;
import com.ecommerce.demo.repository.PedidoRepository;
import com.ecommerce.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO request) {
        Pedido pedido = new Pedido();
        pedido.setCliente(request.getCliente().trim());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto con id " + item.getProductoId() + " no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock() + ", solicitado: " + item.getCantidad());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            pedido.addDetalle(detalle);

            total = total.add(subtotal);
        }

        pedido.setMontoTotal(total);
        return toDTO(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return pedidoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtener(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido con id " + id + " no encontrado"));
        return toDTO(pedido);
    }

    // JPQL con JOIN FETCH
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> porCliente(String cliente) {
        return pedidoRepository.buscarPorClienteConDetallesJPQL(cliente).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private PedidoResponseDTO toDTO(Pedido p) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(p.getId());
        dto.setCliente(p.getCliente());
        dto.setFechaCompra(p.getFechaCompra());
        dto.setMontoTotal(p.getMontoTotal());
        dto.setDetalles(p.getDetalles().stream().map(d -> {
            DetalleResponseDTO det = new DetalleResponseDTO();
            det.setProductoId(d.getProducto().getId());
            det.setProducto(d.getProducto().getNombre());
            det.setCantidad(d.getCantidad());
            det.setPrecioUnitario(d.getPrecioUnitario());
            det.setSubtotal(d.getSubtotal());
            return det;
        }).collect(Collectors.toList()));
        return dto;
    }
}
