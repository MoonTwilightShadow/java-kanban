package history;

import model.Task;

import java.util.LinkedList;

public interface HistoryManager {
	public void add(Task task);

	public LinkedList<Task> getHistory();
}
