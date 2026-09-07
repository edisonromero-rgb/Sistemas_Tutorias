# Ae3 — Incremento 1 del proyecto

Sistema de Gestión de Tutorías · Diseño de Software UCOM0310 · Semana 4

## 1. Estado inicial (antes de este incremento)

El repositorio traía dos árboles de código desconectados:

- `edu.uees.tutorias` (Ae1): el dominio real — `Usuario`, `Estudiante`,
  `Docente`, `HorarioDisponible`, `Reserva`, `EstadoReserva`,
  `ServicioReservas`, un `Notificador` simple (solo `NotificadorCorreo`)
  y `ReservaRepository`/`ReservaRepositoryMemoria`.
- `edu.uees.patrones` (Ae2): una demo aislada de Factory Method
  (`Notificador` basado en `String destinatario`) y Builder (un
  `Reserva` de juguete con `String estudiante/docente`, sin relación
  con el dominio de Ae1).

`ServicioReservas` construía la reserva con `new Reserva(id, estudiante,
docente, horario)` y, después de cada operación, llamaba directamente a
`Notificador.notificar(...)`. Antes de modificar nada se verificó la
línea base con `mvn clean test`: compilaba y las 7 pruebas de
`ServicioReservasTest` pasaban.

## 2. Problemas de diseño identificados

### Problema 1 — Reglas de cancelación variables por tipo de reserva

El sistema no distinguía tipos de reserva ni antelación mínima para
cancelar. En un sistema real, una tutoría grupal (afecta a varios
estudiantes) debería exigir más antelación que una prioritaria. Sin un
punto de extensión, esa regla habría terminado como un `if/else` dentro
de `ServicioReservas` o de `Reserva`, mezclando una política de negocio
variable con la coordinación del caso de uso o con el ciclo de vida de
la reserva.

### Problema 2 — `ServicioReservas` acoplado a la notificación

Cada operación (`crearReserva`, `confirmarReserva`, `cancelarReserva`,
`reprogramarReserva`, `finalizarReserva`) terminaba con una o más
llamadas manuales a `notificador.notificar(...)`, repitiendo la
decisión de "quién debe enterarse de este cambio". Agregar un interesado
nuevo en los cambios de estado (un calendario, un panel administrativo,
una bitácora de auditoría) obligaba a modificar `ServicioReservas` cada
vez, violando OCP y mezclando coordinación con notificación (SRP).

## 3. Qué se recuperó de Ae2 (Factory Method y Builder)

| Patrón | Problema que resuelve en mi proyecto | ¿Se mantiene? | Justificación |
|---|---|---|---|
| **Factory Method** | Elegir la implementación de `Notificador` (correo, SMS, push, WhatsApp) según el canal preferido de una reserva, sin un `if/else` repetido. | Sí | Se reintegra sobre el dominio real (`edu.uees.tutorias.notification.factory`) y ahora sí se usa desde producción: `ObservadorNotificaciones` lo consulta vía `NotificadorFactoryProvider` en cada cambio de estado. En Ae2 era una demo aislada que nadie invocaba. |
| **Builder** | Construir `Reserva` con datos obligatorios (estudiante, docente, horario) y opcionales (modalidad, notas, canal preferido, recordatorio, tipo) sin un constructor telescópico. | Sí | Se reintegra sobre el `Reserva` real del dominio (antes construía un objeto de juguete con `String`). Ahora es la **única** forma de crear una `Reserva`: su constructor es de paquete. Se le agregó el campo `tipo` (usado por Strategy) y `observador(...)` (usado por Observer). |

## 4. Patrones nuevos incorporados (Semana 4)

### Patrón 1 — Strategy (políticas de cancelación)

| Elemento | Detalle |
|---|---|
| Problema real | La antelación mínima para cancelar sin penalización varía según el tipo de reserva, y se espera que la política cambie con el tiempo. |
| Contexto | `ServicioReservas.cancelarReserva(id)` |
| Qué cambia | La regla de antelación mínima por tipo (24h normal, 2h prioritaria, 48h grupal). |
| Qué permanece estable | El contrato `PoliticaCancelacion.validar(Reserva)` y el flujo de `ServicioReservas`. |
| Patrón seleccionado | Strategy — `PoliticaCancelacion` (interfaz), `CancelacionNormalPolitica`, `CancelacionPrioritariaPolitica`, `CancelacionGrupalPolitica`, resueltas por `PoliticaCancelacionProvider` según `Reserva.getTipo()`. |
| Clases/interfaces implicadas | `PoliticaCancelacion`, sus 3 implementaciones, `PoliticaCancelacionProvider`, `CancelacionNoPermitidaException`, `TipoReserva`. |
| Principio SOLID relacionado | OCP (agregar un tipo de reserva nuevo no obliga a tocar las políticas existentes) y SRP (la regla de negocio no vive en `ServicioReservas` ni en `Reserva`). |
| Beneficio esperado | Cambiar o agregar una política no afecta al resto del sistema. |
| Costo/compromiso | Una interfaz y una clase por cada regla nueva; para un solo tipo de reserva habría sido sobreingeniería. |
| Cómo se verificó | `PoliticaCancelacionTest` (4 pruebas) + demo en `App.java`: una reserva GRUPAL con 18h de antelación es rechazada con `CancelacionNoPermitidaException`. |

### Patrón 2 — Observer (reacción a cambios de estado de Reserva)

| Elemento | Detalle |
|---|---|
| Problema real | Varios componentes independientes (notificación, calendario, panel administrativo) deben reaccionar cuando cambia el estado de una reserva. |
| Contexto | `Reserva.confirmar()/cancelar()/reprogramar()/finalizar()` y su creación. |
| Qué cambia | La lista de interesados y lo que hace cada uno ante un cambio de estado. |
| Qué permanece estable | `Reserva` no conoce a sus observadores concretos, solo el contrato `ReservaObserver`. |
| Patrón seleccionado | Observer — `Reserva` es el Subject; `ReservaObserver` el contrato; `ObservadorNotificaciones`, `ObservadorCalendario` y `ObservadorPanelAdministrativo` son los ConcreteObserver. |
| Clases/interfaces implicadas | `ReservaObserver`, las 3 implementaciones, `Reserva.agregarObservador()/notificarObservadores()`. |
| Principio SOLID relacionado | OCP (agregar un observador nuevo no toca `Reserva` ni `ServicioReservas`) y SRP (`ServicioReservas` deja de saber cómo se notifica). |
| Beneficio esperado | Desacopla el ciclo de vida de la reserva de sus efectos secundarios; `ServicioReservas` perdió su dependencia directa de `Notificador`. |
| Costo/compromiso | Una interfaz más y una lista de observadores en `Reserva`; para un solo tipo de reacción no se habría justificado. |
| Cómo se verificó | `ServicioReservasTest` (observador de prueba que registra eventos) + `ReservaBuilderTest` + demo en `App.java`, donde correo/SMS, calendario y panel reaccionan a cada transición sin que `ServicioReservas` los invoque explícitamente. |

## 5. Cohesión, acoplamiento y SOLID en el incremento

- **SRP**: `Reserva` sigue concentrando solo su ciclo de vida; ahora también avisa (no decide cómo) que cambió. `ServicioReservas` coordina; ya no notifica ni valida políticas de cancelación directamente, delega en `Reserva`, `ReservaObserver` y `PoliticaCancelacion`.
- **OCP**: nuevo canal de notificación → nueva `NotificadorFactory`. Nuevo tipo de reserva → nueva `PoliticaCancelacion`. Nuevo interesado en los cambios de estado → nuevo `ReservaObserver`. Ninguno de los tres casos toca código existente.
- **LSP**: cualquier `ReservaObserver`, `PoliticaCancelacion` o `NotificadorFactory` es intercambiable sin que el código cliente lo note.
- **ISP**: `ReservaObserver` tiene un único método; `PoliticaCancelacion` también.
- **DIP**: `ServicioReservas(ReservaRepository, List<ReservaObserver>)` — depende de abstracciones, no de `ReservaRepositoryMemoria`, `NotificadorCorreo` ni una política concreta.
- **Cambio deliberado**: `ServicioReservas` ya no recibe `Notificador` por constructor (como en Ae1). Se documenta como evolución: esa responsabilidad nunca fue suya, era código repetido después de cada operación; con Observer, `Reserva` la asumió correctamente.

## 6. Verificación

```
mvn clean test
...
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Suites: `ServicioReservasTest` (8), `ReservaBuilderTest` (5),
`NotificadorFactoryTest` (7), `PoliticaCancelacionTest` (4).

`mvn compile exec:java` ejecuta `App.java`: crea una reserva grupal
(SMS, con recordatorio), la confirma (dispara notificación y evento de
calendario), intenta cancelarla con solo 18h de antelación (la
política grupal exige 48h y la rechaza con `CancelacionNoPermitidaException`),
y crea/cancela una segunda reserva normal con antelación suficiente. Al
final imprime la bitácora del panel administrativo, que registró los
cuatro eventos sin que `ServicioReservas` supiera que ese observador
existía.

## 7. Conclusiones

El incremento 1 unificó dos árboles de código que hasta Ae2 vivían
desconectados y agregó dos patrones (Strategy y Observer) motivados por
problemas concretos, no por completar una lista. El cambio más
importante no fue agregar clases sino *quitarle* a `ServicioReservas`
una responsabilidad que nunca debió tener (decidir a quién notificar);
Observer permitió que esa decisión la tome cada interesado por su
cuenta, y Strategy permitió que la regla de cancelación varíe sin
tocar el flujo de coordinación. Factory Method y Builder de Ae2 no se
descartaron: se conectaron por primera vez a un flujo real.
