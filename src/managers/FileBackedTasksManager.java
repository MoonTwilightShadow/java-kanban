package managers;

import history.HistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import exception.ManagerSaveException;
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File fileName;

    public FileBackedTasksManager(String fileName) {
        this.fileName = new File(fileName);
    }

    @Override
    public void create(Task task) {
        super.create(task);
        save();
    }

    @Override
    public void create(SubTask subTask) {
        super.create(subTask);
        save();
    }

    @Override
    public void create(EpicTask epic) {
        super.create(epic);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        save();
    }

    @Override
    public void update(EpicTask epic) {
        super.update(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteByIdTask(int id) {
        super.deleteByIdTask(id);
        save();
    }

    @Override
    public void deleteByIdSubTask(int id) {
        super.deleteByIdSubTask(id);
        save();
    }

    @Override
    public void deleteByIdEpicTask(int id) {
        super.deleteByIdEpicTask(id);
        save();
    }

    @Override
    public Task getByIdTask(int id) {
        Task task = super.getByIdTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getByIdSubTask(int id) {
        SubTask subTask = super.getByIdSubTask(id);
        save();
        return subTask;
    }

    public EpicTask getByIdEpicTask(int id) {
        EpicTask epic = super.getByIdEpicTask(id);
        save();
        return epic;
    }

    private void save() {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                fw.write(task.toString());
            }

            for (EpicTask epic : epicTasks.values()) {
                fw.write(epic.toString());
            }

            for (SubTask subTask : subTasks.values()) {
                fw.write(subTask.toString());
            }

            fw.write("\n" + historyToString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Save Error");
        }
    }

    public static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager manager = new FileBackedTasksManager("output2.csv");
        List<String> lines = null;
        int maxId = 0;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException exception) {
            throw new ManagerSaveException("Load Error");
        }

        for (int i = 1; !lines.get(i).equals(""); i++) {
            String[] parts = lines.get(i).split(",");
            int id = Integer.parseInt(parts[0]);

            if (parts[1].equals("TASK")) {
                manager.tasks.put(id, manager.taskFromString(lines.get(i)));
            } else if (parts[1].equals("SUBTASK")) {
                manager.subTasks.put(id, manager.subTaskFromString(lines.get(i)));
            } else if (parts[1].equals("EPIC")) {
                manager.epicTasks.put(id, manager.epicFromString(lines.get(i)));
            }

            if (id > maxId) {
                maxId = id;
            }
        }
        manager.setNextId(maxId + 1);

        for (SubTask value : manager.subTasks.values()) {
                manager.epicTasks.get(value.getEpicId()).addSubIds(value.getId());
        }

        List<Integer> historyIds = historyIdsFromString(lines.get(lines.size() - 1));

        for (Integer historyId : historyIds) {
            Task task = null;

            if (manager.tasks.containsKey(historyId)) {
                task = manager.tasks.get(historyId);
            } else if (manager.subTasks.containsKey(historyId)) {
                task = manager.subTasks.get(historyId);
            } else {
                task = manager.epicTasks.get(historyId);
            }

            manager.historyManager.add(task);
        }

        return manager;
    }

    private Task taskFromString(String value) {
        Task task = new Task();
        String[] parts = value.split(",");

        task.setId(Integer.parseInt(parts[0]));
        task.setName(parts[2]);
        switch (parts[3]) {
            case "NEW":
                task.setStatus(TaskStatus.NEW);
                break;
            case "IN_PROGRESS":
                task.setStatus(TaskStatus.IN_PROGRESS);
                break;
            case "DONE":
                task.setStatus(TaskStatus.DONE);
        }
        task.setDescription(parts[4]);

        return task;
    }

    private SubTask subTaskFromString(String value) {
        SubTask subTask = new SubTask();
        String[] parts = value.split(",");

        subTask.setId(Integer.parseInt(parts[0]));
        subTask.setName(parts[2]);
        switch (parts[3]) {
            case "NEW":
                subTask.setStatus(TaskStatus.NEW);
                break;
            case "IN_PROGRESS":
                subTask.setStatus(TaskStatus.IN_PROGRESS);
                break;
            case "DONE":
                subTask.setStatus(TaskStatus.DONE);
        }
        subTask.setDescription(parts[4]);
        subTask.setEpicId(Integer.parseInt(parts[5]));

        return subTask;
    }

    private EpicTask epicFromString(String value) {
        EpicTask epic = new EpicTask();
        String[] parts = value.split(",");

        epic.setId(Integer.parseInt(parts[0]));
        epic.setName(parts[2]);
        switch (parts[3]) {
            case "NEW":
                epic.setStatus(TaskStatus.NEW);
                break;
            case "IN_PROGRESS":
                epic.setStatus(TaskStatus.IN_PROGRESS);
                break;
            case "DONE":
                epic.setStatus(TaskStatus.DONE);
        }
        epic.setDescription(parts[4]);

        return epic;
    }

    static List<Integer> historyIdsFromString(String values) {
        List<Integer> ids = new ArrayList<>();
        String[] stringIds = values.split(",");

        for (int i = 0; i < stringIds.length; i++) {
            ids.add(Integer.parseInt(stringIds[i]));
        }

        return ids;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        String[] ids = new String[tasks.size()];

        for (int i = 0; i < ids.length; i++) {
            ids[i] = String.valueOf(tasks.get(i).getId());
        }
        return String.join(",", ids);
    }
}
