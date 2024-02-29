package TaskManagerPackage.Test;

import TaskManagerPackage.*;
import TaskPackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
	Managers manager = new Managers();
	@Test
	void ifTaskIDisSameThenTaskIsSame() {
		Task sameTask1 = new Task("Task","Task", Status.NEW, 0);
		Task sameTask2 = new Task("Task","Task", Status.NEW, sameTask1.getID());
		Assertions.assertEquals(sameTask1, sameTask2);
	}

	@Test
	void ifSubTaskIDisSameThenTaskIsSame() {
		Subtask sameTask1 = new Subtask("Subtask","Task", Status.NEW, 0);
		Subtask sameTask2 = new Subtask("Subtask","Task", Status.NEW, sameTask1.getID());
		Assertions.assertEquals(sameTask1, sameTask2);
	}

	@Test
	void ifEpicIDisSameThenTaskIsSame() {
		Epic sameTask1 = new Epic("Epic", 0);
		Epic sameTask2 = new Epic("Epic", sameTask1.getID());
		Assertions.assertEquals(sameTask1, sameTask2);
	}

	@Test
	void epicCantBeSubtask() {
		TaskManager tsk = manager.getDefault();
		Epic epic = new Epic("Epic");
		tsk.addEpic(epic);
		/*tsk.addSubtask(epic);*/		//Программа выдает ошибку несовместимости типов, запрещая данный подход к методу
	}

	@Test
	void subtaskCantBeEpic() {
		TaskManager tsk = manager.getDefault();
		Subtask subtask = new Subtask("Subtask", "subtask", Status.NEW, 0);
		/*tsk.addEpic(subtask);*/		//Программа выдает ошибку несовместимости типов, запрещая данный подход к методу
	}

	@Test
	void managersCreatesInitialisedTaskManagerSamples() {
		TaskManager taskManager = manager.getDefault();

		Task taskOne = new Task("Task");
		taskManager.addTask(taskOne);
		taskManager.getTask(taskOne.getID());
		List<Task> history = taskManager.getHistory();
		List<Task> historyManual = new ArrayList<>();
		historyManual.add(taskOne);

		Assertions.assertEquals(history, historyManual);
	}

	@Test
	void managersCreatesInitialisedHistoryManagerSamples() {
		HistoryManager historyManager = manager.getDefaultHistory();
	}

	@Test
	void taskManagerCanWorkWithID() {
		TaskManager tskManager = manager.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int ID = task.getID();
		Assertions.assertEquals(task, tskManager.getTask(ID));

		Epic epic = new Epic("Epic");
		tskManager.addEpic(epic);
		ID = epic.getID();
		Assertions.assertEquals(epic, tskManager.getEpic(ID));

		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, ID);
		tskManager.addSubtask(subtask);
		ID = subtask.getID();
		Assertions.assertEquals(subtask, tskManager.getSubtask(ID));
	}

	@Test
	void taskManagerCanWorkWithManualID() {
		TaskManager tskManager = manager.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		Task updateTask = new Task("TaskNew", "TaskNew", Status.NEW, 0);
		tskManager.updateTask(updateTask);
		Assertions.assertEquals(updateTask, tskManager.getTask(0));
	}

	@Test
	void taskShouldBeUnchangedAfterAddToManager() {
		TaskManager tskManager = manager.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int ID = task.getID();

		String taskName = tskManager.getTask(ID).getName();
		String taskDescription = tskManager.getTask(ID).getDescription();
		Status taskStatus = tskManager.getTask(ID).getStatus();
		int taskID = tskManager.getTask(ID).getID();

		Assertions.assertEquals(task.getName(), taskName);
		Assertions.assertEquals(task.getDescription(), taskDescription);
		Assertions.assertEquals(task.getStatus(), taskStatus);
		Assertions.assertEquals(task.getID(), taskID);
	}

	@Test
	void taskInHistoryManagerIsUnchanged() {
		TaskManager tskManager = manager.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int ID = task.getID();
		tskManager.getTask(ID);

		Task taskInHistory = tskManager.getHistory().get(0);

		String taskName = taskInHistory.getName();
		String taskDescription = taskInHistory.getDescription();
		Status taskStatus = taskInHistory.getStatus();
		int taskID = taskInHistory.getID();

		Assertions.assertEquals(task.getName(), taskName);
		Assertions.assertEquals(task.getDescription(), taskDescription);
		Assertions.assertEquals(task.getStatus(), taskStatus);
		Assertions.assertEquals(task.getID(), taskID);
	}

	@Test
	void historyListShouldBeSize10() {
		TaskManager tsk = manager.getDefault();
		Task task1 = new Task("Task1", "Task", Status.NEW);
		Task task2 = new Task("Task2", "Task", Status.NEW);
		Task task3 = new Task("Task3", "Task", Status.NEW);
		Task task4 = new Task("Task4", "Task", Status.NEW);
		Task task5 = new Task("Task5", "Task", Status.NEW);
		Task task6 = new Task("Task6", "Task", Status.NEW);
		Task task7 = new Task("Task7", "Task", Status.NEW);
		Task task8 = new Task("Task8", "Task", Status.NEW);
		Task task9 = new Task("Task9", "Task", Status.NEW);
		Task task10 = new Task("Task10", "Task", Status.NEW);
		Task task11 = new Task("Task11", "Task", Status.NEW);
		tsk.addTask(task1);
		tsk.addTask(task2);
		tsk.addTask(task3);
		tsk.addTask(task4);
		tsk.addTask(task5);
		tsk.addTask(task6);
		tsk.addTask(task7);
		tsk.addTask(task8);
		tsk.addTask(task9);
		tsk.addTask(task10);
		tsk.addTask(task11);
		tsk.getTask(0);
		tsk.getTask(1);
		tsk.getTask(2);
		tsk.getTask(3);
		tsk.getTask(4);
		tsk.getTask(5);
		tsk.getTask(6);
		tsk.getTask(7);
		tsk.getTask(8);
		tsk.getHistory();
		tsk.getTask(9);
		tsk.getHistory();
		tsk.getTask(10);
		tsk.getHistory();
		int size = 10;
		Assertions.assertEquals(tsk.getHistory().size(), size);
	}

	@Test
	void historyListShouldChangeContentAfterSize10() {
		TaskManager tsk = manager.getDefault();
		Task task1 = new Task("Task1", "Task", Status.NEW);
		Task task2 = new Task("Task2", "Task", Status.NEW);
		Task task3 = new Task("Task3", "Task", Status.NEW);
		Task task4 = new Task("Task4", "Task", Status.NEW);
		Task task5 = new Task("Task5", "Task", Status.NEW);
		Task task6 = new Task("Task6", "Task", Status.NEW);
		Task task7 = new Task("Task7", "Task", Status.NEW);
		Task task8 = new Task("Task8", "Task", Status.NEW);
		Task task9 = new Task("Task9", "Task", Status.NEW);
		Task task10 = new Task("Task10", "Task", Status.NEW);
		Task task11 = new Task("Task11", "Task", Status.NEW);
		tsk.addTask(task1);
		tsk.addTask(task2);
		tsk.addTask(task3);
		tsk.addTask(task4);
		tsk.addTask(task5);
		tsk.addTask(task6);
		tsk.addTask(task7);
		tsk.addTask(task8);
		tsk.addTask(task9);
		tsk.addTask(task10);
		tsk.addTask(task11);
		tsk.getTask(0);
		tsk.getTask(1);
		tsk.getTask(2);
		tsk.getTask(3);
		tsk.getTask(4);
		tsk.getTask(5);
		tsk.getTask(6);
		tsk.getTask(7);
		tsk.getTask(8);
		tsk.getHistory();
		tsk.getTask(9);
		tsk.getHistory();
		tsk.getTask(10);
		tsk.getHistory();
		Assertions.assertNotEquals(task1, tsk.getHistory().get(0));
	}
}