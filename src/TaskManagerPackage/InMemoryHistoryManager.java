package TaskManagerPackage;
import TaskPackage.*;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{
	private final LinkedList<Task> historyList = new LinkedList<>();

	@Override
	public void add(Task task) {
		if (historyList.contains(task)) {
		}
		historyList.addLast(task);
	}

	@Override
	public LinkedList<Task> getHistory() {
		return new LinkedList<>(historyList);
	}

	@Override
	public void remove(int id) {

	}
}
