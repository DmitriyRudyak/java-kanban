import exceptions.ManagerSaveException;
import taskmanager.*;
import taskpackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
	@Test
	void shouldSaveAndLoadEmptyFile() throws IOException {
		File taskStorage = File.createTempFile("data", ".csv");
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
		File taskStorage = File.createTempFile("data", ".csv");
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
		File taskStorage = File.createTempFile("data", ".csv");
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

	@Test
	void shouldSaveAndLoadTaskClocksFromFile() throws IOException {
		File taskStorage = File.createTempFile("data", ".csv");
		FileBackedTaskManager taskManager = new  FileBackedTaskManager(taskStorage.getPath());


		Task taskOne = new Task("First", "...", Status.NEW, 30, "2010-10-10T20:20");
		taskManager.addTask(taskOne);

		System.out.println(taskStorage.getPath());
		FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(taskStorage);

		LocalDateTime timeFromFile = taskManagerSecond.getTask(taskOne.getId()).getStartTime();
		Assertions.assertEquals(timeFromFile, taskOne.getStartTime());
		taskStorage.deleteOnExit();
	}

	@Test
	void testException() throws IOException {
		File taskStorage = File.createTempFile("data", ".csv");
		FileBackedTaskManager taskManager = new  FileBackedTaskManager(taskStorage.getPath());

		Task taskOne = new Task("First", "...", Status.NEW, 30, "2010-10-10T20:20");
		taskManager.addTask(taskOne);

		System.out.println(taskStorage.getPath());
		assertThrows(ManagerSaveException.class, () -> {
			FileBackedTaskManager taskManagerSecond = FileBackedTaskManager.loadFromFile(new File("load"));
			LocalDateTime timeFromFile = taskManagerSecond.getTask(taskOne.getId()).getStartTime();
		});

		taskStorage.deleteOnExit();
	}
}