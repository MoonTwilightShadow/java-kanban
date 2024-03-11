package managers;

import client.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import history.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient kvClient;

    public HttpTaskManager(String url) {
        super("output.csv");

        kvClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        Gson gson = new GsonBuilder().serializeNulls().create();

        kvClient.put("tasks", gson.toJson(tasks));
        kvClient.put("subTasks", gson.toJson(subTasks));
        kvClient.put("epicTasks", gson.toJson(epicTasks));
        kvClient.put("historyManager", historyToString(historyManager));
    }

    public void load() {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Type type = new TypeToken<HashMap<Integer, Task>>(){}.getType();
        tasks = gson.fromJson(kvClient.load("tasks"), type);

        type = new TypeToken<HashMap<Integer, SubTask>>(){}.getType();
        subTasks = gson.fromJson(kvClient.load("subTasks"), type);

        type = new TypeToken<HashMap<Integer, EpicTask>>(){}.getType();
        epicTasks = gson.fromJson(kvClient.load("epicTasks"), type);

        List<Integer> historyIds = historyIdsFromString(kvClient.load("historyManager"));

        for (Integer historyId : historyIds) {
            Task task;

            if (tasks.containsKey(historyId)) {
                task = tasks.get(historyId);
            } else if (subTasks.containsKey(historyId)) {
                task = subTasks.get(historyId);
            } else {
                task = epicTasks.get(historyId);
            }

            historyManager.add(task);
        }

        prioritizedTask.addAll(tasks.values());
        prioritizedTask.addAll(subTasks.values());
    }

}
