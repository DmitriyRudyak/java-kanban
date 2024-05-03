package taskpackage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
	protected String name;
	protected String description;
	protected Status status;
	protected int id;
	protected Duration duration;
	protected LocalDateTime startTime;

	public Task(String name, String description) {
		this.name = name;
		this.description = description;
		this.status = Status.NEW;
	}

	public Task(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public Task(String name, String description, Status status, int id) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.id = id;
	}

	public Task(String name) {
		this.name = name;
	}

	public Task(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public Task(String name, String description, Status status, int duration, String startTime) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.duration = Duration.ofMinutes(duration);
		this.startTime = LocalDateTime.parse(startTime);
	}

	public Task(String name, String description, Status status, int duration, String startTime, int id) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.id = id;
		this.duration = Duration.ofMinutes(duration);
		this.startTime = LocalDateTime.parse(startTime);
	}

	@Override
	public String toString() {
		if (duration == null) {
			return "TaskPackage.Task{" +
					"name='" + name + '\'' +
					", description='" + description + '\'' +
					", status=" + status +
					", ID=" + id +
					'}';
		} else {
			return "Task{" +
					"name='" + name + '\'' +
					", description='" + description + '\'' +
					", status=" + status +
					", id=" + id +
					", duration=" + duration.toMinutes() +
					", startTime=" + startTime +
					'}';
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status, id, duration, startTime);
	}

	public LocalDateTime getEndTime() {
		return startTime.plus(duration);
	}

	public boolean hasClocks() {
		return duration == null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TaskType getTaskType() {
		return TaskType.TASK;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public int compareTo(Task task) {
		if (this.startTime.isBefore(task.startTime)) {
			return -1;
		} else if (this.startTime.isEqual(task.startTime)) {
			return 0;
		} else return 1;
	}
}