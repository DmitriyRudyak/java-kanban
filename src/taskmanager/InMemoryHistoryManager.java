package taskmanager;
import taskpackage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
	private final CustomLinkedList historyList = new CustomLinkedList();

	@Override
	public void add(Task task) {
		historyList.linkLast(task);
	}

	@Override
	public void remove(int id) {
		historyList.removeNode(id);
	}

	@Override
	public List<Task> getHistoryList() {
		return historyList.getTasks();
	}

	private static class CustomLinkedList {		//

		private static class Node<T> {			//класс-узел для создания коллекции
			private T task;
			private Node<T> next;
			private Node<T> prev;

			public Node(Node<T> prev, T task, Node<T> next) {
				this.task = task;
				this.next = next;
				this.prev = prev;
			}
		}

		private Node<Task> head;
		private Node<Task> tail;

		private final Map<Integer, Node<Task>> idToNodeMap = new HashMap<>();	//мапа, хранящая пару: номер id таска - узел

		private void linkLast(Task task) {				//метод добавляет задачу в конец списка и удаляет ее предыдущий просмотр
			if (idToNodeMap.containsKey(task.getId())) {
				removeNode(idToNodeMap.get(task.getId()));
			}
			final Node<Task> oldTail = tail;
			final Node<Task> newNode = new Node<>(oldTail, task, null);
			tail = newNode;
			if (oldTail == null) {
				head = newNode;
			} else {
				oldTail.next = newNode;
			}
			idToNodeMap.put(task.getId(), newNode);
		}

		private void removeNode(int id) {				//метод принимает id задачи на удаление и передает соответствующий узел в сл метод по удалению
			if (idToNodeMap.containsKey(id)) {
				removeNode(idToNodeMap.get(id));
			}
		}

		private void removeNode(Node<Task> node) {		//метод удаляет из мапы узел с уже просмотренной и добавляемой вновь задачей
			final Node<Task> prev = node.prev;
			final Node<Task> next = node.next;
			if (prev == null) {
				head = next;
			} else {
				prev.next = next;
				node.prev = null;
			}
			if (next == null) {
				tail = prev;
			} else {
				next.prev = prev;
				node.next = null;
			}
			node.task = null;
		}

		private List<Task> getTasks() {					//метод формирует список задач для просмотра истории
			List<Task> tasks = new ArrayList<>();
			for (Node<Task> node = head; node != null; node = node.next) {
				tasks.add(node.task);
			}
			return tasks;
		}
	}
}
