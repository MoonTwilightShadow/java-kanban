package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.HttpTaskManager;
import managers.Managers;
import managers.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private final HttpServer server;
    private TaskManager manager;

    Gson gson;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::getPrioritizedTasks);
        server.createContext("/tasks/history", this::getHistory);

        server.createContext("/tasks/task", this::taskProcessing);
        server.createContext("/tasks/subtask", this::subtaskProcessing);
        server.createContext("/tasks/epic", this::epicProcessing);

        server.createContext("/tasks/subtask/epic", this::getEpicSubtask);

        manager = Managers.getDefault();

        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    public void start() {
        server.start();
    }

    private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        try {
            if (GET.equals(exchange.getRequestMethod())) {
                sendText(exchange, gson.toJson(manager.getPrioritizedTask()));
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        try {
            if (GET.equals(exchange.getRequestMethod())) {
                sendText(exchange, gson.toJson(manager.getHistory()));
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void taskProcessing(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getQuery();

            switch (exchange.getRequestMethod()) {
                case GET:
                    if (query == null) {
                        sendText(exchange, gson.toJson(manager.getTasks()));
                    } else {
                        try {
                            sendText(exchange, gson.toJson(manager.getByIdTask(getId(query))));
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                case POST:
                    Optional<String> body = getBody(exchange);

                    if (body.isEmpty()) {
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }

                    Task task = gson.fromJson(body.get(), Task.class);

                    if (task.getId() == null) {
                        manager.create(task);
                    } else {
                        manager.update(task);
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;

                case DELETE:
                    if (query == null) {
                        manager.deleteAllTasks();
                    } else {
                        try {
                            manager.deleteByIdTask(getId(query));
                            exchange.sendResponseHeaders(200, 0);
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void subtaskProcessing(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getQuery();

            switch (exchange.getRequestMethod()) {
                case GET:
                    if (query == null) {
                        sendText(exchange, gson.toJson(manager.getSubTasks()));
                    } else {
                        try {
                            sendText(exchange, gson.toJson(manager.getByIdSubTask(getId(query))));
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                case POST:
                    Optional<String> body = getBody(exchange);

                    if (body.isEmpty()) {
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }

                    SubTask task = gson.fromJson(body.get(), SubTask.class);

                    if (task.getId() == null) {
                        manager.create(task);
                    } else {
                        manager.update(task);
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;

                case DELETE:
                    if (query == null) {
                        manager.deleteAllSubTasks();
                    } else {
                        try {
                            manager.deleteByIdSubTask(getId(query));
                            exchange.sendResponseHeaders(200, 0);
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void epicProcessing(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getQuery();

            switch (exchange.getRequestMethod()) {
                case GET:
                    if (query == null) {
                        sendText(exchange, gson.toJson(manager.getEpicTasks()));
                    } else {
                        try {
                            sendText(exchange, gson.toJson(manager.getByIdEpicTask(getId(query))));
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                case POST:
                    Optional<String> body = getBody(exchange);

                    if (body.isEmpty()) {
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }

                    EpicTask task = gson.fromJson(body.get(), EpicTask.class);

                    if (task.getId() == null) {
                        manager.create(task);
                    } else {
                        manager.update(task);
                    }
                    exchange.sendResponseHeaders(200, 0);
                    break;

                case DELETE:
                    if (query == null) {
                        manager.deleteAllEpicTasks();
                    } else {
                        try {
                            manager.deleteByIdEpicTask(getId(query));
                            exchange.sendResponseHeaders(200, 0);
                        } catch (NumberFormatException exception) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void getEpicSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (GET.equals(exchange.getRequestMethod())) {
            try {
                sendText(exchange, gson.toJson(manager.getEpicSubTasks(getId(query))));
            } catch (NumberFormatException | NullPointerException exception) {
                exchange.sendResponseHeaders(400, 0);
            }
        } else {
            exchange.sendResponseHeaders(405, 0);
        }
    }

    private int getId(String query) throws NumberFormatException {
        String id = query.substring(query.indexOf("=") + 1);
        int taskId = Integer.parseInt(id);

        return taskId;
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void stop() {
        server.stop(0);
    }

    private Optional<String> getBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), DEFAULT_CHARSET);

        if (body.isEmpty())
            return Optional.empty();

        return Optional.of(body);
    }
}
