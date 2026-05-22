# 📊 Traceability Service

Microservicio dedicado a la trazabilidad y auditoría de los pedidos, desarrollado en Java 17 y Spring Boot 3.

Este componente del ecosistema administra el historial de estados de las órdenes utilizando una base de datos NoSQL (**MongoDB**) para optimizar la escritura rápida de logs y la consulta de documentos. Permite registrar cambios, consultar la línea de tiempo por cliente y analizar métricas de eficiencia (tiempos promedios de gestión) por empleado.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características Principales](#características-principales)
- [Modelo de Datos (MongoDB)](#modelo-de-datos-mongodb)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Configuración Inicial](#configuración-inicial)
- [Dependencias Principales](#dependencias-principales)
- [Ejecución y Pruebas](#ejecución-y-pruebas)
- [Autor](#autor)

---

## Descripción

El microservicio de Trazabilidad actúa como una bitácora inmutable (log) del sistema. Cada vez que un pedido cambia de estado en la Plazoleta, este servicio recibe y almacena la información estructurada. Además, procesa estos datos para calcular la duración exacta de cada pedido y generar rankings de eficiencia del personal (chefs), mejorando la toma de decisiones del propietario del restaurante.

## Características Principales

- **Registro Histórico:** Almacenamiento del ciclo de vida de un pedido dentro de un único documento NoSQL.
- **Cálculo de Tiempos:** Registro automático del tiempo de inicio (`startTime`), fin (`endTime`) y duración total en minutos de cada pedido.
- **Métricas de Eficiencia:** Generación de un ranking de empleados basado en su tiempo promedio de preparación.
- **Consultas por Actor:** Filtros de trazabilidad específicos para clientes (sus propios pedidos) y empleados.
- **Seguridad JWT:** Protección de endpoints mediante la validación de tokens provistos por el microservicio de Usuarios.

## Modelo de Datos (MongoDB)

Al utilizar MongoDB, la entidad principal (`OrderTraceability`) se mapea como un `@Document` en lugar de una tabla relacional. Su estructura optimizada incluye:

- `id`: Identificador autogenerado (ObjectId hexadecimal).
- `orderId`, `clientId`, `employeeId`: Referencias cruzadas a otros microservicios.
- `currentState`: Estado actual de la orden.
- `startTime` / `endTime`: Marcas de tiempo de los hitos principales.
- `totalDurationInMinutes`: Cálculo en caché de la duración total para consultas eficientes.
- `history`: Arreglo (Sub-documentos) con el historial detallado de estados y sus respectivas fechas.

## Arquitectura del Proyecto

El proyecto sigue los principios de la **Arquitectura Hexagonal (Clean Architecture)**:

- **Domain:** Modelos de dominio puros e interfaces (Puertos).
- **Application:** Casos de uso (UseCases) y orquestación de la lógica de trazabilidad y cálculos de fechas.
- **Infrastructure:** Adaptadores de entrada (Controladores REST), adaptadores de salida (`MongoRepository`) y configuraciones de seguridad.

## Configuración Inicial

1. Tener instalado **JDK 17**.
2. Clonar el repositorio.
3. Asegurarse de tener una instancia de **MongoDB** en ejecución en el puerto `27017` (local o en contenedor Docker).

### Variables de Entorno

Configurar la siguiente variable de entorno en el sistema o IDE antes de ejecutar:
- `PRAGMA_JWT_KEY`: Clave secreta para la validación de firmas JWT (debe ser idéntica a la de los otros microservicios).

### Configuración del Servidor (`application.yml`)

El servicio se despliega en el puerto `8083`. La configuración base en `src/main/resources/application.yml` es la siguiente:

```yaml
server:
  port: 8083

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/bd_trazabilidad
      
  security:
    jwt:
      secret: ${PRAGMA_JWT_KEY}