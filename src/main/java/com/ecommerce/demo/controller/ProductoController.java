package com.ecommerce.demo.controller;

<<<<<<< HEAD
import com.ecommerce.demo.entity.Producto;
import com.ecommerce.demo.service.ElectroStoreService;
=======
import com.ecommerce.demo.DTO.ProductoDTO;
import com.ecommerce.demo.service.ProductoService;
>>>>>>> 012bcf6 (Update T2)
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
<<<<<<< HEAD
    private ElectroStoreService service;

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return new ResponseEntity<>(service.guardarProducto(producto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Producto existente = service.obtenerProductoPorId(id);
        existente.setNombre(producto.getNombre());
        existente.setCategoria(producto.getCategoria());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        return ResponseEntity.ok(service.guardarProducto(existente));
=======
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
>>>>>>> 012bcf6 (Update T2)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
<<<<<<< HEAD
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoriaNamed(@PathVariable String categoria) {
        return ResponseEntity.ok(service.buscarProductosPorCategoriaNamed(categoria));
    }

    @GetMapping("/precio-max")
    public ResponseEntity<List<Producto>> listarPorPrecioMaximo(@RequestParam BigDecimal max) {
        return ResponseEntity.ok(service.buscarPorPrecioMaximo(max));
    }

    @GetMapping("/buscar-avanzado")
    public ResponseEntity<List<Producto>> buscarAvanzado(
            @RequestParam String keyword,
            @RequestParam Integer stockMin) {
        return ResponseEntity.ok(service.buscarPorFiltroAvanzadoEM(keyword, stockMin));
    }
}
=======
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Named Query
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.porCategoria(categoria));
    }

    // JPQL: /api/productos/precio-max?valor=500
    @GetMapping("/precio-max")
    public ResponseEntity<List<ProductoDTO>> porPrecioMaximo(@RequestParam("valor") BigDecimal valor) {
        return ResponseEntity.ok(productoService.porPrecioMaximo(valor));
    }
}
>>>>>>> 012bcf6 (Update T2)
