package taskmanager;
import taskpackage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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

	Epic getEpic(int id);

	Epic updateEpic(Epic newEpic);

	void deleteEpicList();

	Subtask addSubtask(Subtask subtask);

	ArrayList<Subtask> subtaskList(int id);

	ArrayList<Subtask> subtaskList();

	void removeSubtask(int id);

	Subtask getSubtask(int id);

	Subtask updateSubtask(Subtask newSubtask);

	void deleteSubtaskList();

	List<Task> getHistory();

	void setEpicStatus(Epic epic);

	void setEpicClocks(Epic epic);

	TreeSet<Task> getPrioritizedTasks();

	boolean checkTaskCrossing(Task task);
}
