import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "8d7480f7-73f2-4aee-847e-4b58e69b5d0f";
        double lat = 50.29;
        double lon = 127.53;
        int limit = 5;

        String url = "https://api.weather.yandex.ru/v2/forecast?lat=" + lat +
                "&lon=" + lon +
                "&limit=" + limit +
                "&lang=ru_RU";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Yandex-Weather-Key", apiKey)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("JSON-ответ:");
        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        int temp = root.get("fact").get("temp").asInt();
        System.out.println("Текущая температура (fact.temp): " + temp + "°C");

        JsonNode forecasts = root.get("forecasts");
        double sum = 0;
        int count = 0;

        for (JsonNode forecast : forecasts) {
            double tempAvg = forecast.get("parts").get("day").get("temp_avg").asDouble();
            sum += tempAvg;
            count++;
        }

        double avgTemp = sum / count;
        System.out.printf("Средняя температура за %d суток: %.1f°C%n", count, avgTemp);

    }
}
