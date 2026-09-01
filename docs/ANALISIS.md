# Análisis — Ae2: Implementación comparativa de patrones de diseño

Factory Method y Builder aplicados al Sistema de gestión de tutorías.

## 1. Caso base

El Sistema de gestión de tutorías necesita notificar eventos (una reserva
creada, confirmada, cancelada, etc.) por distintos canales, y necesita
construir objetos `Reserva` cuya configuración incluye varios datos
obligatorios y varios datos opcionales. Esta actividad resuelve ambos
problemas con dos patrones creacionales distintos, sobre el mismo dominio
trabajado en semanas anteriores.

## 2. Parte A — Factory Method

### 2.1 Problema inicial

Antes de aplicar el patrón, el código que envía una notificación tendría
que decidir con una estructura condicional qué implementación concreta
usar según el canal:

```java
Notificador notificador;
if (canal == Canal.EMAIL) {
    notificador = new NotificadorCorreo();
} else if (canal == Canal.SMS) {
    notificador = new NotificadorSMS();
} else if (canal == Canal.PUSH) {
    notificador = new NotificadorPush();
} else {
    throw new IllegalArgumentException("Canal no soportado");
}
notificador.notificar(destinatario, mensaje);
```

Ese bloque `if/else` tendría que repetirse (o mantenerse centralizado y
modificarse) cada vez que se agrega un canal nuevo, y mezcla la decisión
de "qué crear" con la lógica de negocio que usa el objeto creado.

### 2.2 Solución con Factory Method

- **Product:** `Notificador` (interfaz) — contrato `notificar(destinatario, mensaje)`.
- **ConcreteProduct:** `NotificadorCorreo`, `NotificadorSMS`, `NotificadorPush` y, agregado después, `NotificadorWhatsApp`.
- **Creator:** `NotificadorFactory` (clase abstracta) — declara el factory method `crearNotificador()` y el método plantilla `enviar(destinatario, mensaje)` que lo usa.
- **ConcreteCreator:** `NotificadorCorreoFactory`, `NotificadorSMSFactory`, `NotificadorPushFactory` y `NotificadorWhatsAppFactory`.

Código: [`src/main/java/edu/uees/patrones/factory`](../src/main/java/edu/uees/patrones/factory).
Diagrama: [`factory-method.png`](factory-method.png) / [`factory-method.puml`](factory-method.puml).

### 2.3 Extensibilidad demostrada

`NotificadorWhatsApp` y `NotificadorWhatsAppFactory` se agregaron **después**
de tener funcionando correo, SMS y push, sin modificar ninguna clase
existente (ver `DemoFactoryMethod`).

**Qué clases cambian:** ninguna de las existentes. Se agregan dos clases
nuevas: un ConcreteProduct (`NotificadorWhatsApp`) y un ConcreteCreator
(`NotificadorWhatsAppFactory`).

**Qué clases permanecen estables:** `Notificador` (el contrato no cambia),
`NotificadorFactory` (el método plantilla `enviar()` no cambia) y todo el
código cliente que ya dependía de `NotificadorFactory` (no necesita
recompilarse ni conocer la clase nueva). Esto es la aplicación concreta
del principio abierto/cerrado (OCP): el sistema se extiende agregando
código, no modificando el que ya funciona.

## 3. Parte B — Builder

### 3.1 Problema inicial (constructor telescópico)

`Reserva` tiene 3 campos obligatorios (estudiante, docente, horario) y 4
opcionales (modalidad, notas, canal de notificación, recordatorio). Un
único constructor con todos los parámetros se vería así:

```java
new Reserva("R-1", "Ana Perez", "Jaime Sayago",
            LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0),
            Modalidad.VIRTUAL, "recordar traer material",
            CanalNotificacion.SMS, true);
```

Problemas concretos de este enfoque:

- Es difícil de leer: no queda claro, en el sitio de la llamada, qué
  representa cada valor posicional.
- Para usar solo los campos obligatorios hay que pasar explícitamente
  valores por defecto para todos los opcionales.
- Dos parámetros del mismo tipo (`horaInicio` y `horaFin`, ambos
  `LocalTime`) se pueden intercambiar por error sin que el compilador lo
  detecte.
- Agregar un nuevo campo opcional obliga a crear otro constructor
  sobrecargado o a romper todas las llamadas existentes.

### 3.2 Solución con Builder

- **Product:** `Reserva` (inmutable, constructor de paquete que solo `ReservaBuilder` puede invocar).
- **Builder:** `ReservaBuilder` — API fluida (`estudiante()`, `docente()`, `horario()`, `modalidad()`, `notas()`, `canalNotificacion()`, `conRecordatorio()`) y `build()`.
- **Validación:** `build()` verifica que estudiante, docente y horario estén presentes, y que `horaInicio` sea anterior a `horaFin`; lanza `IllegalStateException` con el detalle de los campos faltantes si no se cumple.
- **Valores por defecto:** modalidad `PRESENCIAL`, canal `EMAIL`, notas `""`, recordatorio `false`.

No se usa un `Director` separado porque la construcción de `Reserva` no
tiene una secuencia fija que valga la pena encapsular aparte del propio
`ReservaBuilder`; el orden de las llamadas fluidas es libre.

Código: [`src/main/java/edu/uees/patrones/builder`](../src/main/java/edu/uees/patrones/builder).
Diagrama: [`builder.png`](builder.png) / [`builder.puml`](builder.puml).

### 3.3 Configuraciones de ejemplo

`DemoBuilder` construye dos `Reserva` distintas: una solo con los campos
obligatorios (toma todos los valores por defecto) y otra con todos los
campos opcionales personalizados (modalidad virtual, canal SMS, notas y
recordatorio activado), además de mostrar la excepción que se lanza
cuando faltan campos obligatorios.

## 4. Parte D — Comparación técnica

| Criterio | Factory Method | Builder |
|---|---|---|
| Problema que resuelve | Desacoplar al cliente de qué clase concreta se instancia cuando existen varias implementaciones intercambiables de un mismo contrato. | Desacoplar la construcción de un objeto complejo (con datos obligatorios y opcionales) de su representación final, evitando constructores con muchos parámetros. |
| Variabilidad principal | Qué **clase concreta** del producto se crea (varía el "qué"). | Qué **combinación de valores** tiene el objeto final (varía el "cómo se arma"). |
| Participantes | Product, ConcreteProduct, Creator, ConcreteCreator. | Product (objeto a construir), Builder, (Director opcional). |
| Ventaja principal | Agregar una variante nueva no requiere tocar el código cliente ni las clases existentes (OCP). | Construcción legible, auto-descriptiva y segura: cada campo se identifica por nombre, no por posición. |
| Costo / consecuencia | Una clase adicional por cada variante (Product + Creator), lo que puede ser excesivo si solo hay una o dos implementaciones. | Una clase Builder adicional; para objetos simples puede ser sobreingeniería. |
| Cuándo utilizarlo | Cuando existen varias implementaciones de un mismo contrato y se espera que la lista crezca (nuevos canales, nuevos formatos, nuevos algoritmos). | Cuando un objeto tiene varios parámetros, algunos obligatorios y otros opcionales, y su construcción puede beneficiarse de valores por defecto y validación centralizada. |
| Cuándo evitarlo | Si solo existe una implementación posible y no se prevé que cambie, el patrón agrega complejidad sin beneficio real. | Si la clase tiene dos o tres atributos simples y sin combinaciones opcionales, un constructor normal es más directo. |

### Aplicación al taller "¿Factory Method, Builder o ninguno?"

| Situación | Decisión | Justificación |
|---|---|---|
| Diferentes mecanismos de notificación | Factory Method | Varias implementaciones intercambiables de un mismo contrato (`Notificador`) que se espera que crezcan; es exactamente el caso implementado en la Parte A. |
| Objeto con múltiples parámetros opcionales | Builder | Es el caso de `Reserva`: pocos campos obligatorios y varios opcionales con valores por defecto; construcción legible con Fluent API. |
| Clase sencilla con dos atributos | Ninguno | Un constructor normal con dos parámetros ya es legible; ni la variabilidad de Factory Method ni la construcción progresiva de Builder aportan beneficio. |
| Diferentes implementaciones de generación de reportes | Factory Method | Mismo razonamiento que las notificaciones: varias implementaciones (PDF, Excel, CSV, ...) de un contrato común (`GeneradorReporte`), seleccionables e intercambiables. |

## 5. Conclusiones

- Factory Method y Builder resuelven problemas de creación de objetos
  distintos: uno decide **qué clase concreta** instanciar entre varias
  alternativas intercambiables; el otro organiza **cómo se ensamblan**
  los datos de un único objeto complejo.
- Ambos patrones se justifican por un problema real del dominio (varios
  canales de notificación que crecerán, y una `Reserva` con datos
  obligatorios/opcionales), no se aplicaron "porque sí": aplicar un
  patrón sin un problema concreto agrega clases e indirección sin
  beneficio.
- En el sistema de tutorías conviven bien: `ServicioReservas` (de la
  actividad anterior) podría recibir una `Reserva` ya construida con
  `ReservaBuilder` y notificar usando cualquier `NotificadorFactory`, sin
  que ninguno de los dos patrones dependa del otro.
