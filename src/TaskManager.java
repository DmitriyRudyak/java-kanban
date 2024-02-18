import java.util.HashMap;

public class TaskManager {
	static HashMap<Integer, Task> taskMap = new HashMap<>();
	static HashMap<Integer, Epic> epicMap = new HashMap<>();

	ID_Generator generator = new ID_Generator();

	public Task addTask(Task task) {		//метод для создания базовых задач
		boolean exist = false;
		if (taskMap.isEmpty()) {
			task.ID = generator.generateID();
			taskMap.put(task.ID, task);
		} else {
			for (Task value : taskMap.values()) {
				if (value.equals(task)) {
					exist = true;
				}
			}
			if (exist) {
				System.out.println("Ошибка. Такая задача уже записана.");
			} else {
				task.ID = generator.generateID();
				taskMap.put(task.ID, task);
			}
		}
		return task;
	}

	public void taskList() {				//метод для получения списка всех базовых задач
		System.out.println("Список задач " + taskMap);
	}

	public Task removeTask(int id) {		//метод для удаления базовых задач по идентификатору
		Task task = taskMap.get(id);
		taskMap.remove(id);
		return task;
	}

	public Task getTask(int id) {			//метод для получения базовых задач по идентификатору
		return taskMap.get(id);
	}

	public Task updateTask(Task targetTask, Task newTask) {	//метод для обновления базовых задач
		newTask.ID = targetTask.ID;
		taskMap.put(newTask.ID, newTask);
		return newTask;
	}

	public void deleteTaskList() {			//метод для удаления всех базовых задач
		taskMap.clear();
	}

	public Epic addEpic(Epic epic) {		//метод для создания Эпик задач
		boolean exist = false;
		if (epicMap.isEmpty()) {
			epic.ID = generator.generateID();
			epicMap.put(epic.ID, epic);
		} else {
			for (Epic value : epicMap.values()) {
				if (value.equals(epic)) {
					exist = true;
				}
			}
			if (exist) {
				System.out.println("Ошибка. Такая задача уже записана.");
			} else {
				epic.ID = generator.generateID();
				epicMap.put(epic.ID, epic);
			}
		}
		return epic;
	}

	public void epicList() {				//метод для получения списка всех Эпик задач
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
		System.out.println("Список эпиков " + epicMap);
	}

	public Task removeEpic(int id) {		//метод для удаления Эпик задач по идентификатору
		Epic epic = epicMap.get(id);
		epicMap.remove(id);
		return epic;
	}

	public Task getEpic(int id) {			//метод для получения Эпик задач по идентификатору
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
		return epicMap.get(id);
	}

	public Task updateEpic(Epic targetEpIC, Epic newEpic) {	//метод для обновления Эпик задач
		newEpic.ID = targetEpIC.ID;
		taskMap.put(newEpic.ID, newEpic);
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
		return newEpic;
	}

	public void deleteEpicList() {			//метод для удаления всех Эпик задач
		epicMap.clear();
	}

	public Subtask addSubtask(Subtask subtask, Epic epic) {		//метод для создания подзадач в Эпике
		boolean exist = false;
		if (epic.subTaskMap.isEmpty()) {
			subtask.ID = generator.generateID();
			epic.addSubtask(subtask);
			epic.setEpicStatus();
		} else {
			for (Subtask value : epic.subTaskMap.values()) {
				if (value.equals(subtask)) {
					exist = true;
				}
			}
			if (exist) {
				System.out.println("Ошибка. Такая задача уже записана.");
			} else {
				subtask.ID = generator.generateID();
				epic.addSubtask(subtask);
				epic.setEpicStatus();
			}
		}
		return subtask;
}

	public void subtaskList(Epic epic) {						//метод для получения списка подзадач Эпика
		System.out.println("Список подзадач в " + epic.name + epic.subTaskMap);
}

	public Subtask removeSubtask(int idOfEpic, int idOfSubtask) {		//метод для удаления подзадач в Эпике
		Subtask subtask = epicMap.get(idOfEpic).subTaskMap.get(idOfSubtask);
		epicMap.get(idOfEpic).subTaskMap.remove(idOfSubtask);
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
		return subtask;
}

	public Subtask getSubtask(int idOfEpic, int idOfSubtask) {			//метод для получения подзадач из Эпика
		return epicMap.get(idOfEpic).subTaskMap.get(idOfSubtask);
}

	public Subtask updateSubtask(int idOfEpic, Subtask targetSubtask, Subtask newSubtask) {	//метод для обновления подзадач в Эпике
		newSubtask.ID = targetSubtask.ID;
		epicMap.get(idOfEpic).subTaskMap.put(newSubtask.ID, newSubtask);
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
		return newSubtask;
}

	public void deleteSubtaskList(int idOfEpic) {			//метод для удаления списка подзадач в Эпике
		epicMap.get(idOfEpic).subTaskMap.clear();
		for (Epic epic : epicMap.values()) {
			epic.setEpicStatus();
		}
	}
}
