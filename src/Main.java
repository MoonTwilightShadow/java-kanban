import managers.FileBackedTasksManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("out.csv");

        Task t = new Task("1", "111", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(t);
        t = new Task("2", "222", TaskStatus.NEW, Duration.ofMinutes(15), null /*LocalDateTime.of(1, 1, 1, 11, 00)*/);
        manager.create(t);

        System.out.println(t.equals(manager.getByIdTask(2)));

        SubTask sub = new SubTask("3", "333", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 12, 00));
        manager.create(sub);
        sub = new SubTask("4", "444", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 12, 30));
        manager.create(sub);

        EpicTask epic = new EpicTask("5", "555");
        epic.addSubIds(3);
        epic.addSubIds(4);
        manager.create(epic);

        manager.getByIdEpicTask(5);
        manager.getByIdTask(1);
        manager.getByIdTask(2);
        manager.getByIdSubTask(3);
        manager.getByIdTask(2);

        t = new Task("6", "666", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 15, 00));
        manager.create(t);
        t = new Task("7", "777", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 16, 00));
        manager.create(t);


        manager.deleteByIdTask(7);

        //System.out.println(manager.getPrioritizedTask());

        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(Path.of("out.csv"));
        newManager.getByIdEpicTask(5);
        newManager.getByIdTask(1);
        newManager.getByIdTask(2);
        newManager.getByIdSubTask(3);

        //System.out.println(newManager.getPrioritizedTask());


    }
}
