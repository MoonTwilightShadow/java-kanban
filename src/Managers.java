public class Managers {

	public TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public InMemoryHistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}
}
