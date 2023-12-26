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
		return "model.SubTask{" +
				"epicId=" + epicId +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", status='" + status + '\'' +
				'}';
	}
}
