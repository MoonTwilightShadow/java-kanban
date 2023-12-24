import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
	private HashMap<Integer, Task> tasks = new HashMap<>();
	private HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
	private HashMap<Integer, SubTask> subTasks = new HashMap<>();
	private int nextId = 1;
	InMemoryHistoryManager historyManager = new Managers().getDefaultHistory();

	@Override
	public void create(Task task) {
		task.id = nextId++;
		tasks.put(task.id, task);
	}

	@Override
	public void create(SubTask subTask) {
		subTask.id = nextId++;
		subTasks.put(subTask.id, subTask);
	}

	@Override
	public void create(EpicTask epic) {
		epic.id = nextId++;

		for (Integer subId : epic.getSubIds()) {
			subTasks.get(subId).setEpicId(epic.id);
		}

		checkStatus(epic);
		epicTasks.put(epic.id, epic);
	}

	@Override
	public void update(Task task) {
		tasks.put(task.id, task);
	}

	@Override
	public void update(SubTask subTask) {
		subTasks.put(subTask.id, subTask);

		checkStatus(epicTasks.get(subTask.getEpicId()));
	}

	@Override
	public void update(EpicTask epic) {
		checkStatus(epic);

		epicTasks.put(epic.id, epic);
	}

	private void checkStatus(EpicTask epic) {
		int sumStatus = 0;
		for (Integer subId : epic.getSubIds()) {
			SubTask sub = subTasks.get(subId);

			if (sub.status == TaskStatus.NEW) {
				sumStatus++;
			} else if (sub.status == TaskStatus.DONE) {
				sumStatus += 2;
			} else {
				sumStatus += 3;
			}
		}

		if (sumStatus == epic.getSubIds().size()) {
			epic.status = TaskStatus.NEW;
		} else if (sumStatus == epic.getSubIds().size() * 2) {
			epic.status = TaskStatus.DONE;
		} else {
			epic.status = TaskStatus.IN_PROGRESS;
		}
	}

	@Override
	public void deleteAllTasks() {
		tasks.clear();
	}

	@Override
	public void deleteAllSubTasks() {
		subTasks.clear();

		for (EpicTask value : epicTasks.values()) {
			value.getSubIds().clear();
			value.status = TaskStatus.NEW;
		}
	}

	@Override
	public void deleteAllEpicTasks() {
		deleteAllSubTasks();
		epicTasks.clear();
	}

	@Override
	public void deleteByIdTask(int id) {
		tasks.remove(id);
	}

	@Override
	public void deleteByIdSubTask(int id) {
		EpicTask epic = epicTasks.get(subTasks.get(id).getEpicId());

		int index = 0;
		for (int i = 0; i < epic.getSubIds().size(); i++) {
			if (epic.getSubIds().get(i) == id) {
				index = i;
				break;
			}
		}

		epic.getSubIds().remove(index);
		subTasks.remove(id);

		checkStatus(epic);
	}

	@Override
	public void deleteByIdEpicTask(int id) {
		EpicTask epic = epicTasks.get(id);

		for (Integer subId : epic.getSubIds()) {
			subTasks.remove(subId);
		}

		epicTasks.remove(id);
	}

	@Override
	public Task getByIdTask(int id) {
		historyManager.add(tasks.get(id));
		return tasks.get(id);
	}

	@Override
	public SubTask getByIdSubTask(int id) {
		historyManager.add(subTasks.get(id));
		return subTasks.get(id);
	}

	@Override
	public EpicTask getByIdEpicTask(int id) {
		historyManager.add(epicTasks.get(id));
		return epicTasks.get(id);
	}

	@Override
	public HashMap<Integer, Task> getTasks() {
		return tasks;
	}

	@Override
	public HashMap<Integer, SubTask> getSubTasks() {
		return subTasks;
	}

	@Override
	public HashMap<Integer, EpicTask> getEpicTasks() {
		return epicTasks;
	}

	@Override
	public ArrayList<SubTask> getEpicSubTasks(int epicId) {
		EpicTask epic = epicTasks.get(epicId);
		ArrayList<SubTask> sub = new ArrayList<>();

		for (Integer subId : epic.getSubIds()) {
			sub.add(subTasks.get(subId));
		}

		return sub;
	}

	public List<Task> getHistory() {
		return historyManager.getHistory();
	}
}
