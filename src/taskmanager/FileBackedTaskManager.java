package taskmanager;
import exceptions.ManagerSaveException;
import taskpackage.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final File taskStorage;

	public FileBackedTaskManager() {
		taskStorage = new File("./resources/taskStorage.csv");
	}

	public FileBackedTaskManager(String path) {
		taskStorage = new File(path);
	}

	private void save() {		//метод для записи задач в файл. Так же записывает историю в отдельный файл.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskStorage, StandardCharsets.UTF_8))) {
			writer.write("id,type,name,status,description,epic,startTime,duration\n");
			saveTasksToFile(writer);	//запись задач в файл
			writer.write("\n");
			historyToString(writer);	//запись истории в файл
		} catch (IOException e) {
			throw new ManagerSaveException("Непредвиденная ошибка");
		}
	}

	public static FileBackedTaskManager loadFromFile(File file) {	//метод для загрузки данных и истории изи файла
		System.out.println("Загрузка из файла " + file);
		FileBackedTaskManager fileManager = new FileBackedTaskManager();
		Map<Integer, Task> fileHistory = new HashMap<>();
		List<Integer> idsHistory = new ArrayList<>();
		try {
			String taskLines = Files.readString(file.toPath());	//считывание файла и запись в список построчно
			List<String> taskLineMass = List.of(taskLines.split("\n"));
			for (int i = 1; i < taskLineMass.size(); i++) {		//если строка пустая, и следующая строка не пустая, формируется список с идентификаторами истории
				if (taskLineMass.get(i).isEmpty() && !taskLineMass.get(i + 1).isEmpty()) {
				idsHistory = historyFromString(taskLineMass.get(i + 1));
				break;
			}
				Task task = fromString(taskLineMass.get(i));	//получение и сборка задачи
				fileHistory.put(task.getId(), task); 			//складываем в мапу для истории
				switch (task.getTaskType()) { 					//рапределение Task-ов по мапам
					case TASK:
						fileManager.taskMap.put(task.getId(), task);
						break;
					case EPIC:
						fileManager.epicMap.put(task.getId(), (Epic) task);
						break;
					case SUB_TASK:
						fileManager.subtaskMap.put(task.getId(), (Subtask) task);
						int epicId = fileManager.subtaskMap.get(task.getId()).getEpicID();
						fileManager.epicMap.get(epicId).getSubTaskIDList().add(task.getId());	//запись subtaskID в Epic
						fileManager.setEpicStatus(fileManager.epicMap.get(epicId));
						if (task.getDuration() != null) {
							fileManager.setEpicClocks(fileManager.epicMap.get(epicId));
						}
						break;
				}
				if (task.getId() > taskID) {
					taskID = task.getId();
				}
			}
			for (Integer id : idsHistory) {		//заполнение истории задач
				fileManager.historyManager.add(fileHistory.get(id));
			}
		} catch (IOException e) {
			throw new ManagerSaveException("Непредвиденная ошибка");
		}
		return fileManager;
	}

	private void saveTasksToFile(BufferedWriter writer) throws IOException {	//вспомогательный метод для записи задач в файл
		for (Task task : taskList()) {
			writer.write(toString(task));
		}
		for (Epic epic : epicList()) {
			writer.write(toString(epic));
		}
		for (Subtask subtask : subtaskList()) {
			writer.write(toString(subtask));
		}
	}

	private String toString(Task task) {	//метод для создания строки из Task
		if (task.getDuration() != null) {
			return String.format("%s,%s,%s,%s,%s,%s,%s,%s,\n", task.getId(), task.getTaskType(), task.getName(),
					task.getStatus(), task.getDescription(), "", task.getStartTime(), task.getDuration().toMinutes());
		} else return String.format("%s,%s,%s,%s,%s,\n", task.getId(), task.getTaskType(), task.getName(),
				task.getStatus(), task.getDescription());
	}

	private String toString(Epic epic) {	//метод для создания строки из Epic
		return String.format("%s,%s,%s,%s,%s,\n", epic.getId(), epic.getTaskType(), epic.getName(),
				epic.getStatus(), epic.getDescription());
	}

	private String toString(Subtask subtask) {	//метод для создания строки из Subtask
		if (subtask.getDuration() != null) {
			return String.format("%s,%s,%s,%s,%s,%s,%s,%s,\n", subtask.getId(), subtask.getTaskType(), subtask.getName(),
					subtask.getStatus(), subtask.getDescription(), subtask.getEpicID(), subtask.getStartTime(), subtask.getDuration().toMinutes());
		} else return String.format("%s,%s,%s,%s,%s,%s,\n", subtask.getId(), subtask.getTaskType(), subtask.getName(),
				subtask.getStatus(), subtask.getDescription(), subtask.getEpicID());
	}

	private static Task fromString(String value) {		//метод для сборки Task-ов из строк
		String[] taskValue = value.split(",");
		int id = Integer.parseInt(taskValue[0]);
		TaskType taskType = TaskType.valueOf(taskValue[1]);
		String name = taskValue[2];
		Status status = Status.valueOf(taskValue[3]);
		String description = taskValue[4];
		String startTime = "";
		int duration = 0;
		if (taskValue.length > 6) {
			startTime = taskValue[6];
			duration = Integer.parseInt(taskValue[7]);
		}
		switch (taskType) {
			case TASK:
				if (duration > 0) return new Task(name, description, status, duration, startTime, id);
				return new Task(name, description, status, id);
			case EPIC:
				return new Epic(name, description, status, id);
			case SUB_TASK:
				int epicId = Integer.parseInt(taskValue[5]);
				if (duration > 0) return new Subtask(name, description, status, duration, startTime, epicId, id);
				return new Subtask(name, description, status, epicId, id);
		}
		return null;
	}

	private String historyToString(BufferedWriter writer) throws IOException {		//метод записывает историю в файл
		List<String> historyID = new ArrayList<>();
			for (Task task : getHistory()) {
				historyID.add(String.valueOf(task.getId()));
			}
			writer.write(String.join(",", historyID));
		return String.join(",", historyID);
	}

	private static List<Integer> historyFromString(String value) {		//метод восстанавливает историю из файла
		List<Integer> idsHistory = new ArrayList<>();
		String[] line = value.split(",");
		for (String id : line) {
			idsHistory.add(Integer.valueOf(id));
		}
		return idsHistory;
	}

	@Override
	public Task addTask(Task task) {
		super.addTask(task);
		save();
		return task;
	}

	@Override
	public void removeTask(int id) {
		super.removeTask(id);
		save();
	}

	@Override
	public Task updateTask(Task newTask) {
		super.updateTask(newTask);
		save();
		return newTask;
	}

	@Override
	public void deleteTaskList() {
		super.deleteTaskList();
		save();
	}

	@Override
	public Epic addEpic(Epic epic) {
		super.addEpic(epic);
		save();
		return epic;
	}

	@Override
	public void removeEpic(int id) {
		super.removeEpic(id);
		save();
	}

	@Override
	public Task updateEpic(Epic newEpic) {
		super.updateEpic(newEpic);
		save();
		return newEpic;
	}

	@Override
	public void deleteEpicList() {
		super.deleteEpicList();
		save();
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {
		super.addSubtask(subtask);
		save();
		return subtask;
	}

	@Override
	public void removeSubtask(int id) {
		super.removeSubtask(id);
		save();
	}

	@Override
	public Subtask updateSubtask(Subtask newSubtask) {
		super.updateSubtask(newSubtask);
		save();
		return newSubtask;
	}

	@Override
	public void deleteSubtaskList() {
		super.deleteSubtaskList();
		save();
	}

	@Override
	public Task getTask(int id) {
		super.getTask(id);
		save();
		return taskMap.get(id);
	}

	@Override
	public Task getEpic(int id) {
		super.getEpic(id);
		save();
		return epicMap.get(id);
	}

	@Override
	public Subtask getSubtask(int id) {
		super.getSubtask(id);
		save();
		return subtaskMap.get(id);
	}

	public static void main(String[] args) {
		File taskStorage = new File("resources/taskStorage.csv");
		FileBackedTaskManager manager = Managers.getDefaultFbManager();

		Task taskOne = new Task("First", "Description", Status.NEW);
		Task taskTwo = new Task("Second", "Description", Status.IN_PROGRESS);

		Epic epic = new Epic("Epic");
//		Epic epicEmpty = new Epic("EpicEmpty");

//		Subtask subtaskOne = new Subtask("SubOne", "Description",Status.NEW,2);
//		Subtask subtaskTwo = new Subtask("SubTwo", "Description",Status.IN_PROGRESS,2);
//		Subtask subtaskThree = new Subtask("SubThree", "Description",Status.DONE,3);

		manager.addTask(taskOne);
		manager.addTask(taskTwo);
//
		manager.addEpic(epic);
//		manager.addEpic(epicEmpty);
//
//		manager.addSubtask(subtaskOne);
//		manager.addSubtask(subtaskTwo);
//		manager.addSubtask(subtaskThree);

		Task taskWithTime = new Task("Time", "", Status.NEW, 30, "2020-10-10T20:20");
		Task taskWithTime2 = new Task("Time2", "", Status.NEW, 320, "2022-10-10T20:20");
		Subtask subtaskWithTime = new Subtask("Time", "", Status.NEW, 30, "2010-10-10T20:20", epic.getId());
		Subtask subtaskWithTime2 = new Subtask("Time2", "", Status.NEW, 120, "2020-10-10T20:20", epic.getId());
		Subtask subtaskWithTime3 = new Subtask("Time3", "", Status.NEW, 10, "2030-10-10T20:20", epic.getId());

		manager.addTask(taskWithTime);
		manager.addTask(taskWithTime2);
		manager.addSubtask(subtaskWithTime);
		manager.addSubtask(subtaskWithTime2);
		manager.addSubtask(subtaskWithTime3);
//
//		System.out.println(manager.taskMap);
//
//		manager.getTask(taskOne.getId());
//		manager.getTask(taskTwo.getId());
//		System.out.println(manager.getHistory());

		System.out.println();
		System.out.println(manager.taskMap);
		System.out.println(manager.subtaskMap);
		System.out.println(manager.epicMap);
		System.out.println();

		FileBackedTaskManager managerNew = FileBackedTaskManager.loadFromFile(taskStorage);

//		System.out.println(managerNew.getHistory());

		System.out.println();
		System.out.println(managerNew.taskMap);
		System.out.println(managerNew.subtaskMap);
		System.out.println(managerNew.epicMap);
		System.out.println(managerNew.getPrioritizedTasks());
	}
}
