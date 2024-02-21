package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;

    protected int id;
    protected TaskStatus status;
    protected Duration durarion;
    protected LocalDateTime startTime;

    public Task(String name, String description, TaskStatus status, Duration durarion, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.durarion = durarion;
        this.startTime = startTime;
    }

    public Duration getDurarion() {
        return durarion;
    }

    public void setDurarion(Duration durarion) {
        this.durarion = durarion;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(durarion);
    }

    @Override
    public String toString() {
        return id + "," + TypeOfTasks.TASK + "," + name + "," + status + "," + description + "," + durarion + "," +
                startTime + "\n";
    }
}
