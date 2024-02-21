package model;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTaskTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void emptyListOfTask() {
        EpicTask epic = new EpicTask("One", "aaa");

        manager.create(epic);

        assertEquals(TaskStatus.NEW, manager.getByIdEpicTask(1).getStatus());

    }

    @Test
    public void allNewSubTasks() {
        EpicTask epic = new EpicTask("One", "aaa");

        SubTask subTask = new SubTask("Two", "bbb", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 15, 00));
        manager.create(subTask);
        subTask = new SubTask("Three", "ccc", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 16, 00));
        manager.create(subTask);

        epic.addSubIds(1);
        epic.addSubIds(2);

        manager.create(epic);

        assertEquals(TaskStatus.NEW, manager.getByIdEpicTask(3).getStatus());
    }

    @Test
    public void allDoneSubTasks() {
        EpicTask epic = new EpicTask("One", "aaa");

        SubTask subTask = new SubTask("Two", "bbb", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 15, 00));
        manager.create(subTask);
        subTask = new SubTask("Three", "ccc", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 16, 00));
        manager.create(subTask);

        epic.addSubIds(1);
        epic.addSubIds(2);

        manager.create(epic);

        assertEquals(TaskStatus.DONE, manager.getByIdEpicTask(3).getStatus());
    }

    @Test
    public void newAndDoneSubTasks() {
        EpicTask epic = new EpicTask("One", "aaa");

        SubTask subTask = new SubTask("Two", "bbb", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 15, 00));
        manager.create(subTask);
        subTask = new SubTask("Three", "ccc", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 16, 00));
        manager.create(subTask);

        epic.addSubIds(1);
        epic.addSubIds(2);

        manager.create(epic);

        assertEquals(TaskStatus.NEW, manager.getByIdEpicTask(3).getStatus());
    }

    @Test
    public void inProgressSubTasks() {
        EpicTask epic = new EpicTask("One", "aaa");

        SubTask subTask = new SubTask("Two", "bbb", TaskStatus.IN_PROGRESS, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 15, 00));
        manager.create(subTask);
        subTask = new SubTask("Three", "ccc", TaskStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(1, 1, 1, 16, 00));
        manager.create(subTask);

        epic.addSubIds(1);
        epic.addSubIds(2);

        manager.create(epic);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getByIdEpicTask(3).getStatus());
    }


}