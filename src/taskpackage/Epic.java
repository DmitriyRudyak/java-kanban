package taskpackage;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
	private final ArrayList<Integer> subTaskIDList = new ArrayList<>();
	private LocalDateTime endTime;

	public Epic(String name) {
		super(name);
		this.description = "Epic";
	}

	public Epic(String name, int id) {
		super(name, id);
		this.description = "Epic";
	}

	public Epic(String name, String description, Status status, int id) {
		super(name, description, status, id);
	}

	public Epic(String name, String description, Status status, int duration, String startTime) {
		super(name, description, status, duration, startTime);
	}

	public Epic(String name, String description, Status status, int duration, String startTime, int id) {
		super(name, description, status, duration, startTime, id);
	}

	@Override
	public String toString() {
		if (duration != null) {
			return "Epic{" +
					"subTaskID=" + subTaskIDList +
					", name='" + name + '\'' +
					", status=" + status +
					", ID=" + id +
					", startTime=" + startTime +
					", duration=" + duration +
					", endTime=" + endTime +
					'}';
		} else {
			return "Epic{" +
					"subTaskID=" + subTaskIDList +
					", name='" + name + '\'' +
					", status=" + status +
					", ID=" + id +
					'}';

		}
	}

	public ArrayList<Integer> getSubTaskIDList() {
		return subTaskIDList;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.EPIC;
	}

	@Override
	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
}
