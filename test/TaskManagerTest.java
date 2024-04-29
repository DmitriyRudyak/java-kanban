import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.Managers;
import taskmanager.TaskManager;
import taskpackage.Epic;
import taskpackage.Status;
import taskpackage.Subtask;
import taskpackage.Task;

import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
		@Test
		void ifTaskIDisSameThenTaskIsSame() {
			Task sameTask1 = new Task("Task","Task", Status.NEW, 0);
			Task sameTask2 = new Task("Task","Task", Status.NEW, sameTask1.getId());
			Assertions.assertEquals(sameTask1, sameTask2);
		}

		@Test
		void ifSubTaskIDisSameThenTaskIsSame() {
			Subtask sameTask1 = new Subtask("Subtask","Task", Status.NEW, 0);
			Subtask sameTask2 = new Subtask("Subtask","Task", Status.NEW, sameTask1.getId());
			Assertions.assertEquals(sameTask1, sameTask2);
		}

		@Test
		void ifEpicIDisSameThenTaskIsSame() {
			Epic sameTask1 = new Epic("Epic", 0);
			Epic sameTask2 = new Epic("Epic", sameTask1.getId());
			Assertions.assertEquals(sameTask1, sameTask2);
		}

		@Test
		void epicCantBeSubtask() {
			TaskManager tsk = Managers.getDefault();
			Epic epic = new Epic("Epic");
			tsk.addEpic(epic);
			/*tsk.addSubtask(epic);*/		//Программа выдает ошибку несовместимости типов, запрещая данный подход к методу
		}

		@Test
		void subtaskCantBeEpic() {
			TaskManager tsk = Managers.getDefault();
			Subtask subtask = new Subtask("Subtask", "subtask", Status.NEW, 0);
			/*tsk.addEpic(subtask);*/		//Программа выдает ошибку несовместимости типов, запрещая данный подход к методу
		}

		@Test
		void managersCreatesInitialisedTaskManagerSamples() {
			TaskManager taskManager = Managers.getDefault();

			Task taskOne = new Task("Task");
			taskManager.addTask(taskOne);
			taskManager.getTask(taskOne.getId());
			List<Task> history = taskManager.getHistory();
			List<Task> historyManual = new ArrayList<>();
			historyManual.add(taskOne);

			Assertions.assertEquals(history, historyManual);
		}

		@Test
		void taskManagerCanWorkWithID() {
			TaskManager tskManager = Managers.getDefault();
			Task task = new Task("Task", "Task", Status.NEW);
			tskManager.addTask(task);
			int id = task.getId();
			Assertions.assertEquals(task, tskManager.getTask(id));

			Epic epic = new Epic("Epic");
			tskManager.addEpic(epic);
			id = epic.getId();
			Assertions.assertEquals(epic, tskManager.getEpic(id));

			Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, id);
			tskManager.addSubtask(subtask);
			id = subtask.getId();
			Assertions.assertEquals(subtask, tskManager.getSubtask(id));
		}

		@Test
		void taskManagerCanWorkWithManualID() {
			TaskManager tskManager = Managers.getDefault();
			Task task = new Task("Task", "Task", Status.NEW);
			tskManager.addTask(task);
			Task updateTask = new Task("TaskNew", "TaskNew", Status.NEW, 0);
			tskManager.updateTask(updateTask);
			Assertions.assertEquals(updateTask, tskManager.getTask(0));
		}

		@Test
		void taskShouldBeUnchangedAfterAddToManager() {
			TaskManager tskManager = Managers.getDefault();
			Task task = new Task("Task", "Task", Status.NEW);
			tskManager.addTask(task);
			int id = task.getId();

			String taskName = tskManager.getTask(id).getName();
			String taskDescription = tskManager.getTask(id).getDescription();
			Status taskStatus = tskManager.getTask(id).getStatus();
			int taskID = tskManager.getTask(id).getId();

			Assertions.assertEquals(task.getName(), taskName);
			Assertions.assertEquals(task.getDescription(), taskDescription);
			Assertions.assertEquals(task.getStatus(), taskStatus);
			Assertions.assertEquals(task.getId(), taskID);
		}

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
		void deletedSubTasksShouldNotHaveID() {
			TaskManager tskManager = Managers.getDefault();
			Epic epic = new Epic("Epic", 0);
			tskManager.addEpic(epic);
			Subtask subtask = new Subtask("Subtask", "...", Status.NEW, epic.getId());

			tskManager.addSubtask(subtask);
			tskManager.removeSubtask(subtask);

			Assertions.assertEquals(epic.getSubTaskIDList().size(), 0);
		}

		@Test
		void setterChangesAllContent() {
			TaskManager tskManager = Managers.getDefault();
			Epic epic = new Epic("Epic");
			tskManager.addEpic(epic);
			Subtask subtaskOne = new Subtask("Subtask", "...", Status.NEW, epic.getId());
			tskManager.addSubtask(subtaskOne);

			Assertions.assertEquals(epic.getStatus(), Status.NEW);

			subtaskOne.setStatus(Status.DONE);
			tskManager.setEpicStatus(epic);
			Assertions.assertEquals(epic.getStatus(), Status.DONE);
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
