# CineTrack

Aplicación web full stack para llevar el control personal de las películas que quieres ver, estás viendo o ya viste.

**Stack:** Angular 21 (frontend) · Spring Boot 4 + Spring Data JPA (backend) · H2 en archivo (persistencia) · API externa TMDB.

## Problema que resuelve
Las personas que ven muchas películas pierden el rastro de lo que tienen pendiente, lo que ya vieron y qué opinaron.
CineTrack centraliza el catálogo personal, permite calificarlo y reseñarlo, y autocompleta los datos de cada película consultando TMDB.

## Usuarios principales
- Aficionados al cine que quieren organizar su catálogo personal.

## Recursos de la API propia
| Recurso | Endpoints |
|---|---|
| **Película** | `GET /peliculas`, `GET /peliculas/{id}`, `POST /peliculas`, `PUT /peliculas/{id}`, `DELETE /peliculas/{id}` |
| **Reseña** | `GET /resenas`, `GET /resenas/{id}`, `GET /resenas/pelicula/{peliculaId}`, `POST /resenas`, `PUT /resenas/{id}`, `DELETE /resenas/{id}` |
| Dashboard | `GET /dashboard` |
| API externa (proxy) | `GET /tmdb/buscar?titulo=...` |

## Reglas de negocio (en Spring Boot, capa `service`)
1. **No se permiten películas duplicadas**: mismo título y año (al crear y al editar).
2. **Calificación válida**: si la película se califica, la nota debe estar entre 1.0 y 5.0 (0 = sin calificar).
3. **Una película solo puede marcarse como VISTA si tiene calificación** (1.0 a 5.0).
4. **Una reseña debe pertenecer a una película existente**.

## Validaciones y códigos HTTP
| Código | Cuándo |
|---|---|
| 200 OK | Consultas y actualizaciones exitosas |
| 201 Created | Creación de película o reseña |
| 204 No Content | Eliminación exitosa |
| 400 Bad Request | Campos vacíos, año/calificación inválidos, reglas de negocio incumplidas |
| 404 Not Found | Id inexistente |
| 503 Service Unavailable | TMDB no disponible o sin configurar |

Todos los errores devuelven `{"mensaje": "..."}` (ver `GlobalExceptionHandler`) y Angular lo muestra al usuario.

## Integración con TMDB
Al crear una película, el formulario busca en TMDB (a través del backend), y al elegir un resultado se autocompletan título, año, género, sinopsis y póster.
Los datos que el usuario agrega (estado, calificación, reseñas) pertenecen a CineTrack y se guardan en la base de datos propia.
Si TMDB falla, el backend responde 503 y el formulario sigue funcionando de forma manual.

## Cómo ejecutar

### 1. Backend (puerto 8080)
Necesitas una API key de TMDB (https://www.themoviedb.org/settings/api) definida como variable de entorno:

```bash
# Linux / macOS
export TMDB_API_KEY=tu_key
# Windows (PowerShell)
$env:TMDB_API_KEY="tu_key"

cd cinetrack
./mvnw spring-boot:run
```
La base de datos H2 se guarda en `cinetrack/data/` (los datos sobreviven al reinicio). Consola: http://localhost:8080/h2-console

### 2. Frontend (puerto 4200)
```bash
cd cinetrack-frontend
npm install
npm start
```
Abrir http://localhost:4200

> **Nota de seguridad:** nunca subas la API key al repositorio. Se lee de la variable de entorno `TMDB_API_KEY`.

## CORS
Angular (`localhost:4200`) y Spring Boot (`localhost:8080`) son orígenes distintos, y el navegador bloquea por defecto las respuestas entre orígenes.
`CorsConfig` autoriza a `http://localhost:4200` para los métodos GET, POST, PUT, DELETE y OPTIONS.

## Arquitectura
```
Angular (components / services / models)
      │  HttpClient
      ▼
Spring Boot: Controller → Service (reglas de negocio) → Repository (JPA) → H2
                      └→ TmdbService → API TMDB
```
