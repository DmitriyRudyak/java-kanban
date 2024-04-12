package taskmanager;
import taskpackage.*;

import java.util.List;

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
}
