package taskpackage;

public class Subtask extends Task {
	private int epicID;

	public Subtask(String name, String description, int epicID) {
		super(name, description);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int epicID) {
		super(name, description, status);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int epicID, int id) {
		super(name, description, status, id);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int duration, String startTime, int epicID) {
		super(name, description, status, duration, startTime);
		this.epicID = epicID;
	}

	public Subtask(String name, String description, Status status, int duration, String startTime, int epicID, int id) {
		super(name, description, status, duration, startTime, id);
		this.epicID = epicID;
	}

	@Override
	public String toString() {
		if (duration == null) {
			return "Subtask{" +
					"epicID=" + epicID +
					", name='" + name + '\'' +
					", description='" + description + '\'' +
					", status=" + status +
					", ID=" + id +
					'}';
		} else {
			return "Subtask{" +
					"epicID=" + epicID +
					", name='" + name + '\'' +
					", description='" + description + '\'' +
					", status=" + status +
					", id=" + id +
					", duration=" + duration.toMinutes() +
					", startTime=" + startTime +
					'}';
		}
	}

	public int getEpicID() {
		return epicID;
	}

	public void setEpicID(int epicID) {
		this.epicID = epicID;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.SUB_TASK;
	}
}
