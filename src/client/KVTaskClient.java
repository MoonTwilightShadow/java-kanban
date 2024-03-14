package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class KVTaskClient {
    private HttpClient client;
    private String url;
    private String API_TOKEN;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();

        register();
        //Test
        API_TOKEN = "DEBUG";
    }

    private void register() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка при регистрации клиента");
                return;
            }

            API_TOKEN = response.body();
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }
    }

    public void put(String key, String json) {
        //POST /save/<ключ>?API_TOKEN=

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка при добавлении данных на сервер");
                return;
            }
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

    }

    public String load(String key) {
        //GET /load/<ключ>?API_TOKEN=

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        String result = null;

        try {
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Ошибка при загрузке данных с сервера");
                return result;
            }

            result = response.body();
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

        return result;
    }
}
