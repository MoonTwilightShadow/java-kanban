package managers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    @Test
    public void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);

        final Task savedTask = manager.getByIdTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void addNewSubTask() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);

        final Task savedTask = manager.getByIdSubTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, SubTask> tasks = manager.getSubTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpicTask() {
        EpicTask task = new EpicTask("Test addNewTask", "Test addNewTask description");
        manager.create(task);

        final Task savedTask = manager.getByIdEpicTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, EpicTask> tasks = manager.getEpicTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void deleteTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);

        manager.deleteByIdTask(1);
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void deleteSubTaskTest() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);
        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);
        manager.create(epic);

        manager.deleteByIdSubTask(1);
        assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    public void deleteEpicTaskTest() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);
        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);
        manager.create(epic);

        manager.deleteByIdEpicTask(2);
        assertEquals(0, manager.getEpicTasks().size());
    }

    @Test
    public void deleteAllTaskTest() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);
        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);
        manager.create(epic);

        manager.deleteByIdEpicTask(2);
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getSubTasks().size());
        assertEquals(0, manager.getEpicTasks().size());
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("Test", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);
        task = new Task("TestTest", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        task.setId(1);
        manager.update(task);

        final Task savedTask = manager.getByIdTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void updateSubTaskTest() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);

        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);
        manager.create(epic);

        task = new SubTask("TestTest", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        task.setId(1);
        task.setEpicId(2);
        manager.update(task);

        final Task savedTask = manager.getByIdSubTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void updateEpicSubTaskTest() {
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 10, 00));
        manager.create(task);
        EpicTask epic = new EpicTask("Test addNewTask", "Test addNewTask description");
        epic.addSubIds(1);
        manager.create(epic);

        epic = new EpicTask("TestTest", "Test addNewTask description");
        epic.addSubIds(1);
        epic.setId(2);
        manager.update(epic);

        final Task savedTask = manager.getByIdEpicTask(2);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

    }
}
