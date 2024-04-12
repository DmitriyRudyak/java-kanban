package taskmanager;
import taskpackage.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
	Task addTask(Task task);

	ArrayList<Task> taskList();

	void removeTask(int id);

	Task getTask(int id);

	Task updateTask(Task newTask);

	void deleteTaskList();

	Epic addEpic(Epic epic);

	ArrayList<Epic> epicList();

	void removeEpic(int id);

	Task getEpic(int id);

	Task updateEpic(Epic newEpic);

	void deleteEpicList();

	Subtask addSubtask(Subtask subtask);

	ArrayList<Subtask> subtaskList(Epic epic);

	ArrayList<Subtask> subtaskList();

	void removeSubtask(Subtask subtask);

	Subtask getSubtask(int id);

	Subtask updateSubtask(Subtask newSubtask);

	void deleteSubtaskList();

	public List<Task> getHistory();

	void setEpicStatus(Epic epic);
}
