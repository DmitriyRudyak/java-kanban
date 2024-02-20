package TaskManager;
import TaskPackage.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
	private final HashMap<Integer, Task> taskMap = new HashMap<>();
	private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
	private final HashMap<Integer, Epic> epicMap = new HashMap<>();

	private static int ID = 0;

	public Task addTask(Task task) {		//метод для создания базовых задач
		task.setID(ID++);
		taskMap.put(task.getID(), task);
		return task;
	}

	public void taskList() {				//метод для получения списка всех базовых задач
		ArrayList<Task> taskList = new ArrayList<>(taskMap.values());
		System.out.println("Список задач " + taskList);
	}

	public void removeTask(int id) {		//метод для удаления базовых задач по идентификатору
		taskMap.remove(id);
	}

	public Task getTask(int id) {			//метод для получения базовых задач по идентификатору
		return taskMap.get(id);
	}

	public Task updateTask(Task newTask) {	//метод для обновления базовых задач
		taskMap.put(newTask.getID(), newTask);
		return newTask;
	}

	public void deleteTaskList() {			//метод для удаления всех базовых задач
		taskMap.clear();
	}

	public Epic addEpic(Epic epic) {		//метод для создания Эпик задач
		epic.setID(ID++);
		epicMap.put(epic.getID(), epic);
		setEpicStatus(epic);
		return epic;
	}

	public void epicList() {				//метод для получения списка всех Эпик задач
		ArrayList<Task> epicList = new ArrayList<>(epicMap.values());
		System.out.println("Список эпиков " + epicList);
	}

	public void removeEpic(int id) {		//метод для удаления Эпик задач по идентификатору
		for (Integer subTaskID : epicMap.get(id).getSubTaskIDList()) {
			subtaskMap.remove(subTaskID);
		}
		epicMap.remove(id);
	}

	public Task getEpic(int id) {			//метод для получения Эпик задач по идентификатору
		return epicMap.get(id);
	}

	public Task updateEpic(Epic newEpic) {	//метод для обновления Эпик задач
		ArrayList<Integer> list = epicMap.get(newEpic.getID()).getSubTaskIDList();
		for (Integer i : list) {
			newEpic.getSubTaskIDList().add(i);
		}
		setEpicStatus(newEpic);
		epicMap.put(newEpic.getID(), newEpic);
		return newEpic;
	}

	public void deleteEpicList() {			//метод для удаления всех Эпик задач
		subtaskMap.clear();
		epicMap.clear();
	}

	public Subtask addSubtask(Subtask subtask) {		//метод для создания подзадач в Эпике
		subtask.setID(ID++);
		subtaskMap.put(subtask.getID(), subtask);
		epicMap.get(subtask.getEpicID()).getSubTaskIDList().add(subtask.getID()); //запись subTask ID в Эпик
		setEpicStatus(epicMap.get(subtask.getEpicID()));
		return subtask;
}

	public void subtaskList(Epic epic) {						//метод для получения списка подзадач в определенном Эпике
		ArrayList<Task> subTaskList = new ArrayList<>();
		for (Integer subTaskID : epic.getSubTaskIDList()) {
			subTaskList.add(subtaskMap.get(subTaskID));
		}
		System.out.println("Список подзадач в " + epic.getName() + subTaskList);
}

	public void subtaskList() {									//метод для получения общего списка подзадач
		ArrayList<Task> subtaskList = new ArrayList<>(subtaskMap.values());
		System.out.println("Список подзадач " + subtaskList);
	}

	public void removeSubtask(Subtask subtask) {		//метод для удаления подзадач в Эпике
		epicMap.get(subtask.getEpicID()).getSubTaskIDList().remove(subtask.getID()); //удаление subtask ID из списка в эпике
		subtaskMap.remove(subtask.getID());
		setEpicStatus(epicMap.get(subtask.getEpicID()));
}

	public Subtask getSubtask(Subtask subtask) {			//метод для получения подзадач из Эпика
		return subtaskMap.get(subtask.getID());
}

	public Subtask updateSubtask(Subtask newSubtask) {	//метод для обновления подзадач в Эпике
		subtaskMap.put(newSubtask.getID(), newSubtask);
		setEpicStatus(epicMap.get(newSubtask.getEpicID()));
		return newSubtask;
}

	public void deleteSubtaskList(Epic epic) {			//метод для удаления списка всех подзадач в Эпике
		for (Integer subTaskID : epic.getSubTaskIDList()) {
			subtaskMap.remove(subTaskID);
			epic.getSubTaskIDList().remove(subTaskID);
		}
		setEpicStatus(epic);
	}

	public void setEpicStatus(Epic epic) {
		int NEW = 0;
		int IN_PROGRESS = 0;
		int DONE = 0;
		for (Subtask subtask : subtaskMap.values()) {
			switch (subtask.getStatus()) {
				case NEW: NEW++;
					break;
				case IN_PROGRESS: IN_PROGRESS++;
					break;
				case DONE: DONE++;
					break;
			}
		}
		if (subtaskMap.isEmpty()) {
			epic.setStatus(Status.NEW);
		} else if (IN_PROGRESS == 0 && DONE == 0) {
			epic.setStatus(Status.NEW);
		} else if (IN_PROGRESS == 0 && NEW == 0) {
			epic.setStatus(Status.DONE);
		} else {
			epic.setStatus(Status.IN_PROGRESS);
		}
	}
}
