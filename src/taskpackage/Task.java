package taskpackage;

import java.util.Objects;

public class Task {
	protected String name;
	protected String description;
	protected Status status;
	protected int id;

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

	@Override
	public String toString() {
		return "TaskPackage.Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + id +
				'}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		Task task = (Task) object;
		System.out.println();
		return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, status);
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
}