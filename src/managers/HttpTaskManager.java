package managers;

import client.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import history.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        kvClient.put("id", String.valueOf(getNextId()));
    }

    public void load() {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        String response;
        Type type = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        response = kvClient.load("tasks");
        if (response != null)
            tasks = gson.fromJson(response, type);

        type = new TypeToken<HashMap<Integer, SubTask>>() {}.getType();
        response = kvClient.load("subTasks");
        if (response != null)
            subTasks = gson.fromJson(response, type);

        type = new TypeToken<HashMap<Integer, EpicTask>>() {}.getType();
        response = kvClient.load("epicTasks");
        if (response != null)
            epicTasks = gson.fromJson(response, type);

        response = kvClient.load("historyManager");
        List<Integer> historyIds = new ArrayList<>();
        if (response != null)
             historyIds = historyIdsFromString(response);

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

        response = kvClient.load("id");
        if (response != null)
            setNextId(Integer.parseInt(response));

        prioritizedTask.addAll(tasks.values());
        prioritizedTask.addAll(subTasks.values());

    }

}
