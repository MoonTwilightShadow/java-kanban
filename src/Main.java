public class Main {

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		Task t = new Task();

		t.description = "asdasd";
		t.name = "One";
		t.status = "NEW";
		manager.create(t);

		t = new Task();
		t.name = "Two";
		manager.create(t);

		t.status = "DONE";
		manager.update(t);

		SubTask s = new SubTask();

		s.name = "S1";
		s.status = "DONE";
		manager.create(s);

		s = new SubTask();
		s.name = "S2";
		s.status = "IN_PROGRESS";
		manager.create(s);

		EpicTask e = new EpicTask();
		e.subIds.add(3);
		e.subIds.add(4);
		manager.create(e);

		System.out.println(manager.getTasks());
		System.out.println(manager.getSubTasks());
		System.out.println(manager.getEpicTasks());

	}
}
