public class Task {
	protected String name;
	protected String description;
	protected int id;
	protected String status;

	@Override
	public String toString() {
		return "Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", status='" + status + '\'' +
				'}';
	}
}
