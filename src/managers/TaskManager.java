package managers;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    public void create(Task task);

    public void create(SubTask subTask);

    public void create(EpicTask epic);

    public void update(Task task);

    public void update(SubTask subTask);

    public void update(EpicTask epic);

    public void deleteAllTasks();

    public void deleteAllSubTasks();

    public void deleteAllEpicTasks();

    public void deleteByIdTask(int id);

    public void deleteByIdSubTask(int id);

    public void deleteByIdEpicTask(int id);

    public Task getByIdTask(int id);

    public SubTask getByIdSubTask(int id);

    public EpicTask getByIdEpicTask(int id);

    public HashMap<Integer, Task> getTasks();

    public HashMap<Integer, SubTask> getSubTasks();

    public HashMap<Integer, EpicTask> getEpicTasks();

    public ArrayList<SubTask> getEpicSubTasks(int epicId);
}
