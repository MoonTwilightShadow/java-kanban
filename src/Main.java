import managers.InMemoryTaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		InMemoryTaskManager manager = new InMemoryTaskManager();
		Task t = new Task();

		t.setDescription("asdasd");
		t.setName("One");
		t.setStatus(TaskStatus.NEW);
		manager.create(t);

		t = new Task();
		t.setName("Two");
		manager.create(t);

		t.setStatus(TaskStatus.DONE);
		manager.update(t);

		SubTask s = new SubTask();

		s.setName("S1");
		s.setStatus(TaskStatus.DONE);
		manager.create(s);

		s = new SubTask();
		s.setName("S2");
		s.setStatus(TaskStatus.IN_PROGRESS);
		manager.create(s);

		EpicTask e = new EpicTask();
		e.setSubIds(new ArrayList<Integer>(Arrays.asList(3, 4)));
		manager.create(e);

		manager.getByIdTask(1);
		manager.getByIdTask(2);
		manager.getByIdSubTask(3);
		manager.getByIdSubTask(4);
		manager.getByIdEpicTask(5);
		manager.getByIdTask(2);
		manager.getByIdTask(2);
		manager.getByIdTask(2);
		manager.getByIdTask(2);
		manager.getByIdTask(2);
		manager.getByIdTask(2);

		System.out.println(manager.getTasks());
		System.out.println(manager.getSubTasks());
		System.out.println(manager.getEpicTasks());
		System.out.println(manager.getHistory());
	}
}
