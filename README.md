# Stacking-Cups
Este es el primer proyecto de DOPO 2026 - 1 
# 🥤 StackingItems - Torre de Tazas y Tapas

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![BlueJ](https://img.shields.io/badge/BlueJ-0052CC?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-En%20Desarrollo-yellow?style=for-the-badge)

**Simulador interactivo de torres de tazas y tapas**  
*Inspirado en el Problem J - ICPC World Championship 2025*

[📋 Características](#-características) •
[🎯 Requisitos](#-requisitos-funcionales) •
[🎨 Diseño](#-diseño) •
[🚀 Inicio Rápido](#-inicio-rápido)

---

</div>

## 📖 Descripción

**StackingItems** es un simulador visual que permite apilar tazas cilíndricas y sus tapas en una torre vertical. Basado en el desafío de la maratón de programación ICPC 2025, este proyecto implementa una solución orientada a objetos con interfaz gráfica interactiva.

### 🎪 Concepto

- 🥤 **Tazas**: Elementos cilíndricos de diferentes alturas (potencias de 2: 1cm, 3cm, 5cm, 7cm...)
- 🎩 **Tapas**: Cada taza tiene su tapa correspondiente (1 cm de alto)
- 🏗️ **Torre**: Apilamiento vertical con alineación central
- 🔗 **Vínculo**: Cuando una taza está tapada, ambas se mueven juntas

---

## ✨ Características

<table>
<tr>
<td width="50%">

### 🏗️ Gestión de Torre
- ✅ Crear torre con ancho y alto personalizados
- 📏 Visualización con marcas de altura en cm
- 👁️ Modo visible/invisible

</td>
<td width="50%">

### 🎯 Manipulación
- ➕ Agregar/eliminar tazas
- 🎩 Agregar/eliminar tapas
- 🔄 Ordenar elementos
- 🔃 Invertir orden

</td>
</tr>
<tr>
<td width="50%">

### 📊 Consultas
- 📐 Altura total de elementos apilados
- 🔍 Información de tazas tapadas
- 📋 Listado de elementos (tipo y cantidad)

</td>
<td width="50%">

### 🎨 Visualización
- 🌈 Tazas con colores diferentes
- 🎨 Tapas del mismo color que su taza
- 🎭 Representación visual clara

</td>
</tr>
</table>

---

## 🎯 Requisitos Funcionales primer ciclo

| # | Función | Descripción |
|---|---------|-------------|
| 1️⃣ | **Create Tower** | Crear torre con dimensiones específicas (ancho × alto) |
| 2️⃣ | **Manage Cup** | Agregar o eliminar tazas de la torre |
| 3️⃣ | **Manage Lid** | Agregar o eliminar tapas de la torre |
| 4️⃣ | **Order Tower** | Ordenar elementos de mayor a menor (solo los que quepan) |
| 5️⃣ | **Reverse Tower** | Invertir el orden de los elementos (solo los que quepan) |
| 6️⃣ | **Height Query** | Consultar altura total de elementos apilados |
| 7️⃣ | **Stack Info** | Consultar información de tazas tapadas y elementos (tipo y número) |
| 8️⃣ | **Set Visibility** | Hacer visible/invisible el simulador (modo invisible funcional) |
| 9️⃣ | **Exit** | Terminar la ejecución del simulador |

---

## 🏛️ Diseño

### 📦 Clase Principal: `Tower`

```java
public class Tower {
    // Atributos
    + Tower(width: int, maxHeight: int)
    
    // Gestión de elementos
    + pushCup(i: int): void
    + popCup(): void
    + removeCup(i: int): void
    + pushLid(i: int): void
    + popLid(): void
    + removeLid(i: int): void
    
    // Reorganización
    + orderTower(): void
    + reverseTower(): void
    
    // Consultas
    + height(): int
    + lidedCups(): int[]
    + stackingItems(): String[][]
    
    // Visibilidad
    + makeVisible(): void
    + makeInvisible(): void
    + exit(): void
    + ok(): boolean
}
```

### 🎨 Principios de Diseño

- 🧩 **Reutilización**: Basado en el proyecto `shapes`
- 🔧 **Extensibilidad**: Diseño preparado para futuras mejoras
- 📐 **Encapsulamiento**: Separación clara de responsabilidades
- 🎯 **OOP**: Programación orientada a objetos

---

## 🛠️ Tecnologías

<div align="center">

| Herramienta | Propósito |
|-------------|-----------|
| ☕ **Java** | Lenguaje de programación |
| 🔵 **BlueJ** | IDE de desarrollo |
| 📊 **Astah** | Diagramas UML (clases y secuencia) |
| 🎨 **Shapes** | Librería de componentes gráficos |
| 🌳 **Git** | Control de versiones |

</div>

---

## 🚀 Inicio Rápido

### 📋 Prerrequisitos

- ☕ Java JDK 8 o superior
- 🔵 BlueJ IDE
- 📦 Proyecto `shapes` (incluido como dependencia)

### 🔧 Instalación

```bash
# Clonar el repositorio
git clone [URL-del-repositorio]

# Abrir en BlueJ
# File → Open Project → Seleccionar carpeta stackingItems
```

### ▶️ Ejecución

1. Abrir el proyecto en BlueJ
2. Compilar todas las clases
3. Hacer clic derecho en `Tower`
4. Crear nueva instancia con parámetros `(width, maxHeight)`
5. ¡Empezar a apilar!

---

## 📐 Reglas del Simulador

> ⚠️ **Importante**: Solo se incluyen elementos que quepan en la torre

### 🎲 Comportamiento Especial

- 🔗 **Tazas tapadas**: Si una taza tiene su tapa, ambas se mueven juntas
- 📏 **Ordenamiento**: Solo incluye elementos que quepan al ordenar
- 🔄 **Inversión**: Solo incluye elementos que quepan al invertir
- ⚠️ **Mensajes**: Si el simulador está visible, muestra JOptionPane en operaciones inválidas

### 🎨 Aspectos Visuales

- 🌈 Cada taza tiene un color único
- 🎨 Las tapas coinciden con el color de su taza
- 📏 Marcas de altura en centímetros (sin números)
- 🎯 Alineación vertical de todos los elementos

---

## 📦 Estructura del Proyecto

```
stackingItems/
│
├── 📁 domain/
│   ├── Tower.java          # Clase principal
│   ├── Cup.java            # Clase taza
│   ├── Lid.java            # Clase tapa
│   └── ...                 # Otras clases de dominio
│
├── 📁 shapes/              # Librería de componentes gráficos
│   └── ...
│
├── 📁 test/                # Pruebas unitarias
│   └── ...
│
├── 📄 README.md            # Este archivo
└── 📄 package.bluej        # Configuración BlueJ
```

---

## 📚 Entregables

### 🎯 Ciclo 1

- [x] 📊 Diagrama de clases (Astah)
- [x] 🔄 Diagramas de secuencia de métodos principales
- [x] 💻 Código fuente con documentación Javadoc
- [x] 🔍 Retrospectiva del proyecto
- [x] 🌳 Repositorio Git

---

## 🤝 Equipo de Desarrollo

**Escuela Colombiana de Ingeniería Julio Garavito**  
*Desarrollo Orientado por Objetos (POOB)*  
*Ciclo 1 - 2026-1*

---

## ⚠️ Nota Importante

> 🚫 Este simulador **NO** resuelve el problema completo de la maratón ICPC  
> ✅ Solo implementa la **visualización y manipulación** de torres también parte de operaciones del problema de la maratón  

---

## 📝 Licencia

Proyecto académico - Escuela Colombiana de Ingeniería

---

<div align="center">

**¿Preguntas? ¿Sugerencias?**  
Abre un [issue](../../issues) o contacta al equipo de desarrollo

---

Hecho con ☕ y 💙 por estudiantes de la ECI

</div>
