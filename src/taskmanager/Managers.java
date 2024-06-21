package taskmanager;

public class Managers {
	public static InMemoryTaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static FileBackedTaskManager getDefaultFbManager() {
		return new FileBackedTaskManager();
	}
}
