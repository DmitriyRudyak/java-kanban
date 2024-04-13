package taskmanager;
import exceptions.ManagerSaveException;
import taskpackage.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
	private final File taskStorage = new File("resources/taskStorage.csv");

	private void save() {		//метод для записи задач в файл. Так же записывает историю в отдельный файл.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskStorage, StandardCharsets.UTF_8))) {
			writer.write("id,type,name,status,description,epic");
			writer.newLine();
			saveTasksToFile(writer);			//запись задач в файл
			historyToString(historyManager);	//запись истории в файл
		} catch (IOException e) {
			throw new ManagerSaveException();
		}
	}

	public void load(File file) {		//метод для загрузки задач и истории из файла
		taskMap.clear();
		subtaskMap.clear();
		epicMap.clear();

		try {
			String taskLines = Files.readString(file.toPath());
			List<String> taskLineMass = List.of(taskLines.split("\r\n"));
			for (int i = 1; i < taskLineMass.size(); i++) {
				Task task = fromString(taskLineMass.get(i));	//получение и сборка задачи
				if (task != null) {
					switch (task.getTaskType()) { 					//рапределение Task-ов по Map-ам
						case TASK:
							taskMap.put(task.getId(), task);
							break;
						case EPIC:
							epicMap.put(task.getId(), (Epic) task);
							break;
						case SUB_TASK:
							subtaskMap.put(task.getId(), (Subtask) task);
							int epicId = subtaskMap.get(task.getId()).getEpicID();
							epicMap.get(epicId).getSubTaskIDList().add(task.getId());	//запись subtaskID в Epic
							setEpicStatus(epicMap.get(epicId));
							break;
					}
				} else break;
			}
		} catch (IOException e) {
			throw new ManagerSaveException();
		}
	}

	private void saveTasksToFile(BufferedWriter writer) throws IOException {	//вспомогательный метод для записи задач в файл
		for (Task task : taskList()) {
			writer.write(toString(task));
			writer.newLine();
		}
		for (Epic epic : epicList()) {
			writer.write(toString(epic));
			writer.newLine();
		}
		for (Subtask subtask : subtaskList()) {
			writer.write(toString(subtask));
			writer.newLine();
		}
	}

	private String toString(Task task) {	//метод для создания строки из Task
		return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," +
				task.getDescription();
	}

	private String toString(Epic epic) {	//метод для создания строки из Epic
		return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," +
				epic.getDescription();
	}

	private String toString(Subtask subtask) {	//метод для создания строки из Subtask
		return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() + "," +
				subtask.getDescription() + "," + subtask.getEpicID();
	}

	private Task fromString(String value) {		//метод для сборки Task-ов из строк
		String[] taskValue = value.split(",");
		int id = Integer.parseInt(taskValue[0]);
		TaskType taskType = TaskType.valueOf(taskValue[1]);
		String name = taskValue[2];
		Status status = Status.valueOf(taskValue[3]);
		String description = taskValue[4];
		switch (taskType) {
			case TASK:
				return new Task(name, description, status, id);
			case EPIC:
				return new Epic(name, description, status, id);
			case SUB_TASK:
				int epicId = Integer.parseInt(taskValue[5]);
				return new Subtask(name, description, status, epicId, id);
		}
		return null;
	}

	private static String historyToString(HistoryManager manager) {		//метод записывает историю в файл
		File historyStorage = new File("resources/historyStorage.csv");
		List<String> historyID = new ArrayList<>();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyStorage, StandardCharsets.UTF_8))) {
			for (Task task : manager.getHistoryList()) {
				historyID.add(String.valueOf(task.getId()));
			}
			writer.write(String.join(",", historyID));
		} catch (IOException e) {
			throw new ManagerSaveException();
		}
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
	public void removeSubtask(Subtask subtask) {
		super.removeSubtask(subtask);
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

	public static void main(String[] args) throws IOException {
		File taskStorage = new File("resources/taskStorage.csv");
		File taskStorage2 = new File("resources/taskStorage2.csv");
		FileBackedTaskManager manager = Managers.getDefaultFbManager();

		Task taskOne = new Task("First", "Description", Status.NEW);
		Task taskTwo = new Task("Second", "Description", Status.IN_PROGRESS);

		Epic epic = new Epic("Epic");
		Epic epicEmpty = new Epic("EpicEmpty");

		Subtask subtaskOne = new Subtask("SubOne", "Description",Status.NEW,2);
		Subtask subtaskTwo = new Subtask("SubTwo", "Description",Status.IN_PROGRESS,2);
		Subtask subtaskThree = new Subtask("SubThree", "Description",Status.DONE,3);

		manager.addTask(taskOne);
		manager.addTask(taskTwo);

		manager.addEpic(epic);
		manager.addEpic(epicEmpty);

		manager.addSubtask(subtaskOne);
		manager.addSubtask(subtaskTwo);
		manager.addSubtask(subtaskThree);

//		System.out.println(manager.taskMap);
//		System.out.println(manager.subtaskMap);
//		System.out.println(manager.epicMap);

		Subtask subtaskFour = new Subtask("SubFour", "Description",Status.DONE,3);
		manager.addSubtask(subtaskFour);

		manager.getTask(taskOne.getId());
		manager.getTask(taskTwo.getId());
		System.out.println(manager.getHistory());
		File history = new File("resources/historyStorage.csv");
		String taskLines = Files.readString(history.toPath());
		System.out.println("taskLines = " + taskLines);
//		System.out.println(manager.subtaskMap);

//		FileBackedTaskManager managerNew = Managers.getDefaultFbManager();

//		System.out.println(managerNew.taskMap);
//		System.out.println(managerNew.subtaskMap);
//		System.out.println(managerNew.epicMap);

//		managerNew.load(taskStorage);

//		System.out.println(managerNew.taskMap);
//		System.out.println(managerNew.subtaskMap);
//		System.out.println(managerNew.epicMap);

//		managerNew.load(taskStorage2);

//		System.out.println(managerNew.taskMap);
//		System.out.println(managerNew.subtaskMap);
//		System.out.println(managerNew.epicMap);
	}
}
