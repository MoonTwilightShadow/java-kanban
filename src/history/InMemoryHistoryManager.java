package history;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
	private Node head;
	private Node tail;
	private Map<Integer, Node> tasks = new HashMap<>();

	@Override
	public void add(Task task) {
		if (tasks.get(task.getId()) != null) {
			removeNode(tasks.get(task.getId()));
			tasks.remove(task.getId());
		}

		tasks.put(task.getId(), linkLast(task));
	}

	@Override
	public void remove(int id) {
		removeNode(tasks.get(id));
		tasks.remove(id);
	}

	@Override
	public List<Task> getHistory() {
		return getTask();
	}

	private Node linkLast(Task task) {
		final Node t = tail;
		final Node newNode = new Node(task, t, null);
		tail = newNode;
		if (t == null)
			head = newNode;
		else
			t.next = newNode;

		return newNode;
	}

	private void removeNode(Node node) {
		Node prev = node.prev;
		Node next = node.next;

		if (prev == null) {
			head.next = next;
		} else if (next == null) {
			tail = prev;
			tail.next = null;
		} else {
			prev.next = next;
			next.prev = prev;
		}
	}

	private List<Task> getTask() {
		List<Task> tasks = new ArrayList<>();

		Node node = head;
		while (node != null) {
			tasks.add(node.task);
			node = node.next;
		}

		return tasks;
	}
}
