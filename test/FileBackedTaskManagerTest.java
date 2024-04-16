import taskmanager.*;
import taskpackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest {
	@Test
	void shouldSaveAndLoadEmptyFile() throws IOException {
		File taskStorage = File.createTempFile("storage", ".csv", new File("./test/resources"));
		FileBackedTaskManager taskManager = new  FileBackedTaskManager(taskStorage.getPath());

		Task taskOne = new Task("First", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.removeTask(taskOne.getId());

		System.out.println(taskStorage.getPath());
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(0, taskManagerSecond.taskList().size());

		taskStorage.deleteOnExit();
	}

	@Test
	void shouldSaveAndLoadFileWithData() throws IOException {
		File taskStorage = File.createTempFile("storage", ".csv", new File("./test/resources"));
		FileBackedTaskManager taskManager = new  FileBackedTaskManager(taskStorage.getPath());

		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);

		System.out.println(taskStorage.getPath());
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(4, taskManagerSecond.taskList().size());
		taskStorage.deleteOnExit();
	}

	@Test
	void shouldSaveAndLoadHistoryFromFile() throws IOException {
		File taskStorage = File.createTempFile("storage", ".csv", new File("./test/resources"));
		FileBackedTaskManager taskManager = new  FileBackedTaskManager(taskStorage.getPath());


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

		System.out.println(taskStorage.getPath());
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		Assertions.assertEquals(2, taskManagerSecond.getHistory().size());
		taskStorage.deleteOnExit();
	}
}