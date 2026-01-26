# 🐾 AniMall - Todo para tu mascota

**AniMall** es una aplicación nativa de Android desarrollada con **Kotlin** y **Jetpack Compose**. Es una plataforma de comercio electrónico diseñada para la venta de productos para mascotas, con roles diferenciados para clientes y administradores.

## 📱 Capturas de Pantalla

| Inicio Cliente | Categorías |
|:---:|:---:|
| <img src="ruta/a/tu/imagen_home.png" width="300" /> | <img src="ruta/a/tu/imagen_categorias.png" width="300" /> |

*(Nota: La interfaz sigue los lineamientos de Material Design).*

## 🚀 Características Principales

### 👤 Módulo Cliente
* **Catálogo de Productos:** Visualización de artículos con precios e imágenes.
* **Filtrado por Categorías:** Clasificación en *Alimentos*, *Juguetes*, *Accesorios* y más.
* **Carrito de Compras:** Funcionalidad para agregar productos al pedido.
* **Perfil de Usuario:** Gestión de cuenta personal.

### 🛡️ Módulo Administrador
* **Panel de Control:** Acceso exclusivo para usuarios con rol `esAdmin`.
* **Gestión:** Pantalla dedicada (`PantallaHomeAdmin`) para la administración del negocio.

### 🔐 Autenticación y Seguridad
* **Login/Registro:** Flujo de entrada condicional.
* **Roles de Usuario:** Lógica de navegación inteligente que detecta si el usuario es `Admin` o `Cliente` al iniciar la app.
* **Persistencia de Sesión:** El estado del usuario se observa en tiempo real (`collectAsState`).

## 🛠️ Stack Tecnológico

El proyecto sigue una arquitectura **Clean Architecture + MVVM** (Model-View-ViewModel).

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (UI declarativa sin XML).
* **Arquitectura:** MVVM.
* **Gestión de Estado:** `StateFlow` y `collectAsState`.
* **Navegación:** Control de flujo basado en estados (`MainActivity`).
* **Build System:** Gradle (Kotlin DSL).

## 📂 Estructura del Proyecto

El código está organizado siguiendo buenas prácticas de separación de responsabilidades:

```text
com.example.animall
├── 📂 data             # Capa de Datos
│   ├── 📂 modelo       # Data classes (Usuario, Producto)
│   └── 📂 repositorio  # Lógica de acceso a datos
├── 📂 ui               # Capa de Presentación (Compose)
│   ├── 📂 componentes  # Widgets reutilizables (Cards, Botones)
│   ├── 📂 navegacion   # Grafos de navegación
│   ├── 📂 pantallas    # Pantallas completas (Admin, Auth, Cliente)
│   ├── 📂 theme        # Tema, colores y tipografía
│   └── 📂 vistamodelo  # ViewModels (AppViewModel)
└── 📄 MainActivity.kt  # Punto de entrada y orquestador de navegación

Requisitos y Configuración:
Android Studio: Koala / Ladybug o superior.

minSdk: 24 (Android 7.0).

targetSdk: 36.

Lenguaje: Java 11 / Kotlin 1.9+.

Instalación:
Clonar el repositorio.

Abrir en Android Studio.

Esperar la sincronización de Gradle.

Ejecutar en un emulador (Recomendado: API 34+ para compatibilidad con Google Play Services).

Desarrollado con ❤️ para la clase de Desarrollo Móvil.
