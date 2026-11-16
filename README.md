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


La API queda disponible en `http://localhost:8080/api`.

La base de datos H2 utiliza persistencia en disco y se almacena en la carpeta `data/`, conservando los datos al reiniciar la aplicación. Esta carpeta está excluida del repositorio mediante `.gitignore`.

La consola web de H2 está disponible en `http://localhost:8080/h2-console`.

**Datos de conexión:**
- JDBC URL: `jdbc:h2:file:./data/taller`
- Usuario: `sa`
- Contraseña: vacía.


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

### Endpoints adicionales

| Método | Ruta | Descripción | Respuestas |
| --- | --- | --- | --- |
| GET | /api/repuestos | Lista los repuestos disponibles | 200 |
| GET | /api/repuestos/{id} | Busca un repuesto por ID | 200, 404 |
| POST | /api/repuestos | Crea un repuesto con nombre, precio y stock | 201, 400 |
| POST | /api/ordenes/{id}/repuestos | Registra repuestos utilizados y descuenta el stock | 201, 400, 404, 409 |
| GET | /api/vehiculos/patente/{patente}/historial | Devuelve el historial de reparaciones del vehículo, de la más reciente a la más antigua | 200, 404 |
| GET | /api/ordenes/facturacion?mes=2026-11 | Devuelve la cantidad de órdenes entregadas y el total facturado durante el mes | 200 |

## Reglas de negocio

1. No se permiten vehículos con patentes repetidas. Si se intenta registrar una patente existente, se devuelve HTTP 409 Conflict.
2. Un vehículo no puede tener dos órdenes abiertas simultáneamente. Si ya existe una orden abierta, se devuelve HTTP 409 Conflict.
3. Las órdenes nuevas comienzan en estado `INGRESADO` y deben respetar las transiciones permitidas. Solo pueden cancelarse desde `INGRESADO`. Las transiciones inválidas devuelven HTTP 409.
4. Una orden no puede pasar a `ENTREGADO` sin tener un costo final registrado. Si se intenta, se devuelve HTTP 409.


## Funcionalidades adicionales

**1. Gestión de repuestos e inventario**

Permite registrar repuestos con nombre, precio y stock disponible. Al asociar un repuesto a una orden de reparación, el sistema descuenta automáticamente la cantidad utilizada. Si el stock es insuficiente, devuelve HTTP 409 Conflict.

**2. Cálculo del costo de repuestos**

Las órdenes incluyen el campo `costoRepuestos`, calculado como la suma de los subtotales de los repuestos utilizados. El sistema impide registrar un `costoFinal` inferior a ese importe y devuelve HTTP 400 Bad Request.

**3. Historial de reparaciones por patente**

Permite consultar todas las órdenes de reparación asociadas a un vehículo mediante su patente, ordenadas desde la más reciente hasta la más antigua. El historial incluye estados, fechas, costos y tiempo de reparación.

**4. Facturación mensual**

Permite consultar la facturación de un mes específico mediante el parámetro `mes`, con formato `AAAA-MM`.

La respuesta incluye:
- `cantidadOrdenes`: número de órdenes entregadas durante el mes.
- `totalFacturado`: suma de los costos finales de esas órdenes.

**5. Registro de fechas y tiempo de reparación**

Cada orden registra automáticamente las fechas correspondientes a los cambios de estado:

- `fechaIngreso`
- `fechaInicioReparacion`
- `fechaTerminacion`
- `fechaEntrega`
- `fechaCancelacion`

Una vez finalizada la reparación, el sistema calcula y devuelve `horasEnReparacion`, expresado en horas.


## Pruebas

En `pruebas.http` están las peticiones de ejemplo para probar la API REST (se pueden ejecutar desde VS Code con la extensión REST Client).


