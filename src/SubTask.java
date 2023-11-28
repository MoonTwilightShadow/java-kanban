public class SubTask extends Task {
	protected int epicId;

	@Override
	public String toString() {
		return "SubTask{" +
				"epicId=" + epicId +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", status='" + status + '\'' +
				'}';
	}
}
