package managers;

import history.InMemoryHistoryManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int nextId = 1;
    protected InMemoryHistoryManager historyManager = Managers.getDefaultHistory();
    private Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() == null && o2.getStartTime() == null)
                return 0;

            if (o1.getStartTime() == null)
                return 1;

            if (o2.getStartTime() == null)
                return -1;

            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };
    protected TreeSet<Task> prioritizedTask = new TreeSet<>(comparator);

    protected void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public int getNextId() {
        return nextId;
    }

    @Override
    public void create(Task task) {
        if (validate(task)) {
            task.setId(nextId++);
            tasks.put(task.getId(), task);

            prioritizedTask.add(task);
        } else {
            System.out.println("Данное задание пересекается с уже созданными, выберите другое время");
        }
    }

    @Override
    public void create(SubTask subTask) {
        if (validate(subTask)) {
            subTask.setId(nextId++);
            subTasks.put(subTask.getId(), subTask);

            prioritizedTask.add(subTask);
        } else {
            System.out.println("Данное задание пересекается с уже созданными, выберите другое время");
        }
    }

    @Override
    public void create(EpicTask epic) {
        epic.setId(nextId++);

        for (Integer subId : epic.getSubIds()) {
            subTasks.get(subId).setEpicId(epic.getId());
        }

        checkStatus(epic);
        calculateTime(epic);

        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void update(Task task) {
        prioritizedTask.remove(tasks.get(task.getId()));

        if (validate(task)) {
            tasks.put(task.getId(), task);
            prioritizedTask.add(task);
        } else {
            prioritizedTask.add(tasks.get(task.getId()));
            System.out.println("Данное задание пересекается с уже созданными, выберите другое время");
        }
    }

    @Override
    public void update(SubTask subTask) {
        prioritizedTask.remove(subTasks.get(subTask.getId()));

        if (validate(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            checkStatus(epicTasks.get(subTask.getEpicId()));
            calculateTime(epicTasks.get(subTask.getEpicId()));
            prioritizedTask.add(subTask);
        } else {
            prioritizedTask.add(subTasks.get(subTask.getId()));
            System.out.println("Данное задание пересекается с уже созданными, выберите другое время");
        }
    }

    @Override
    public void update(EpicTask epic) {
        checkStatus(epic);
        calculateTime(epic);

        epicTasks.put(epic.getId(), epic);
    }

    private boolean validate(Task task) {
        if (task.getStartTime() == null)
            return true;

        for (Task currentTask : prioritizedTask) {
            if (currentTask.getStartTime() != null &&
                    !(task.getStartTime().plus(task.getDurarion()).isBefore(currentTask.getStartTime()) ||
                            task.getStartTime().isAfter(currentTask.getEndTime()))) {
                return false;
            }
        }

        return true;
    }

    private void checkStatus(EpicTask epic) {
        int countDone = 0;
        int countProgress = 0;

        for (Integer subId : epic.getSubIds()) {
            SubTask sub = subTasks.get(subId);

            if (sub.getStatus() == TaskStatus.DONE) {
                countDone++;
            } else if (sub.getStatus() == TaskStatus.IN_PROGRESS) {
                countProgress++;
            }
        }

        if (countDone == epic.getSubIds().size() && !epic.getSubIds().isEmpty()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (countProgress != 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    protected void calculateTime(EpicTask epic) {
        epic.setStartTime(calculateStartTime(epic));
        epic.setDurarion(calculateDuration(epic));
        epic.setEndTime(calculateEndTime(epic));
    }

    private Duration calculateDuration(EpicTask epic) {
        return epic.getSubIds().stream()
                .map(id -> subTasks.get(id))
                .map(Task::getDurarion)
                .filter(Objects::nonNull)
                .reduce(Duration::plus)
                .orElse(null);
    }

    private LocalDateTime calculateStartTime(EpicTask epic) {
        return epic.getSubIds().stream()
                .map(id -> subTasks.get(id))
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private LocalDateTime calculateEndTime(EpicTask epic) {
        Optional<SubTask> sub = epic.getSubIds().stream()
                .map(id -> subTasks.get(id))
                .filter(o -> o.getStartTime() != null)
                .max(Comparator.comparing(Task::getStartTime));

        if (sub.isPresent()) {
            SubTask subTask = sub.get();

            if (subTask.getStartTime() != null && subTask.getDurarion() != null)
                return subTask.getStartTime().plus(subTask.getDurarion());
        }

        return null;
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
            value.setStatus(TaskStatus.NEW);
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
        historyManager.remove(id);
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
        historyManager.remove(id);

        checkStatus(epic);
    }

    @Override
    public void deleteByIdEpicTask(int id) {
        EpicTask epic = epicTasks.get(id);

        for (Integer subId : epic.getSubIds()) {
            subTasks.remove(subId);
        }

        epicTasks.remove(id);
        historyManager.remove(id);
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

    public TreeSet<Task> getPrioritizedTask() {
        return prioritizedTask;
    }
}