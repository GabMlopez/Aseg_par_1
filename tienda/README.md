# 💬 Sistema de Venta y gestión de Inventario

Sistema de ventas e inventario de prendas, camisas y pantalones, con autenticación basada en JWT. Pruebas funcionales, unitarias e integración ademas de pruebas no fucionales de seguridad, carga y mantenibilidad. 

---

## 🏗️ Arquitectura

### Vista General (Contenedores)

Diagrama de clases del sistema de inventario

### Diagrama de interacción del sistema

```mermaid
graph TD
    %% Clients
    User[Web Client ]

    %% Backend
    subgraph Backend [Backend - Java]
        API[Java API - Springboot Framework]
    end

    %% Storage & Databases
    subgraph Databases [Data & Storage]
        Postgres[(PostgresSQL - Aiven)]
    end

    %% Connections
    User -- HTTP / REST --> API

    API -- CRUD Operations --> Postgres
  
```
### Estructura de carpetas API
Arquitectura Hexagonal de 4 capas
```
├── tienda/
├──── application/              # Aplicaciones sobre el core
│      └── Services/            # Servicios en la API
├──── domain/                   # Core de Negocio
|       ├── Model/              # Clases unicas del sistema
│       └── Ports/              # Funciones aplicadas a los modelos 
├──── infraestructure/          # Interacciones con servicios externos
|       ├── Persitency/         # Servicios con bases de datos (PostgreSQL)
|       |    ├── Adapter/      # Adapta el core a funciones de BD
|       |    ├── Entity/       # Clases Entendibles para la BD
|       |    ├── Mapper/       # Mappea los datos de la entidad
│       |    └── Repository/   # Interface para el adaptador y las entidades
│       └── Security/          # Configuraciones de seguridad, CORS, JWToken
├──── interfaces/              # Interacción con los usuarios
|       └── rest/              # Servicios Rest y controladores de la API
├──── Sqa/                     # Pruebas para medir calidad del Sw
```

### Diagrama de Clases
```mermaid
classDiagram
    class Usuario {
        <<entity>>
        +Long id
        +String username
        +String password
        +String nombre
        +LocalDateTime lastActive
        +actualizarLastActive()
    }
    
    class LogAcceso {
        <<audit>>
        +Long id
        +Long usuarioId
        +String actividad
        +LocalDateTime fecha
    }
    
    class Prenda {
        <<interface>>
        +Long getId()
        +String getMarca()
        +String getTamanio()
        +Double getPrecio()
        +Integer getCantidad()
        +String getTipo()
    }
    
    class Camisa {
        <<entity>>
        +Long id
        +String marca
        +String tamanio
        +Double precio
        +Integer cantidad
        +TipoEstampado tipoEstampado
        +getTipo()
    }
    
    class Pantalon {
        <<entity>>
        +Long id
        +String marca
        +String tamanio
        +Double precio
        +Integer cantidad
        +getTipo()
    }
    
    class LogVenta {
        <<audit>>
        +Long id
        +Long prendaId
        +Double precioVenta
        +LocalDateTime fechaGeneracion
    }
    
    class TipoEstampado {
        <<enumeration>>
        PLASTICO
        BORDADO
    }

    LogAcceso --> Usuario : "👤 usuarioId"
    Prenda <|-- Camisa : «implements»
    Prenda <|-- Pantalon : «implements»
    LogVenta --> Prenda : "🛒 prendaId"
    Camisa --> TipoEstampado : "usa"

    style Usuario fill:#bbdefb,stroke:#1976d2,stroke-width:3px,color:#000
    style LogAcceso fill:#ffccbc,stroke:#f57c00,stroke-width:3px,color:#000
    style LogVenta fill:#ffccbc,stroke:#f57c00,stroke-width:3px,color:#000
    style Prenda fill:#e1bee7,stroke:#8e24aa,stroke-width:3px,stroke-dasharray: 8 4,color:#000
    style Camisa fill:#c8e6c9,stroke:#388e3c,stroke-width:3px,color:#000
    style Pantalon fill:#c8e6c9,stroke:#388e3c,stroke-width:3px,color:#000
    style TipoEstampado fill:#fff9c4,stroke:#fbc02d,stroke-width:3px,color:#000
```