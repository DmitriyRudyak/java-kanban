package taskpackage;

import java.util.ArrayList;

public class Epic extends Task {
	private final ArrayList<Integer> subTaskIDList = new ArrayList<>();

	public Epic(String name) {
		super(name);
	}

	public Epic(String name, int id) {
		super(name, id);
	}

	public Epic(String name, String description, Status status, int id) {
		super(name, description, status, id);
	}

	@Override
	public String toString() {
		return "Epic{" +
				"subTaskID=" + subTaskIDList +
				", name='" + name + '\'' +
				", status=" + status +
				", ID=" + id +
				'}';
	}

	public ArrayList<Integer> getSubTaskIDList() {
		return subTaskIDList;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.EPIC;
	}
}
