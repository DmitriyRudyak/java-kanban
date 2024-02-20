package TaskPackage;

import java.util.Objects;

public class Task {
	 protected String name;
	 protected String description;
	 protected Status status;
	 protected int ID;

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

	public Task(String name, String description, Status status, int ID) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.ID = ID;
	}

	public Task(String name) {
		this.name = name;
	}

	public Task(String name, int ID) {
		this.name = name;
		this.ID = ID;
	}

	@Override
	public String toString() {
		return "TaskPackage.Task{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				", ID=" + ID +
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

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
}