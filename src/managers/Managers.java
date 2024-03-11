package managers;

import history.InMemoryHistoryManager;

public class Managers {
	public static TaskManager getDefault() {
		return new HttpTaskManager("http://localhost:8078");
	}

	public static InMemoryHistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}
}
