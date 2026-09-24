package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.dto.TmdbResultado;
import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService {

    private static final String MSG_NO_DISPONIBLE =
            "El servicio externo (TMDB) no está disponible en este momento";

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key:}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    // Los géneros casi nunca cambian: se piden una vez y se reutilizan
    private Map<Integer, String> generosEnCache;

    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TmdbResultado> buscarPeliculas(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Escribe un título para buscar");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new ServicioExternoNoDisponibleException(
                    "TMDB no está configurado (falta la variable de entorno TMDB_API_KEY)");
        }

        try {
            Map<Integer, String> mapaGeneros = obtenerMapaGeneros();

            // El título se codifica correctamente (espacios, &, #, tildes...)
            URI url = UriComponentsBuilder.fromUriString(apiUrl + "/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "es-ES")
                    .queryParam("query", "{titulo}")
                    .encode()
                    .buildAndExpand(titulo)
                    .toUri();

            JsonNode respuesta = restTemplate.getForObject(url, JsonNode.class);
            List<TmdbResultado> resultados = new ArrayList<>();

            if (respuesta == null || !respuesta.has("results")) {
                return resultados;
            }

            for (JsonNode item : respuesta.get("results")) {
                TmdbResultado r = new TmdbResultado();
                r.setTitulo(item.path("title").asText(""));
                r.setSinopsis(item.path("overview").asText(""));
                r.setAnio(extraerAnio(item.path("release_date").asText("")));

                String poster = item.path("poster_path").asText(null);
                r.setImagenUrl(poster != null ? "https://image.tmdb.org/t/p/w500" + poster : "");

                // Traducimos los genre_ids (números) a nombres usando el mapa
                StringBuilder generoTexto = new StringBuilder();
                if (item.has("genre_ids")) {
                    for (JsonNode idNode : item.get("genre_ids")) {
                        String nombre = mapaGeneros.get(idNode.asInt());
                        if (nombre != null) {
                            if (generoTexto.length() > 0) generoTexto.append(", ");
                            generoTexto.append(nombre);
                        }
                    }
                }
                r.setGenero(generoTexto.toString());

                resultados.add(r);
            }
            return resultados;

        } catch (RestClientException e) {
            // Timeout, sin internet, key inválida, 5xx de TMDB... -> 503 con mensaje claro
            throw new ServicioExternoNoDisponibleException(MSG_NO_DISPONIBLE);
        }
    }

    private int extraerAnio(String fecha) {
        if (fecha.length() < 4) return 0;
        try {
            return Integer.parseInt(fecha.substring(0, 4));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Map<Integer, String> obtenerMapaGeneros() {
        if (generosEnCache != null) {
            return generosEnCache;
        }

        URI url = UriComponentsBuilder.fromUriString(apiUrl + "/genre/movie/list")
                .queryParam("api_key", apiKey)
                .queryParam("language", "es-ES")
                .build(true)
                .toUri();

        JsonNode respuesta = restTemplate.getForObject(url, JsonNode.class);
        Map<Integer, String> mapa = new HashMap<>();

        if (respuesta != null && respuesta.has("genres")) {
            for (JsonNode genero : respuesta.get("genres")) {
                mapa.put(genero.path("id").asInt(), genero.path("name").asText(""));
            }
        }
        if (!mapa.isEmpty()) {
            generosEnCache = mapa;
        }
        return mapa;
    }
}
