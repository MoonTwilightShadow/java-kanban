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

	@Override
	public String toString() {
		return "model.EpicTask{" +
				"subIds=" + subIds +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", status='" + status + '\'' +
				'}';
	}
}
