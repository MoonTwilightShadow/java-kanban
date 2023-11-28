import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
	private HashMap<Integer, Task> tasks = new HashMap<>();
	private HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
	private HashMap<Integer, SubTask> subTasks = new HashMap<>();
	private int nextId = 1;

	public void create(Task task) {
		task.id = nextId++;
		tasks.put(task.id, task);
	}

	public void create(SubTask subTask) {
		subTask.id = nextId++;
		subTasks.put(subTask.id, subTask);
	}

	public void create(EpicTask epic) {
		epic.id = nextId++;

		for (Integer subId : epic.subIds) {
			subTasks.get(subId).epicId = epic.id;
		}

		checkStatus(epic);
		epicTasks.put(epic.id, epic);
	}

	public void update(Task task) {
		tasks.put(task.id, task);
	}

	public void update(SubTask subTask) {
		subTasks.put(subTask.id, subTask);

		checkStatus(epicTasks.get(subTask.epicId));
	}

	public void update(EpicTask epic) {
		checkStatus(epic);

		epicTasks.put(epic.id, epic);
	}

	private void checkStatus(EpicTask epic) {
		int sumStatus = 0;
		for (Integer subId : epic.subIds) {
			SubTask sub = subTasks.get(subId);

			if (sub.status.equals("NEW")) {
				sumStatus++;
			} else if (sub.status.equals("DONE")) {
				sumStatus += 2;
			} else {
				sumStatus += 3;
			}
		}

		if (sumStatus == epic.subIds.size()) {
			epic.status = "NEW";
		} else if (sumStatus == epic.subIds.size() * 2) {
			epic.status = "DONE";
		} else {
			epic.status = "IN_PROGRESS";
		}
	}

	public void deleteAllTasks() {
		tasks.clear();
	}

	public void deleteAllSubTasks() {
		subTasks.clear();

		for (EpicTask value : epicTasks.values()) {
			value.subIds.clear();
			value.status = "NEW";
		}
	}

	public void deleteAllEpicTasks() {
		deleteAllSubTasks();
		epicTasks.clear();
	}

	public void deleteByIdTask(int id) {
		tasks.remove(id);
	}

	public void deleteByIdSubTask(int id) {
		EpicTask epic = epicTasks.get(subTasks.get(id).epicId);

		int index = 0;
		for (int i = 0; i < epic.subIds.size(); i++) {
			if (epic.subIds.get(i) == id) {
				index = i;
				break;
			}
		}

		epic.subIds.remove(index);
		subTasks.remove(id);

		checkStatus(epic);
	}

	public void deleteByIdEpicTask(int id) {
		EpicTask epic = epicTasks.get(id);

		for (Integer subId : epic.subIds) {
			subTasks.remove(subId);
		}

		epicTasks.remove(id);
	}

	public Task getByIdTask(int id) {
		return tasks.get(id);
	}

	public SubTask getByIdSubTask(int id) {
		return subTasks.get(id);
	}

	public EpicTask getByIdEpicTask(int id) {
		return epicTasks.get(id);
	}

	public HashMap<Integer, Task> getTasks() {
		return tasks;
	}

	public HashMap<Integer, SubTask> getSubTasks() {
		return subTasks;
	}

	public HashMap<Integer, EpicTask> getEpicTasks() {
		return epicTasks;
	}

	public ArrayList<SubTask> getEpicSubTasks(int epicId) {
		EpicTask epic = epicTasks.get(epicId);
		ArrayList<SubTask> sub = new ArrayList<>();

		for (Integer subId : epic.subIds) {
			sub.add(subTasks.get(subId));
		}

		return sub;
	}
}
