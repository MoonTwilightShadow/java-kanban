package model;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subIds = new ArrayList<>();

    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    public void setSubIds(ArrayList<Integer> subIds) {
        this.subIds = subIds;
    }

    public void addSubIds(int id) {
        subIds.add(id);
    }

    @Override
    public String toString() {
        return id + "," + TypeOfTasks.EPIC + "," + name + "," + status + "," + description + "\n";
    }
}
