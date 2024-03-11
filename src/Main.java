import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.FileBackedTasksManager;
import managers.HttpTaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();

        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");

    /*    Task t = new Task("1", "111", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(t);
        t = new Task("2", "222", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 11, 00));
        manager.create(t);

        HttpTaskManager newManager = new HttpTaskManager("http://localhost:8078");
        newManager.load();
     */

//        HttpTaskServer server = new HttpTaskServer();
//        server.start();




//        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Task task = new Task("Test 1", "Test description 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 0));
        manager.create(task);
        task = new Task("Test 2", "Test description 2", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 11, 0));
        manager.create(task);


        System.out.println(manager.getByIdTask(1));
        System.out.println(manager.getByIdTask(2));

    }
}
