package taskmanager;
import taskpackage.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
	protected final HistoryManager historyManager = Managers.getDefaultHistory();
	protected final HashMap<Integer, Task> taskMap = new HashMap<>();
	protected final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
	protected final HashMap<Integer, Epic> epicMap = new HashMap<>();
	protected static int taskID = 0;

	@Override
	public Task addTask(Task task) {		//метод для создания базовых задач
		task.setId(taskID++);
		taskMap.put(task.getId(), task);
		return task;
	}

	@Override
	public ArrayList<Task> taskList() {				//метод для получения списка всех базовых задач
		return new ArrayList<>(taskMap.values());
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
		return new ArrayList<>(epicMap.values());
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

		if (subtask.getDuration() != null) {
			setEpicClocks(epicMap.get(subtask.getEpicID()));
		}
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
		return new ArrayList<>(subtaskMap.values());
	}

	@Override
	public void removeSubtask(Subtask subtask) {		//метод для удаления подзадач в Эпике
		epicMap.get(subtask.getEpicID()).getSubTaskIDList().remove(Integer.valueOf(subtask.getId())); //удаление subtask ID из списка в эпике

		historyManager.remove(subtask.getId());
		subtaskMap.remove(subtask.getId());
		setEpicStatus(epicMap.get(subtask.getEpicID()));

		if (subtask.getDuration() != null) {
			setEpicClocks(epicMap.get(subtask.getEpicID()));
		}
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

		if (newSubtask.getDuration() != null) {
			setEpicClocks(epicMap.get(newSubtask.getEpicID()));
		}
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
			setEpicClocks(epic);
		}
	}

	@Override
	public List<Task> getHistory() {			//метод для получения истории получения тасков
		return historyManager.getHistoryList();
	}

	@Override
	public void setEpicStatus(Epic epic) {		//метод для расчета статуса эпика
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

	@Override
	public void setEpicClocks(Epic epic) {		//метод для расчета duration, startTime, endTime эпика
		ArrayList<Subtask> subtasksFromEpic = new ArrayList<>();
		for (Integer id : epic.getSubTaskIDList()) {
			subtasksFromEpic.add(subtaskMap.get(id));
		}

		Duration allSubDuration = Duration.ofMinutes(0);
		for (Subtask subtask : subtasksFromEpic) {
			if (subtask.getDuration() != null) {
				allSubDuration = allSubDuration.plus(subtask.getDuration());
			}
		}
		epic.setDuration(allSubDuration);		//расчет duration эпика

		LocalDateTime earliestStartTime = null;
		for (Subtask subtask : subtasksFromEpic) {
			if (earliestStartTime == null) {
				earliestStartTime = subtask.getStartTime();
			} else if (subtask.getStartTime().isBefore(earliestStartTime)) {
				earliestStartTime = subtask.getStartTime();
			}
		}
		epic.setStartTime(earliestStartTime);	//расчет startTime эпика

		LocalDateTime lastStartTime = null;
		Duration lastTaskDuration = Duration.ofMinutes(0);
		for (Subtask subtask : subtasksFromEpic) {
			if (lastStartTime == null) {
				lastStartTime = subtask.getStartTime();
				lastTaskDuration = subtask.getDuration();
			} else if (subtask.getStartTime().isAfter(lastStartTime)) {
				lastStartTime = subtask.getStartTime();
				lastTaskDuration = subtask.getDuration();
			}
		}
		epic.setEndTime(lastStartTime.plus(lastTaskDuration));			//расчет endTime эпика
	}

	public static void main(String[] args) {
		TaskManager taskManager = Managers.getDefault();

		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);

		Epic epic = new Epic("Epic");
		Epic epicEmpty = new Epic("EpicEmpty");

		Subtask subtaskOne = new Subtask("SubOne", "...",Status.NEW,2);
		Subtask subtaskTwo = new Subtask("SubTwo", "...",Status.IN_PROGRESS,2);
		Subtask subtaskThree = new Subtask("SubThree", "...",Status.DONE,2);

		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);

		taskManager.addEpic(epic);
		taskManager.addEpic(epicEmpty);

		taskManager.addSubtask(subtaskOne);
		taskManager.addSubtask(subtaskTwo);
		taskManager.addSubtask(subtaskThree);

//        Тесты получения истории
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		System.out.println(taskManager.getHistory());

		taskManager.getTask(taskOne.getId());
		System.out.println(taskManager.getHistory());

		taskManager.getEpic(epic.getId());
		taskManager.getTask(taskTwo.getId());
		System.out.println(taskManager.getHistory());

		taskManager.getSubtask(subtaskOne.getId());
		taskManager.getSubtask(subtaskThree.getId());
		System.out.println(taskManager.getHistory());

		taskManager.removeTask(taskOne.getId());
		System.out.println(taskManager.getHistory());

		taskManager.removeSubtask(subtaskThree);
		System.out.println(taskManager.getHistory());

//        taskManager.removeEpic(epic.getId());
//        System.out.println(taskManager.getHistory());

		Task taskWithTime = new Task("Time", " ", Status.NEW, 30, "10.10.2020, 20:20");
		Subtask subtaskWithTime = new Subtask("Time", " ", Status.NEW, 30, "10.10.2010, 20:20", epic.getId());
		Subtask subtaskWithTime2 = new Subtask("Time2", " ", Status.NEW, 120, "10.10.2024, 20:20", epic.getId());
		Subtask subtaskWithTime3 = new Subtask("Time3", " ", Status.NEW, 10, "10.10.2020, 20:20", epic.getId());

		taskManager.addTask(taskWithTime);
		taskManager.addSubtask(subtaskWithTime);
		taskManager.addSubtask(subtaskWithTime2);
		taskManager.addSubtask(subtaskWithTime3);

		System.out.println(taskManager.getTask(taskWithTime.getId()));
		System.out.println(taskManager.getSubtask(subtaskWithTime.getId()));
		System.out.println(taskWithTime.getEndTime());
		System.out.println(epic.getStartTime() + " " + epic.getEndTime() + " " + epic.getDuration());
	}
}
