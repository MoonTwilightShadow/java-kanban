package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, TaskStatus status, Duration durarion, LocalDateTime startTime) {
        super(name, description, status, durarion, startTime);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id + "," + TypeOfTasks.SUBTASK + "," + name + "," + status + "," + description + "," + durarion + "," +
                startTime + "," + epicId + "\n";
    }
}
