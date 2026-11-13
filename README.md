# TP Integrador · DAW1 — Sistema de Gestión de Taller Mecánico

**Alumno/a:** Lucas Aspee · **Legajo:** 510001
**Enunciado elegido:** taller

## Descripción

Sistema de gestión de un taller mecánico que permite administrar clientes, vehículos y órdenes de reparación mediante una API REST. El sistema registra los trabajos realizados, gestiona los estados de las reparaciones y controla reglas de negocio como las patentes únicas y la prohibición de órdenes abiertas simultáneas para un mismo vehículo.

## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

```bash
./mvnw spring-boot:run
```

En Windows, ejecutar `.\mvnw.cmd spring-boot:run`.

La API queda en `http://localhost:8080/api`. La base H2 se guarda en `data/` (no se sube al repositorio). Consola H2: `http://localhost:8080/h2-console`.

## Modelo

| Entidad  | Atributos principales               | Relaciones                         |
| -------- | ----------------------------------- | ---------------------------------- |
| Cliente  | id, nombre, telefono                | 1 a N con Vehiculo                 |
| Vehiculo | id, patente, marca, modelo          | N a 1 con Cliente; 1 a N con Orden |
| Orden    | id, descripcion, estado, costoFinal | N a 1 con Vehiculo                 |

Estados de Orden: `INGRESADO → EN_REPARACION → TERMINADO → ENTREGADO`. Desde `INGRESADO` también se permite `CANCELADO`.

## Endpoints

| Método | Ruta                         | Descripción                               | Respuestas         |
| ------ | ---------------------------- | ----------------------------------------- | ------------------ |
| GET    | /api/clientes                | Lista clientes                            | 200                |
| GET    | /api/clientes/{id}           | Busca cliente por ID                      | 200, 404           |
| POST   | /api/clientes                | Crea cliente                              | 201, 400           |
| PUT    | /api/clientes/{id}           | Modifica cliente                          | 200, 400, 404      |
| DELETE | /api/clientes/{id}           | Borra cliente                             | 204, 404           |
| GET    | /api/clientes/{id}/vehiculos | Lista los vehículos de un cliente         | 200, 404           |
| GET    | /api/vehiculos               | Lista vehículos                           | 200                |
| GET    | /api/vehiculos/{id}          | Busca vehículo por ID                     | 200, 404           |
| POST   | /api/vehiculos               | Crea vehículo                             | 201, 400, 404, 409 |
| PUT    | /api/vehiculos/{id}          | Modifica vehículo                         | 200, 400, 404, 409 |
| DELETE | /api/vehiculos/{id}          | Borra vehículo                            | 204, 404           |
| GET    | /api/ordenes                 | Lista órdenes; permite filtrar por estado | 200                |
| GET    | /api/ordenes/{id}            | Busca orden por ID                        | 200, 404           |
| POST   | /api/ordenes                 | Crea orden                                | 201, 400, 404, 409 |
| PUT    | /api/ordenes/{id}            | Modifica descripción y costo final        | 200, 404           |
| PATCH  | /api/ordenes/{id}/estado     | Cambia el estado de una orden             | 200, 400, 404, 409 |
| DELETE | /api/ordenes/{id}            | Borra orden                               | 204, 404           |

## Reglas de negocio

1. No se permiten vehículos con patentes repetidas. Si se intenta registrar una patente existente, se devuelve HTTP 409 Conflict.
2. Un vehículo no puede tener dos órdenes abiertas simultáneamente. Si ya existe una orden abierta, se devuelve HTTP 409 Conflict.
3. Las órdenes nuevas comienzan en estado `INGRESADO` y deben respetar las transiciones permitidas. Solo pueden cancelarse desde `INGRESADO`. Las transiciones inválidas devuelven HTTP 409.
4. Una orden no puede pasar a `ENTREGADO` sin tener un costo final registrado. Si se intenta, se devuelve HTTP 409.

## Pruebas

En `pruebas.http` están las peticiones de ejemplo para probar la API REST (se pueden ejecutar desde VS Code con la extensión REST Client).

También se incluyen las pruebas automáticas del profesor en `.tp/`, que se ejecutan con la aplicación iniciada mediante `bash .tp/correr.sh`.
