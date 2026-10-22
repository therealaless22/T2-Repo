#!/bin/bash
# Uso: bash pruebas.sh   (con la app corriendo en :8080)
B=http://localhost:8080/api
J="Content-Type: application/json"

echo "== 1. Login"
TOKEN=$(curl -s -X POST $B/auth/login -H "$J" \
  -d '{"username":"admin","password":"UPN2100"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
A="Authorization: Bearer $TOKEN"

echo "== 2. Sin token (401)"
curl -s -o /dev/null -w "%{http_code}\n" $B/productos

echo "== 3. Crear productos (201)"
curl -s -X POST $B/productos -H "$A" -H "$J" -d '{"nombre":"Laptop Lenovo","categoria":"Laptops","precio":2500.00,"stock":10}'; echo
curl -s -X POST $B/productos -H "$A" -H "$J" -d '{"nombre":"Mouse Logitech","categoria":"Accesorios","precio":80.50,"stock":50}'; echo

echo "== 4. Listar / obtener"
curl -s $B/productos -H "$A"; echo
curl -s $B/productos/1 -H "$A"; echo

echo "== 5. Producto inexistente (404)"
curl -s $B/productos/999 -H "$A"; echo

echo "== 6. Validacion (400)"
curl -s -X POST $B/productos -H "$A" -H "$J" -d '{"nombre":"","categoria":"X","precio":-5,"stock":-1}'; echo

echo "== 7. Named Query por categoria / JPQL precio maximo"
curl -s $B/productos/categoria/laptops -H "$A"; echo
curl -s "$B/productos/precio-max?valor=100" -H "$A"; echo

echo "== 8. Actualizar (200)"
curl -s -X PUT $B/productos/2 -H "$A" -H "$J" -d '{"nombre":"Mouse Logitech G203","categoria":"Accesorios","precio":90.00,"stock":45}'; echo

echo "== 9. Crear pedido (201)"
curl -s -X POST $B/pedidos -H "$A" -H "$J" \
  -d '{"cliente":"Juan Perez","items":[{"productoId":1,"cantidad":2},{"productoId":2,"cantidad":3}]}'; echo

echo "== 10. Stock insuficiente (400)"
curl -s -X POST $B/pedidos -H "$A" -H "$J" \
  -d '{"cliente":"Ana","items":[{"productoId":1,"cantidad":500}]}'; echo

echo "== 11. Pedido con producto inexistente (404)"
curl -s -X POST $B/pedidos -H "$A" -H "$J" \
  -d '{"cliente":"Ana","items":[{"productoId":999,"cantidad":1}]}'; echo

echo "== 12. Listar pedidos / por id / por cliente"
curl -s $B/pedidos -H "$A"; echo
curl -s $B/pedidos/1 -H "$A"; echo
curl -s "$B/pedidos/cliente/Juan%20Perez" -H "$A"; echo

echo "== 13. Eliminar producto sin pedidos (204)"
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE $B/productos/2 -H "$A"
