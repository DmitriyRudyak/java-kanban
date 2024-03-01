package TaskManagerPackage;
import TaskPackage.*;

import java.util.LinkedList;

public interface HistoryManager {
	static final int HISTORY_LIST_SIZE = 10;
	void add(Task task);
	LinkedList<Task> getHistory();
}
