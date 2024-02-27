package TaskManager;

import TaskPackage.Epic;
import TaskPackage.Subtask;
import TaskPackage.Task;

import java.util.ArrayList;

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

	Subtask getSubtask(int ID);

	Subtask updateSubtask(Subtask newSubtask);

	void deleteSubtaskList();

	void setEpicStatus(Epic epic);
}
