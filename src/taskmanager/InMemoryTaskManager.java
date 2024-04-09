package taskmanager;
import taskpackage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
	private final HistoryManager historyManager = Managers.getDefaultHistory();
	private final HashMap<Integer, Task> taskMap = new HashMap<>();
	private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
	private final HashMap<Integer, Epic> epicMap = new HashMap<>();
	private static int taskID = 0;

	@Override
	public Task addTask(Task task) {		//метод для создания базовых задач
		task.setId(taskID++);
		taskMap.put(task.getId(), task);
		return task;
	}

	@Override
	public ArrayList<Task> taskList() {				//метод для получения списка всех базовых задач
		ArrayList<Task> taskList = new ArrayList<>(taskMap.values());
		System.out.println("Список задач " + taskList);
		return taskList;
	}

	@Override
	public void removeTask(int id) {		//метод для удаления базовых задач по идентификатору
		taskMap.remove(id);
		historyManager.remove(id);
	}

	@Override
	public Task getTask(int id) {			//метод для получения базовых задач по идентификатору
		historyManager.add(taskMap.get(id));
		return taskMap.get(id);
	}

	@Override
	public Task updateTask(Task newTask) {	//метод для обновления базовых задач
		taskMap.put(newTask.getId(), newTask);
		return newTask;
	}

	@Override
	public void deleteTaskList() {			//метод для удаления всех базовых задач
		for (Task task : taskMap.values()) {
			historyManager.remove(task.getId());
		}
		taskMap.clear();
	}

	@Override
	public Epic addEpic(Epic epic) {		//метод для создания Эпик задач
		epic.setId(taskID++);
		epicMap.put(epic.getId(), epic);
		return epic;
	}

	@Override
	public ArrayList<Epic> epicList() {				//метод для получения списка всех Эпик задач
		ArrayList<Epic> epicList = new ArrayList<>(epicMap.values());
		System.out.println("Список эпиков " + epicList);
		return epicList;
	}

	@Override
	public void removeEpic(int id) {		//метод для удаления Эпик задач по идентификатору
		for (Integer subTaskID : epicMap.get(id).getSubTaskIDList()) {
			historyManager.remove(subTaskID);
			subtaskMap.remove(subTaskID);
		}
		historyManager.remove(id);
		epicMap.remove(id);
	}

	@Override
	public Task getEpic(int id) {			//метод для получения Эпик задач по идентификатору
		historyManager.add(epicMap.get(id));
		return epicMap.get(id);
	}

	@Override
	public Task updateEpic(Epic newEpic) {	//метод для обновления Эпик задач
		epicMap.put(newEpic.getId(), newEpic);
		return newEpic;
	}

	@Override
	public void deleteEpicList() {			//метод для удаления всех Эпик задач
		for (Subtask subtask : subtaskMap.values()) {
			historyManager.remove(subtask.getId());
		}
		for (Epic epic : epicMap.values()) {
			historyManager.remove(epic.getId());
		}
		subtaskMap.clear();
		epicMap.clear();
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {		//метод для создания подзадач в Эпике
		subtask.setId(taskID++);
		subtaskMap.put(subtask.getId(), subtask);
		Epic buffer = epicMap.get(subtask.getEpicID());
		buffer.getSubTaskIDList().add(subtask.getId()); //запись subTask ID в Эпик
		setEpicStatus(epicMap.get(subtask.getEpicID()));
		return subtask;
	}

	@Override
	public ArrayList<Subtask> subtaskList(Epic epic) {						//метод для получения списка подзадач в определенном Эпике
		ArrayList<Subtask> subTaskList = new ArrayList<>();
		for (Integer subTaskID : epic.getSubTaskIDList()) {
			subTaskList.add(subtaskMap.get(subTaskID));
		}
		System.out.println("Список подзадач в " + epic.getName() + subTaskList);
		return subTaskList;
	}

	@Override
	public ArrayList<Subtask> subtaskList() {									//метод для получения общего списка подзадач
		ArrayList<Subtask> subtaskList = new ArrayList<>(subtaskMap.values());
		System.out.println("Список подзадач " + subtaskList);
		return subtaskList;
	}

	@Override
	public void removeSubtask(Subtask subtask) {		//метод для удаления подзадач в Эпике
		epicMap.get(subtask.getEpicID()).getSubTaskIDList().remove(Integer.valueOf(subtask.getId())); //удаление subtask ID из списка в эпике

		historyManager.remove(subtask.getId());
		subtaskMap.remove(subtask.getId());
		setEpicStatus(epicMap.get(subtask.getEpicID()));
	}

	@Override
	public Subtask getSubtask(int id) {			//метод для получения подзадач из Эпика
		historyManager.add(subtaskMap.get(id));
		return subtaskMap.get(id);
}

	@Override
	public Subtask updateSubtask(Subtask newSubtask) {	//метод для обновления подзадач в Эпике
		subtaskMap.put(newSubtask.getId(), newSubtask);
		setEpicStatus(epicMap.get(newSubtask.getEpicID()));
		return newSubtask;
	}

	@Override
	public void deleteSubtaskList() {			//метод для удаления списка всех подзадач
		for (Subtask subtask : subtaskMap.values()) {
			historyManager.remove(subtask.getId());
		}
		subtaskMap.clear();
		for (Epic epic : epicMap.values()) {
			epic.getSubTaskIDList().clear();
			setEpicStatus(epic);
		}
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistoryList();
	}

	@Override
	public void setEpicStatus(Epic epic) {
		int newTask = 0;
		int inProgressTask = 0;
		int doneTask = 0;
		ArrayList<Subtask> subtasksFromEpic = new ArrayList<>();
		for (Integer id : epic.getSubTaskIDList()) {
			subtasksFromEpic.add(subtaskMap.get(id));
		}
		for (Subtask subtask : subtasksFromEpic) {
			switch (subtask.getStatus()) {
				case NEW: newTask++;
					break;
				case IN_PROGRESS: inProgressTask++;
					break;
				case DONE: doneTask++;
					break;
			}
		}
		if (subtaskMap.isEmpty()) {
			epic.setStatus(Status.NEW);
		} else if (inProgressTask == 0 && doneTask == 0) {
			epic.setStatus(Status.NEW);
		} else if (inProgressTask == 0 && newTask == 0) {
			epic.setStatus(Status.DONE);
		} else {
			epic.setStatus(Status.IN_PROGRESS);
		}
	}
}
