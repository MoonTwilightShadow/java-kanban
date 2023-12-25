package managers;

import model.HistoryManager;
import model.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
	private LinkedList<Task> taskHistory = new LinkedList<>();

	@Override
	public void add(Task task) {
		if (taskHistory.size() > 10) {
			taskHistory.remove(0);
		}

		taskHistory.add(task);
	}

	@Override
	public LinkedList<Task> getHistory() {
		return taskHistory;
	}
}
