# Práctica 1 · Videoteca: de la ventana a los ficheros

**Módulo:** Acceso a Datos (DAM) · **UD1:** Manejo de ficheros

Práctica en Java (Maven) en la que se implementa la capa de persistencia de una aplicación de videoteca. La interfaz gráfica ya está hecha (Equipo A) y solo habla con un contrato de operaciones (`ServicioVideoteca`). El trabajo consiste en programar lo que hay debajo de ese contrato (Equipo B): exportar e importar datos en distintos formatos, leer la configuración y recuperar carátulas de un fichero de acceso directo.

## Índice

1. [Arquitectura por capas](#arquitectura-por-capas)
2. [Contrato `ServicioVideoteca`](#contrato-serviciovideoteca)
3. [Qué está hecho y qué programas](#qué-está-hecho-y-qué-programas)
4. [Las 9 tareas](#las-9-tareas)
5. [Configuración (`conf.properties`)](#configuración-confproperties)
6. [Estructura de ficheros y ejecución](#estructura-de-ficheros-y-ejecución)
7. [Entrega y evaluación](#entrega-y-evaluación)
8. [Ampliaciones](#ampliaciones)
9. [Anexo: recursividad sobre directorios](#anexo-recursividad-sobre-directorios)

## Arquitectura por capas

Cada capa solo conoce a la que tiene debajo. Las excepciones suben hasta la interfaz, que muestra su mensaje.

| Capa | Responsabilidad | Estado |
|------|-----------------|--------|
| 1 · UI | Ventana y entrada del usuario | Hecha |
| 2 · Contrato | Operaciones que la UI puede pedir (`ServicioVideoteca`) | Hecha |
| 3 · Negocio | Datos en memoria y orden de llamadas (`ServicioVideotecaImpl`) | Pendiente |
| 4 · Persistencia | Lectura y escritura de cada tipo de fichero | Pendiente |
| 5 · Ficheros | Lo que queda en disco | — |

**Orden de trabajo recomendado:** exportar → importar → configuración e imágenes → crear datos.

## Contrato `ServicioVideoteca`

| Método | Qué hace | Lanza |
|--------|----------|-------|
| `iniciar()` | Lee `./config/conf.properties` si existe y devuelve un mensaje. No lanza excepciones. | Nada |
| `listarCategorias()` | Devuelve las categorías en memoria con sus películas | — |
| `crearCategoria(nombre)` | Crea una categoría con id automático | `IllegalArgumentException` |
| `crearPelicula(...)` | Crea una película en una categoría con id automático | `IllegalArgumentException` |
| `importar(formato, fichero)` | Lee el fichero y **reemplaza** los datos en memoria solo si la lectura es correcta | `IOException`, `ExcepcionDirectorioNoExiste`, `ExcepcionFicheroNoExiste` |
| `exportar(formato, fichero)` | Escribe los datos en memoria | `IOException`, `ExcepcionDirectorioNoExiste`, `ExcepcionOperacionCancelada` |
| `obtenerImagen(id)` | Devuelve la carátula o `null` | `IOException` |

Notas:
- La videoteca arranca con 3 categorías y 4 películas de ejemplo en memoria.
- `exportar` no pregunta si debe sobrescribir: lo decide el `.properties` (por defecto `false`).
- Cualquier método puede lanzar también `UnsupportedOperationException` (pendiente de implementar), `IllegalStateException` (sin configurar) o `IllegalArgumentException` (datos no válidos).
- Las tres excepciones propias (paquete `videoteca.excepciones`) ya están creadas. Solo hay que usarlas.

## Qué está hecho y qué programas

| Clase | Ya hecho | Lo programas tú |
|-------|----------|-----------------|
| `ServicioVideotecaImpl` | `listarCategorias`, `exportar`, `importar` y constructores | `iniciar`, `crearCategoria`, `crearPelicula`, `obtenerImagen` |
| `GeneradorIds` | Todo (id = mayor + 1 sobre todas las películas) | — |
| `ComprobacionRutas` | — | `comprobarExportacion`, `comprobarImportacion` |
| `Configuracion` | Enumerados, constructor, `porDefecto()` y getters | `cargar(fichero)` |
| `GestorTextoPlano` | — | `guardarCategoriasTexto`, `obtenerCategoriasTexto` |
| `GestorJSON` | Métodos base (eligen la variante) | **Una** variante de guardar y **una** de obtener (Gson o Jackson) |
| `GestorXML` | Métodos base | Lectura: **una** de SAX, JAXB o DOM. Escritura: **una** de JAXB o DOM |
| `AlmacenImagenes` | Constructor, `close()`, `TAMANO_SLOT` | `abrir`, `obtener` e índice (solo lectura) |

Los métodos base miran `conf.properties` y llaman a la variante configurada. Tú solo rellenas las variantes que elijas.

## Las 9 tareas

| # | Tarea | Qué verás en la ventana |
|---|-------|-------------------------|
| 1 | Exportar a texto plano | Se exportan los datos de ejemplo |
| 2 | Exportar a JSON | Exportación JSON |
| 3 | Exportar a XML | Exportación XML |
| 4 | Importar de texto plano | Cambian las listas |
| 5 | Importar de JSON | Importación JSON |
| 6 | Importar de XML | Importación XML |
| 7 | Configuración y arranque | Mensaje en la barra de estado |
| 8 | Imágenes | Se muestran las carátulas |
| 9 | Crear categorías y películas | Se crean datos nuevos |

Hasta la tarea 7 la ventana no sabe qué librería usar para JSON y XML.

### Bloque A · Exportar

**Tarea 1 · Texto plano.** Programas `ComprobacionRutas.comprobarExportacion` y `GestorTextoPlano.guardarCategoriasTexto`.
- `comprobarExportacion(fichero, sobrescribir)`: si el directorio no existe, `ExcepcionDirectorioNoExiste`; si el fichero existe y `sobrescribir` es `false`, `ExcepcionOperacionCancelada`; en otro caso se continúa.
- Formato: una línea `CATEGORIA;id;nombre` y debajo, una línea `PELICULA;id;titulo;director;anio;duracionMinutos` por cada película de esa categoría.

```
CATEGORIA;1;Ciencia ficción
PELICULA;101;Blade Runner;Ridley Scott;1982;117
CATEGORIA;3;Terror
```

**Tarea 2 · JSON (elige una).** Una variante de `GestorJSON` para guardar: `guardarCategoriasJSONGson` (Gson) o `guardarCategoriasJSONJackson` (Jackson).

**Tarea 3 · XML (elige una).** Una variante de `GestorXML` para guardar: `guardarCategoriasXMLJAXB` (menos código) o `guardarCategoriasXMLDOM` (más trabajo). Todo se escribe con atributos:

```xml
<videoteca>
  <categoria id="1" nombre="Ciencia ficción">
    <pelicula id="101" titulo="Blade Runner" director="Ridley Scott"
              anio="1982" duracionMinutos="117"/>
  </categoria>
  <categoria id="3" nombre="Terror"/>
</videoteca>
```

### Bloque B · Importar

**Tarea 4 · Texto plano.** Programas `ComprobacionRutas.comprobarImportacion` y `GestorTextoPlano.obtenerCategoriasTexto`.
- `comprobarImportacion(fichero)`: si el directorio padre no existe, `ExcepcionDirectorioNoExiste`; si no es un fichero regular, `ExcepcionFicheroNoExiste`.
- `obtenerCategoriasTexto(fichero)`: lee línea a línea, ignora líneas en blanco y separa por `;`. Una línea desconocida, un `PELICULA` antes de cualquier `CATEGORIA` o un número mal escrito lanza `IOException` con un mensaje claro.
- **Truco:** exporta primero (tarea 1) e importa ese mismo fichero. Los datos deben salir iguales.

**Tarea 5 · JSON (elige una).** Variante de `GestorJSON` para obtener: `obtenerCategoriasJSONGson` o `obtenerCategoriasJSONJackson`. Con Gson, un `JsonParseException` debe convertirse en `IOException`. Jackson ya lanza `IOException` por sí mismo.

**Tarea 6 · XML (elige una).** Variante de `GestorXML` para obtener: `obtenerCategoriasXMLSAX` (requiere `VideotecaHandler`), `obtenerCategoriasXMLJAXB` o `obtenerCategoriasXMLDOM`.

### Bloque C · Configuración e imágenes

**Tarea 7 · Configuración.** Programas `Configuracion.cargar(fichero)` e `iniciar()`. Esta tarea se considera terminada cuando T7.a a T7.f salen en `[ OK ]`. La ruta es fija: `./config/conf.properties`, y se lee una sola vez, al arrancar.

`iniciar()` nunca lanza excepciones, todo va dentro del mensaje:
1. Si el fichero existe, se carga en `configuracion` y el mensaje contiene `EXISTE`.
2. Si no existe, se usa la configuración por defecto y el mensaje contiene `NO EXISTE`.
3. Si existe pero falla al leerlo, valores por defecto y el mensaje contiene `EXISTE` y el motivo.

**Tarea 8 · Imágenes.** Programas `AlmacenImagenes` y `obtenerImagen`. Los ficheros `imagenes.dat` e `indice.dat` se entregan hechos, con las carátulas de las 4 películas de ejemplo.

| Pieza | Regla |
|-------|-------|
| `imagenes.dat` | Registros de `TAMANO_SLOT` bytes (100 KB), todos iguales. Cada slot es `[bytes de la imagen][relleno de ceros]`, sin longitud por delante. |
| Posición del registro n | `n × TAMANO_SLOT`, usando `(long)` |
| `indice.dat` | `HashMap<Integer,Integer>` serializado: idPelicula → nº de registro |

Pasos de `AlmacenImagenes`:
1. `abrir()`: `RandomAccessFile` en modo `"r"` y carga del índice con `ObjectInputStream`.
2. `obtener(idPelicula)`: si el id no está en el índice, devuelve `null`. Si está, hace `seek` a la posición y `readFully` del slot.
3. Quita los ceros finales y devuelve la parte útil con `Arrays.copyOf`.

> Se pueden quitar los ceros finales porque las carátulas son PNG, y un PNG siempre termina en `0x82`.

En el servicio, `obtenerImagen(id)` busca `imagenes.dat` e `indice.dat` en `config/` **y en todos sus subdirectorios de forma recursiva**. Si no existen, devuelve `null` (no es un error). Abre y cierra el almacén en cada operación con try-with-resources.

### Bloque D · Datos en memoria

**Tarea 9 · Crear categorías y películas.** Programas `crearCategoria` y `crearPelicula`. Los ids los calcula `GeneradorIds`, solo hay que llamarlo.
- `crearCategoria(nombre)`: si el nombre es nulo o en blanco, `IllegalArgumentException`. Crea `Categoria` con el id generado, la añade a la lista y la devuelve.
- `crearPelicula(idCategoria, titulo, director, anio, duracionMinutos)`: si falta el título, `IllegalArgumentException`. Si la categoría no existe, `IllegalArgumentException`. Crea `Pelicula`, la añade a esa categoría y la devuelve.

## Configuración (`conf.properties`)

| Clave | Valores | Por defecto |
|-------|---------|-------------|
| `sobrescribir` | `true` / `false` | `false` |
| `importacion_json` | `gson` / `jackson` | `gson` |
| `exportacion_json` | `gson` / `jackson` | `gson` |
| `importacion_xml` | `sax` / `jaxb` / `dom` | `dom` |
| `exportacion_xml` | `jaxb` / `dom` (SAX no puede escribir) | `dom` |

Ejemplo:

```properties
# true: sobrescribe al exportar | false (por defecto): cancela si ya existe
sobrescribir=false
# JSON: gson | jackson
importacion_json=jackson
exportacion_json=jackson
# XML lectura: sax | jaxb | dom    XML escritura: jaxb | dom
importacion_xml=sax
exportacion_xml=jaxb
```

**Ojo:** los comentarios (`#`) van en líneas aparte. Un `#` al final de un valor forma parte del valor.

## Estructura de ficheros y ejecución

```
<carpeta del proyecto>/
├── pom.xml
├── src/...
└── config/              ← la creas tú, a mano
    ├── conf.properties  ← lo escribes tú (tarea 7)
    ├── imagenes.dat     ← entregado
    └── indice.dat       ← entregado
```

- `./config/` es relativa a la carpeta desde la que se ejecuta (en el IDE, la raíz del proyecto). Por eso el mensaje de arranque muestra la ruta absoluta.
- Importar y exportar usan el directorio que escribe el usuario en la ventana, pero **ese directorio tiene que existir**: el código no crea carpetas.
- El `pom.xml` ya incluye todas las librerías. Abre el proyecto como **proyecto Maven**.

## Entrega y evaluación

**Entrega:** un ZIP con `src/` y `pom.xml`, sin la carpeta `config/`.

**Evaluación:** el código se corrige leyéndolo. Se valora qué se ha implementado y cómo.

| Criterio | Qué se busca |
|----------|--------------|
| Uso de librerías | Gson/Jackson y SAX/JAXB/DOM bien usados; flujos cerrados con try-with-resources |
| Capas separadas | Los ficheros solo en persistencia; el servicio no conoce las librerías |
| Excepciones | Las 3 propias se lanzan y suben hasta la ventana con mensajes claros |
| Configuración | Se lee una vez; `sobrescribir` vale `false` por defecto; técnica sin configurar controlada |
| Almacén de imágenes | Lectura correcta del registro (posición y ceros finales) y del índice serializado |

## Ampliaciones

Opcionales:
- Programar otra técnica de JSON o XML y cambiar `conf.properties` para probarla.
- Añadir modificar y eliminar. Pista: ¿qué pasa con los ids si se borra la última película?

## Anexo: recursividad sobre directorios

Un método recursivo se llama a sí mismo para resolver una versión más pequeña del mismo problema. Necesita tres cosas:

1. **Caso base:** una situación en la que ya no se llama a sí mismo. Sin él, `StackOverflowError`.
2. **Paso recursivo:** la llamada a sí mismo con un problema más pequeño.
3. **Avance:** cada llamada se acerca al caso base. Si no, bucle infinito.

```java
static int sumar(int n) {
    if (n == 0) return 0;               // caso base
    return n + sumar(n - 1);            // paso recursivo
}
// sumar(3) = 3 + 2 + 1 + 0 = 6
```

Plantilla para directorios:

```
método(directorio):
    si el directorio no es válido → caso base
    para cada elemento del directorio:
        si es un fichero → hacer algo con él
        si es un directorio → método(elemento)   ← paso recursivo
```

Ejemplo resuelto (distinto al de la práctica), contar ficheros en un directorio y sus subdirectorios:

```java
static int contarFicheros(File directorio) {
    if (directorio == null || !directorio.isDirectory()) return 0;  // caso base
    File[] elementos = directorio.listFiles();
    if (elementos == null) return 0;                                 // no se puede leer
    int total = 0;
    for (File elemento : elementos) {
        if (elemento.isFile()) {
            total++;
        } else {
            total += contarFicheros(elemento);                       // paso recursivo
        }
    }
    return total;
}
```

**Errores típicos:**
- Olvidar el caso base → `StackOverflowError`.
- Llamar al método recursivo y no usar lo que devuelve: el resultado se pierde.
- No devolver nada al terminar el bucle: hace falta un valor por defecto.
- `listFiles()` puede devolver `null` (sin permisos o error de lectura): hay que comprobarlo.
- `Files.walk` y `Files.find` hacen lo mismo, pero en esta práctica hay que implementar la recursividad a mano.

**Cómo probarlo:** crea esta estructura y comprueba que encuentra el fichero en cada nivel:

```
config/
├── conf.properties
└── antiguas/
    └── 2025/
        ├── imagenes.dat
        └── indice.dat
```
