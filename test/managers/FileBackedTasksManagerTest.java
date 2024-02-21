package managers;

import model.EpicTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("outTest.csv");
    }

    @Test
    public void emptyListOfTasks() {
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(Path.of("./test/resources/empty.csv"));
        Assertions.assertNotNull(newManager, "Менеджер не существует");

        Task one = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        newManager.create(one);
    }

    @Test
    public void epicTaskWithoutSubTasks() {
        EpicTask epic = new EpicTask("One", "Epic One");
        manager.create(epic);

        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(Path.of("outTest.csv"));
        Assertions.assertNotNull(newManager, "Менеджер не существует");

        epic = new EpicTask("Two", "Epic Two");
        newManager.create(epic);
    }

    @Test
    public void emptyListOfHistory() {
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(Path.of("outTest.csv"));
        Assertions.assertNotNull(newManager);

        Assertions.assertEquals(0, newManager.getHistory().size(), "Размеры истории не свопадают");
    }
}
