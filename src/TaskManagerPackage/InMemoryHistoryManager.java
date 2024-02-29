package TaskManagerPackage;
import TaskPackage.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
	private final List<Task> historyList = new ArrayList<>();

	@Override
	public void add(Task task) {
		if (historyList.size() == 10) {
			historyList.remove(0);
			historyList.add(task);
		} else {
			historyList.add(task);
		}
	}

	@Override
	public List<Task> getHistory() {
		return new ArrayList<>(historyList);
	}
}
