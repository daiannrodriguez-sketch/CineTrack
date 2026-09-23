package co.edu.sena.cinetrack.service;

import co.edu.sena.cinetrack.dto.TmdbResultado;
import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TmdbService {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TmdbResultado> buscarPeliculas(String titulo) {
        // Primero obtenemos el diccionario de géneros (id -> nombre)
        Map<Integer, String> mapaGeneros = obtenerMapaGeneros();

        String url = apiUrl + "/search/movie?api_key=" + apiKey
                + "&language=es-ES&query=" + titulo;

        JsonNode respuesta = restTemplate.getForObject(url, JsonNode.class);
        List<TmdbResultado> resultados = new ArrayList<>();

        if (respuesta == null || !respuesta.has("results")) {
            return resultados;
        }

        for (JsonNode item : respuesta.get("results")) {
            TmdbResultado r = new TmdbResultado();
            r.setTitulo(item.path("title").asText(""));
            r.setSinopsis(item.path("overview").asText(""));

            String fecha = item.path("release_date").asText("");
            r.setAnio(fecha.length() >= 4 ? Integer.parseInt(fecha.substring(0, 4)) : 0);

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
    }

    private Map<Integer, String> obtenerMapaGeneros() {
        String url = apiUrl + "/genre/movie/list?api_key=" + apiKey + "&language=es-ES";
        JsonNode respuesta = restTemplate.getForObject(url, JsonNode.class);
        Map<Integer, String> mapa = new HashMap<>();

        if (respuesta != null && respuesta.has("genres")) {
            for (JsonNode genero : respuesta.get("genres")) {
                mapa.put(genero.path("id").asInt(), genero.path("name").asText(""));
            }
        }
        return mapa;
    }
}