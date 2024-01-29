package model;

public class SubTask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id + "," + TypeOfTasks.SUBTASK + "," + name + "," + status + "," + description + "," + epicId + "\n";
    }
}
