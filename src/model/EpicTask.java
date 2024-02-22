package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {
    private ArrayList<Integer> subIds;
    private LocalDateTime endTime;

    public EpicTask(String name, String description) {
        super(name, description, TaskStatus.NEW, null, null);
        subIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    public void setSubIds(ArrayList<Integer> subIds) {
        this.subIds = subIds;
    }

    public void addSubIds(int id) {
        subIds.add(id);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return id + "," + TypeOfTasks.EPIC + "," + name + "," + status + "," + description + "," + durarion + "," +
                startTime + "\n";
    }
}
