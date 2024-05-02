import taskmanager.*;
import taskpackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HistoryManagerTest {
	@Test
	void taskInHistoryManagerIsUnchanged() {
		TaskManager tskManager = Managers.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int id = task.getId();
		tskManager.getTask(id);

		Task taskInHistory = tskManager.getHistory().get(0);

		String taskName = taskInHistory.getName();
		String taskDescription = taskInHistory.getDescription();
		Status taskStatus = taskInHistory.getStatus();
		int taskID = taskInHistory.getId();

		Assertions.assertEquals(task.getName(), taskName);
		Assertions.assertEquals(task.getDescription(), taskDescription);
		Assertions.assertEquals(task.getStatus(), taskStatus);
		Assertions.assertEquals(task.getId(), taskID);
	}

	@Test
	void taskShouldBeAddedToHistory() {
		TaskManager tskManager = Managers.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int id = task.getId();
		tskManager.getTask(id);

		tskManager.getTask(id);

		Assertions.assertEquals(tskManager.getHistory().size(), 1);
	}

	@Test
	void taskShouldBeDeletedFromHistory() {
		TaskManager tskManager = Managers.getDefault();
		Task task = new Task("Task", "Task", Status.NEW);
		tskManager.addTask(task);
		int id = task.getId();
		tskManager.getTask(id);

		tskManager.getTask(id);
		tskManager.removeTask(id);

		Assertions.assertEquals(tskManager.getHistory().size(), 0);
	}

	@Test
	void historyShouldBeEmpty() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		taskManager.getTask(taskThree.getId());
		taskManager.getTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().get(0), taskOne);
		taskManager.removeTask(taskOne.getId());
		taskManager.removeTask(taskTwo.getId());
		taskManager.removeTask(taskThree.getId());
		taskManager.removeTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().size(), 0);
	}

	@Test
	void historyNodeShouldBeDeletedFromBeginning() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		taskManager.getTask(taskThree.getId());
		taskManager.getTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().get(0), taskOne);
		taskManager.removeTask(taskOne.getId());

		Assertions.assertNotEquals(taskManager.getHistory().get(0), taskOne);
	}

	@Test
	void historyNodeShouldBeDeletedFromMiddle() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		taskManager.getTask(taskThree.getId());
		taskManager.getTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().get(2), taskThree);
		taskManager.removeTask(taskThree.getId());

		Assertions.assertNotEquals(taskManager.getHistory().get(2), taskThree);
	}

	@Test
	void historyNodeShouldBeDeletedFromEnd() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		taskManager.getTask(taskThree.getId());
		taskManager.getTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().get(3), taskFour);
		taskManager.removeTask(taskOne.getId());

//		Assertions.assertNull(taskManager.getHistory().get(3));		//IndexOutOfBoundsException
	}

	@Test
	void historyNodeShouldBeRenewed() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskTwo.getId());
		taskManager.getTask(taskThree.getId());
		taskManager.getTask(taskFour.getId());

		Assertions.assertEquals(taskManager.getHistory().get(1), taskTwo);

		taskManager.getTask(taskTwo.getId());
		Assertions.assertNotEquals(taskManager.getHistory().get(1), taskTwo);
		Assertions.assertEquals(taskManager.getHistory().get(3), taskTwo);
	}
}