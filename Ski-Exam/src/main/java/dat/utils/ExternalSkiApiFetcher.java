package dat.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dtos.InstructionDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ExternalSkiApiFetcher {

    private static final String BASE_URL = "https://apiprovider.cphbusinessapps.dk/skilesson/";

    public static List<InstructionDTO> getInstructionsByLevel(String level) {
        try {
            String url = BASE_URL + level.toLowerCase();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new Utils().getObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch instructions from external API", e);
        }
    }
}
