package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import server.KVServerTest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest {
    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        manager = new HttpTaskManager("http://localhost:8078");
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void loadTest() throws IOException {
        new KVServerTest().start();

        HttpTaskManager manager1 = new HttpTaskManager("http://localhost:5000");
        Task one = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 0));
        manager1.create(one);

        Task two = new Task("two", "Task two", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 11, 0));
        manager1.create(two);

        Task three = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 12, 0));
        manager1.create(three);

        manager1.getByIdTask(3);
        manager1.getByIdTask(2);
        manager1.getByIdTask(1);

        HttpTaskManager manager2 = new HttpTaskManager("http://localhost:5000");
        manager2.load();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Assertions.assertEquals(gson.toJson(manager1.getTasks()), gson.toJson(manager2.getTasks()), "Tasks is not equals");
        Assertions.assertEquals(gson.toJson(manager1.getHistory()), gson.toJson(manager2.getHistory()), "History is not equals");

    }
}
