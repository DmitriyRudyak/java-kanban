import taskmanager.*;
import taskpackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileBackedTaskManagerTest {
	@Test
	void shouldSaveAndLoadEmptyFile() {
		File taskStorage = new File("./test/resources/storage.csv");
		FileBackedTaskManager taskManager = new  FileBackedTaskManager("./test/resources/storage.csv");

		Task taskOne = new Task("First", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.removeTask(taskOne.getId());
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(0, taskManagerSecond.taskList().size());
	}

	@Test
	void shouldSaveAndLoadFileWithData() {
		FileBackedTaskManager taskManager = new  FileBackedTaskManager("./test/resources/storage.csv");
		File taskStorage = new File("./test/resources/storage.csv");

		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(4, taskManagerSecond.taskList().size());
	}

	@Test
	void shouldSaveAndLoadHistoryFromFile() {
		FileBackedTaskManager taskManager = new  FileBackedTaskManager("./test/resources/storage.csv");
		File taskStorage = new File("./test/resources/storage.csv");

		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);
		taskManager.getTask(taskOne.getId());
		taskManager.getTask(taskFour.getId());

		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(2, taskManagerSecond.getHistory().size());
	}
}