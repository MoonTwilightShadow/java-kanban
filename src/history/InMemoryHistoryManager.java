package history;

import model.Task;

import java.util.*;

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
        if (tasks.get(id) != null) {
            removeNode(tasks.get(id));
            tasks.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private Node linkLast(Task task) {
        final Node t = tail;
        final Node newNode = new Node(task, t, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }

        return newNode;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
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

    private class Node {
        public Task task;
        public Node prev;
        public Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}
