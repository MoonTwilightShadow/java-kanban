import java.util.ArrayList;

public class EpicTask extends Task {
	protected ArrayList<Integer> subIds = new ArrayList<>();

	@Override
	public String toString() {
		return "EpicTask{" +
				"subIds=" + subIds +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				", status='" + status + '\'' +
				'}';
	}
}
