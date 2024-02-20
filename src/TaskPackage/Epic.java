package TaskPackage;

import java.util.ArrayList;

public class Epic extends Task {
	private final ArrayList<Integer> subTaskIDList = new ArrayList<>();
	public Epic(String name) {
		super(name);
	}

	public Epic(String name, int ID) {
		super(name, ID);
	}

	@Override
	public String toString() {
		return "Epic{" +
				"subTaskID=" + subTaskIDList +
				", name='" + name + '\'' +
				", status=" + status +
				", ID=" + ID +
				'}';
	}

	public ArrayList<Integer> getSubTaskIDList() {
		return subTaskIDList;
	}
}
