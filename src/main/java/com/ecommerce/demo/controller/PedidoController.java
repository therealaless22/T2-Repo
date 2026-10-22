package com.ecommerce.demo.controller;

import com.ecommerce.demo.DTO.PedidoRequestDTO;
<<<<<<< HEAD
import com.ecommerce.demo.entity.Pedido;
import com.ecommerce.demo.service.ElectroStoreService;
=======
import com.ecommerce.demo.DTO.PedidoResponseDTO;
import com.ecommerce.demo.service.PedidoService;
>>>>>>> 012bcf6 (Update T2)
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
<<<<<<< HEAD
    private ElectroStoreService service;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        Pedido nuevoPedido = service.crearPedido(request);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(service.listarPedidos());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPedidoPorId(id));
        
    }
}
=======
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtener(id));
    }

    // JPQL JOIN FETCH
    @GetMapping("/cliente/{cliente}")
    public ResponseEntity<List<PedidoResponseDTO>> porCliente(@PathVariable String cliente) {
        return ResponseEntity.ok(pedidoService.porCliente(cliente));
    }
}
>>>>>>> 012bcf6 (Update T2)
