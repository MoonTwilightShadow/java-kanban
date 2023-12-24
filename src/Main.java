import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		InMemoryTaskManager manager = new InMemoryTaskManager();
		Task t = new Task();

		t.description = "asdasd";
		t.name = "One";
		t.status = TaskStatus.NEW;
		manager.create(t);

		t = new Task();
		t.name = "Two";
		manager.create(t);

		t.status = TaskStatus.DONE;
		manager.update(t);

		SubTask s = new SubTask();

		s.name = "S1";
		s.status = TaskStatus.DONE;
		manager.create(s);

		s = new SubTask();
		s.name = "S2";
		s.status = TaskStatus.IN_PROGRESS;
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
