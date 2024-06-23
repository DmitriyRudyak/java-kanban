package taskmanager;
import exceptions.TimeCrossingException;
import taskpackage.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
	protected final HistoryManager historyManager = Managers.getDefaultHistory();
	protected final HashMap<Integer, Task> taskMap = new HashMap<>();
	protected final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
	protected final HashMap<Integer, Epic> epicMap = new HashMap<>();
	protected static int taskID = 0;
	protected TreeSet<Task> prioritySet = new TreeSet<>();

	@Override
	public Task addTask(Task task) {		//метод для создания базовых задач
		if (checkTaskCrossing(task)) {
			task.setId(taskID++);
			taskMap.put(task.getId(), task);
			prioritySet = getPrioritizedTasks();
		} else {
			throw new TimeCrossingException("Выберите другое время для " + task);
		}
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
		prioritySet = getPrioritizedTasks();
	}

	@Override
	public Task getTask(int id) {			//метод для получения базовых задач по идентификатору
		historyManager.add(taskMap.get(id));
		return taskMap.get(id);
	}

	@Override
	public Task updateTask(Task newTask) {	//метод для обновления базовых задач
		if (checkTaskCrossing(newTask)) {
			taskMap.put(newTask.getId(), newTask);
			prioritySet = getPrioritizedTasks();
		} else {
			throw new TimeCrossingException("Выберите другое время для " + newTask);
		}
		return newTask;
	}

	@Override
	public void deleteTaskList() {			//метод для удаления всех базовых задач
		for (Task task : taskMap.values()) {
			historyManager.remove(task.getId());
		}
		taskMap.clear();
		prioritySet = getPrioritizedTasks();
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
		prioritySet = getPrioritizedTasks();
	}

	@Override
	public Epic getEpic(int id) {			//метод для получения Эпик задач по идентификатору
		historyManager.add(epicMap.get(id));
		return epicMap.get(id);
	}

	@Override
	public Epic updateEpic(Epic newEpic) {	//метод для обновления Эпик задач
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
		prioritySet = getPrioritizedTasks();
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {		//метод для создания подзадач в Эпике
		if (checkTaskCrossing(subtask)) {
			subtask.setId(taskID++);
			subtaskMap.put(subtask.getId(), subtask);
			Epic buffer = epicMap.get(subtask.getEpicID());
			buffer.getSubTaskIDList().add(subtask.getId()); //запись subTask ID в Эпик
			setEpicStatus(epicMap.get(subtask.getEpicID()));

			if (subtask.getDuration() != null) {
				setEpicClocks(epicMap.get(subtask.getEpicID()));
			}
			prioritySet = getPrioritizedTasks();
		} else {
			throw new TimeCrossingException("Выберите другое время для " + subtask);
		}
		return subtask;
	}

	@Override
	public ArrayList<Subtask> subtaskList(int id) {						//метод для получения списка подзадач в определенном Эпике
		System.out.println("Список подзадач в " + epicMap.get(id).getName());
		return epicMap.get(id).getSubTaskIDList().stream()
				.map(subtaskMap::get)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public ArrayList<Subtask> subtaskList() {									//метод для получения общего списка подзадач
		return new ArrayList<>(subtaskMap.values());
	}

	@Override
	public void removeSubtask(int id) {		//метод для удаления подзадач в Эпике
		Epic epic = epicMap.get(subtaskMap.get(id).getEpicID());
		epic.getSubTaskIDList().remove(Integer.valueOf(id)); //удаление subtask ID из списка в эпике

		historyManager.remove(id);
		setEpicStatus(epic);

		if (subtaskMap.get(id).getDuration() != null) {
			setEpicClocks(epic);
		}
		subtaskMap.remove(id);
		prioritySet = getPrioritizedTasks();
	}

	@Override
	public Subtask getSubtask(int id) {			//метод для получения подзадачи из Эпика
		historyManager.add(subtaskMap.get(id));
		return subtaskMap.get(id);
}

	@Override
	public Subtask updateSubtask(Subtask newSubtask) {	//метод для обновления подзадач в Эпике
		if (checkTaskCrossing(newSubtask)) {
			subtaskMap.put(newSubtask.getId(), newSubtask);
			setEpicStatus(epicMap.get(newSubtask.getEpicID()));

			if (newSubtask.getDuration() != null) {
				setEpicClocks(epicMap.get(newSubtask.getEpicID()));
			}
			prioritySet = getPrioritizedTasks();
		} else {
			throw new TimeCrossingException("Выберите другое время для " + newSubtask);
		}
		return newSubtask;
	}

	@Override
	public void deleteSubtaskList() {			//метод для удаления списка всех подзадач
		subtaskMap.values().stream()
				.mapToInt(Task::getId)
				.forEach(historyManager::remove);
		subtaskMap.clear();
		epicMap.values().forEach(epic -> {
			epic.getSubTaskIDList().clear();
			setEpicStatus(epic);
			setEpicClocks(epic);
		});
		prioritySet = getPrioritizedTasks();
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
		ArrayList<Subtask> subtasksFromEpic = epic.getSubTaskIDList().stream()
				.map(subtaskMap::get)
				.collect(Collectors.toCollection(ArrayList::new));
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

		ArrayList<Subtask> subtasksFromEpic = epic.getSubTaskIDList().stream()
				.map(subtaskMap::get)
				.filter(subtask -> subtask.getDuration() != null)
				.collect(Collectors.toCollection(ArrayList::new));

		Duration allSubDuration = Duration.ofMinutes(0);
		LocalDateTime earliestStartTime = null;
		LocalDateTime lastStartTime = null;
		Duration lastTaskDuration = Duration.ofMinutes(0);
		for (Subtask subtask : subtasksFromEpic) {
			allSubDuration = allSubDuration.plus(subtask.getDuration());
			if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
				earliestStartTime = subtask.getStartTime();
			}
			if (lastStartTime == null || subtask.getStartTime().isAfter(lastStartTime)) {
				lastStartTime = subtask.getStartTime();
				lastTaskDuration = subtask.getDuration();
			}
		}
		epic.setDuration(allSubDuration);		//расчет duration эпика
		epic.setStartTime(earliestStartTime);	//расчет startTime эпика
		if (lastStartTime != null) {
			epic.setEndTime(lastStartTime.plus(lastTaskDuration));			//расчет endTime эпика
		} else epic.setEndTime(null);
	}

	@Override
	public TreeSet<Task> getPrioritizedTasks() {		//метод для сортировки тасков по приоритету
		List<Task> allTasks = new ArrayList<>(taskMap.values());
		allTasks.addAll(subtaskMap.values());
		return allTasks.stream()
				.filter(task -> !task.hasClocks())
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public boolean checkTaskCrossing(Task newTask) {	//метод для проверки тасков на пересечение
		if (prioritySet.isEmpty()) {
			return true;
		} else {
		if (newTask.getDuration() != null) {
			return prioritySet.stream()
					.noneMatch(task -> newTask.getStartTime().isAfter(task.getStartTime()) &&
										newTask.getStartTime().isBefore(task.getEndTime()) ||
										newTask.getEndTime().isAfter(task.getStartTime()) &&
										newTask.getEndTime().isBefore(task.getEndTime()));
		} else return true;
			}
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

        //Тесты получения истории
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

		taskManager.removeSubtask(subtaskThree.getId());
		System.out.println(taskManager.getHistory());
		System.out.println();

		Task taskWithTime = new Task("Time", " ", Status.NEW, 90, "2024-10-10T20:20");
//		Task taskWithTime2 = new Task("Time2", " ", Status.NEW, 60, "2024-10-10T21:00");
//		Task taskWithTime3 = new Task("Time3", " ", Status.NEW, 300, "2024-10-10T23:20");
		Task taskThree = new Task("Third", "...", Status.NEW);
		Subtask subtaskWithTime = new Subtask("SubTime", " ", Status.NEW, 30, "2022-10-10T20:20", epic.getId());
		Subtask subtaskWithTime2 = new Subtask("SubTime2", " ", Status.NEW, 120, "2010-10-10T20:20", epic.getId());
		Subtask subtaskWithTime3 = new Subtask("SubTime3", " ", Status.NEW, 500, "2030-10-10T20:20", epic.getId());

//		Task taskWithTime4 = new Task("Time4", " ", Status.NEW, 30, "2024-10-10T23:30");
//		Subtask subtaskWithTime4 = new Subtask("SubTime4", " ", Status.NEW, 10, "2030-10-10T20:40", epic.getId());

		taskManager.addTask(taskWithTime);
//		taskManager.addTask(taskWithTime2);
//		taskManager.addTask(taskWithTime3);
		taskManager.addTask(taskThree);
		taskManager.addSubtask(subtaskWithTime);
		taskManager.addSubtask(subtaskWithTime2);
		taskManager.addSubtask(subtaskWithTime3);

//		taskManager.addTask(taskWithTime4);
//		taskManager.addSubtask(subtaskWithTime4);

		System.out.println(taskManager.getTask(taskWithTime.getId()));
		System.out.println(taskManager.getSubtask(subtaskWithTime.getId()));
		System.out.println(taskWithTime.getEndTime());
		System.out.println(epic.getStartTime() + " " + epic.getEndTime() + " " + epic.getDuration());
		System.out.println(taskManager.getPrioritizedTasks().size());
		System.out.println(epic.getStartTime());
		System.out.println(epic.getEndTime());
		System.out.println(epic.getDuration());

		System.out.println();
		taskManager.deleteSubtaskList();
		System.out.println(epic.getStartTime());
		System.out.println(epic.getEndTime());
		System.out.println(epic.getDuration());
	}
}
