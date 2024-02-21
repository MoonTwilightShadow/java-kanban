package history;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    public InMemoryHistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void getEmptyHistory() {
        List<Task> savedHistory = manager.getHistory();

        assertNotNull(savedHistory, "Истории не существует");
        assertEquals(0, savedHistory.size(), "Размер истории не совпадает");
    }

    @Test
    public void dubclicationOfTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        task.setId(1);
        manager.add(task);
        manager.add(task);

        List<Task> savedHistory = manager.getHistory();

        assertNotNull(savedHistory, "Истории не существует");
        assertEquals(1, savedHistory.size(), "Размер истории не совпадает");
    }

    @Test
    public void deleteFirstElementFromHistory() {
        Task one = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        one.setId(1);
        Task two = new Task("Two", "Task Two", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        two.setId(2);
        Task three = new Task("Three", "Task three", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        three.setId(3);

        manager.add(one);
        manager.add(two);
        manager.add(three);

        manager.remove(1);

        List<Task> expected = List.of(two, three);

        assertEquals(expected, manager.getHistory(), "Списки не совпадают");
    }

    @Test
    public void deleteMiddleElementFromHistory() {
        Task one = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        one.setId(1);
        Task two = new Task("Two", "Task Two", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        two.setId(2);
        Task three = new Task("Three", "Task three", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        three.setId(3);

        manager.add(one);
        manager.add(two);
        manager.add(three);

        manager.remove(2);

        List<Task> expected = List.of(one, three);

        assertEquals(expected, manager.getHistory(), "Списки не совпадают");
    }

    @Test
    public void deleteLastElementFromHistory() {
        Task one = new Task("One", "Task one", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        one.setId(1);
        Task two = new Task("Two", "Task Two", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        two.setId(2);
        Task three = new Task("Three", "Task three", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        three.setId(3);

        manager.add(one);
        manager.add(two);
        manager.add(three);

        manager.remove(3);

        List<Task> expected = List.of(one, two);

        assertEquals(expected, manager.getHistory(), "Списки не совпадают");
    }
}
