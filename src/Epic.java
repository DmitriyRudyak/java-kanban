import java.util.HashMap;

public class Epic extends Task {
	ID_Generator generator = new ID_Generator();
	HashMap<Integer, Subtask> subTaskMap;


	public Epic(String name) {
		super(name);
		subTaskMap = new HashMap<>();
		super.status = Status.NEW;
	}

	public void addSubtask(Subtask subtask) {
		subtask.ID = generator.generateID();
		subTaskMap.put(subtask.ID, subtask);
	}

	public void setEpicStatus() {
		int NEW = 0;
		int IN_PROGRESS = 0;
		int DONE = 0;
		for (Subtask subtask : subTaskMap.values()) {
			switch (subtask.status) {
				case NEW: NEW++;
				break;
				case IN_PROGRESS: IN_PROGRESS++;
				break;
				case DONE: DONE++;
				break;
			}
		}
		if (subTaskMap.isEmpty()) {
			status = Status.NEW;
		} else if (IN_PROGRESS == 0 && DONE == 0) {
			status = Status.NEW;
		} else if (IN_PROGRESS == 0 && NEW == 0) {
			status = Status.DONE;
		} else {
			status = Status.IN_PROGRESS;
		}
	}

	@Override
	public String toString() {
		return "Epic{" +
				"name='" + name + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}
}
