package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        InMemoryHistoryManager history = new InMemoryHistoryManager();

        Task task = new Task("Test 1", "Test description 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 0));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        task.setId(1);
        history.add(task);

        task = new Task("Test 2", "Test description 2", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 11, 0));
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        task.setId(2);
        history.add(task);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task?id=2"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String stringHistory = gson.toJson(history.getHistory());
        assertEquals(stringHistory, response.body(), "Error");
    }

    @Test
    public void taskProcessingTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 0));

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body(), "Нет таска");

        task.setId(1);
        String strTask = gson.toJson(task);

        assertEquals(strTask, response.body(), "Таски отличаются");

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HashMap<Integer, Task> tasks = new HashMap<>();
        tasks.put(1, task);

        String strHash = gson.toJson(tasks);

        assertEquals(strHash, response.body(), "Мапы не совпадают");

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("{}", response.body(), "Not Empty");
    }

    @Test
    public void subtaskProcessingTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=1"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body(), "Нет таска");

        task.setId(1);
        String strTask = gson.toJson(task);

        assertEquals(strTask, response.body(), "Таски отличаются");

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HashMap<Integer, SubTask> tasks = new HashMap<>();
        tasks.put(1, task);

        String strHash = gson.toJson(tasks);

        assertEquals(strHash, response.body(), "Мапы не совпадают");

        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=1"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("{}", response.body(), "Not Empty");
    }

    @Test
    public void epicProcessingTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=1"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response, "Epic is empty");

        epic.setId(1);
        String stringEpic = gson.toJson(epic);
        assertEquals(stringEpic, response.body(), "Epics not equals");

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());


        HashMap<Integer, EpicTask> epics = new HashMap<>();
        epics.put(1, epic);

        String stringEpics = gson.toJson(epics);
        assertEquals(stringEpics, response.body(), "HashMap is no equals");

        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=1"))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("{}", response.body(), "Epics is not empty");
    }
}
