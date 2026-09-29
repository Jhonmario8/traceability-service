# Traceability Service

Microservicio de trazabilidad del proyecto Reto Pragma (plazoleta de comidas). Guarda en MongoDB un registro por cada cambio de estado de un pedido, calcula la duración de los pedidos que terminan y expone el tiempo promedio por empleado. `plazoleta-service` lo llama por OpenFeign para registrar y consultar la trazabilidad.

Repositorio: [Jhonmario8/traceability-service](https://github.com/Jhonmario8/traceability-service). En el repositorio raíz [Reto-Pragma](https://github.com/Jhonmario8/Reto-Pragma) está incluido como submódulo `tracebility`.

## Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Modelo de datos](#modelo-de-datos)
- [Endpoints](#endpoints)
- [Cálculo de tiempos](#cálculo-de-tiempos)
- [Arquitectura](#arquitectura)
- [Configuración](#configuración)
- [Ejecución en local con MongoDB](#ejecución-en-local-con-mongodb)
- [Tests](#tests)
- [Limitaciones conocidas](#limitaciones-conocidas)

## Tecnologías

- Java 17, Spring Boot 4.0.6 (WebMVC, Security, Data MongoDB, Validation)
- MongoDB
- JWT con `io.jsonwebtoken` 0.11.5 (solo valida tokens emitidos por `user-service`)
- MapStruct 1.5.5 y Lombok
- Gradle 9.4.1 (wrapper incluido)
- JUnit 5, Mockito y AssertJ

A diferencia de los demás servicios, este usa Spring Boot 4.

## Modelo de datos

Colección `order_traceability`. Cada documento representa un cambio de estado de un pedido:

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | String (ObjectId) | Generado por MongoDB. |
| `orderId` | Long | Pedido en `plazoleta-service`. |
| `clientId` | Long | Cliente del pedido. |
| `employeeId` | Long | Empleado asignado (puede ser nulo mientras el pedido está en PENDING). |
| `previousState` | String | Estado anterior. |
| `newState` | String | Estado nuevo. |
| `startTime` | LocalDateTime | Inicio del pedido. |
| `endTime` | LocalDateTime | Fin del pedido (entregado o cancelado). |
| `totalDurationInMinutes` | Long | Se calcula al guardar si hay `startTime` y `endTime`. |

## Endpoints

Todos requieren `Authorization: Bearer <token>`.

| Método | Ruta | Rol | Descripción |
|---|---|---|---|
| POST | `/traceability/` | Autenticado | Guarda un registro de trazabilidad. |
| GET | `/traceability/{orderId}` | Autenticado | Devuelve el registro más reciente del pedido; 404 si no existe. |
| GET | `/traceability/client/{clientId}` | CLIENT | Lista los registros de un cliente. |
| GET | `/traceability/employee/{employeeId}` | EMPLOYEE | Lista los registros de un empleado. |
| GET | `/traceability/owner/{employeeId}/media` | OWNER | Tiempo promedio en minutos de los pedidos de un empleado. |

## Cálculo de tiempos

- Al guardar un registro con `endTime`, `totalDurationInMinutes` se calcula como los minutos completos entre `startTime` y `endTime`. Si falta alguna de las dos fechas, queda en `null`.
- El promedio por empleado suma `totalDurationInMinutes` de los registros que la tienen y divide entre el total de registros del empleado. Si el empleado no tiene registros, devuelve `0.0` sin dividir por cero.

## Arquitectura

Arquitectura hexagonal bajo `src/main/java/com/pragma/traceability/`:

```
domain/          Modelo OrderTraceability, puertos api/spi y excepciones
application/     Caso de uso OrderTraceabilityUseCase, handler, DTO y mapper
infrastructura/  Controlador REST, adaptador y repositorio de MongoDB, seguridad JWT y manejo de errores
```

El paquete de infraestructura se llama `infrastructura` y el adaptador de MongoDB está bajo `output/jpa`, aunque no usa JPA. Son nombres heredados que no se han cambiado.

## Configuración

`src/main/resources/application.yml`:

| Propiedad | Valor | Descripción |
|---|---|---|
| `server.port` | `8083` | Puerto HTTP. |
| `spring.mongodb.uri` | `mongodb://localhost:27017/traceability` | Conexión a MongoDB. En Spring Boot 4 la propiedad es `spring.mongodb.*`, no `spring.data.mongodb.*`. |
| `spring.security.jwt.secret` | `${PRAGMA_JWT_KEY}` | Clave para validar los JWT. Debe ser la misma que usa `user-service`. |

Variable de entorno obligatoria: `PRAGMA_JWT_KEY`.

## Ejecución en local con MongoDB

Requisitos: JDK 17 y MongoDB en `localhost:27017`. La base `traceability` y la colección se crean con el primer registro.

Con Docker:

```bash
docker run -d --name mongo-traceability -p 27017:27017 mongo:7
```

Luego:

```bash
export PRAGMA_JWT_KEY=<misma_clave_que_user-service>
./gradlew bootRun
```

El servicio queda en `http://localhost:8083`.

## Tests

```bash
./gradlew test
```

Son tests unitarios con JUnit 5 y Mockito. No levantan Spring ni se conectan a MongoDB, así que `./gradlew build` pasa sin MongoDB ni variables de entorno.

| Clase | Qué cubre |
|---|---|
| `OrderTraceabilityUseCaseTest` | Registro de inicio (sin duración) y de fin (duración en minutos), promedio con varios registros, con uno y sin registros, búsqueda por pedido (encontrado y no encontrado) y listados por cliente y empleado. |
| `OrderTraceabilityTest` | Cálculo de minutos entre dos fechas y casos con fechas nulas. |

## Limitaciones conocidas

- Cada cambio de estado se guarda como un documento nuevo y solo el último (entregado o cancelado) tiene duración. Como el promedio divide entre todos los documentos del empleado, sale menor que el tiempo real. Además, los pedidos cancelados cuentan en el promedio.
- No se valida que `endTime` sea posterior a `startTime`, así que la duración puede ser negativa.
- El repositorio declara `MongoRepository<OrderTraceabilityDocument, Long>`, pero el `@Id` del documento es `String`.
- El controlador no usa `@Valid`, así que las anotaciones `@NotNull` del DTO no se aplican. Si se aplicaran, el registro inicial que envía `plazoleta-service` (sin `employeeId`) sería rechazado.
