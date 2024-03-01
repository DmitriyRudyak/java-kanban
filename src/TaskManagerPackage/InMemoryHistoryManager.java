package TaskManagerPackage;
import TaskPackage.*;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{
	private final LinkedList<Task> historyList = new LinkedList<>();

	@Override
	public void add(Task task) {
		if (historyList.size() == HISTORY_LIST_SIZE) {
			historyList.removeFirst();
		}
		historyList.addLast(task);
	}

	@Override
	public LinkedList<Task> getHistory() {
		return new LinkedList<>(historyList);
	}
}
