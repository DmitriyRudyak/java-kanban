package TaskManager;

import TaskPackage.Status;
import TaskPackage.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
	private static InMemoryTaskManager tsk;

	@BeforeAll
	static void beforeAll() {
		tsk = new InMemoryTaskManager();
		Task task1 = new Task("Task1", "Task1", Status.NEW);
		Task task2 = new Task("Task1", "Task1", Status.NEW);
		Task task3 = new Task("Task1", "Task1", Status.NEW);
		Task task4 = new Task("Task1", "Task1", Status.NEW);
		Task task5 = new Task("Task1", "Task1", Status.NEW);
		Task task6 = new Task("Task1", "Task1", Status.NEW);
		Task task7 = new Task("Task1", "Task1", Status.NEW);
		Task task8 = new Task("Task1", "Task1", Status.NEW);
		Task task9 = new Task("Task1", "Task1", Status.NEW);
		Task task10 = new Task("Task1", "Task1", Status.NEW);
		Task task11 = new Task("Task1", "Task1", Status.NEW);
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
	}

	@Test
	void listShouldWork() {
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
	}
}