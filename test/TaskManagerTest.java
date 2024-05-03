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
	void deletedSubTasksShouldNotHaveID() {
		TaskManager tskManager = Managers.getDefault();
		Epic epic = new Epic("Epic", 0);
		tskManager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "...", Status.NEW, epic.getId());

		tskManager.addSubtask(subtask);
		tskManager.removeSubtask(subtask.getId());

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
	void shouldDeleteSubtask() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Sub", "Sub", epic.getId());
		taskManager.addSubtask(subtask);

		Assertions.assertEquals(1, taskManager.subtaskList().size());
		taskManager.removeSubtask(subtask.getId());
		Assertions.assertEquals(0, taskManager.subtaskList().size());
		Assertions.assertEquals(0, epic.getSubTaskIDList().size());
	}

	@Test
	void shouldDeleteTaskList() {
		TaskManager taskManager = Managers.getDefault();
		Task taskOne = new Task("First", "...", Status.NEW);
		Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);
		Task taskThree = new Task("Third", "...", Status.NEW);
		Task taskFour = new Task("Fourth", "...", Status.NEW);
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		taskManager.addTask(taskThree);
		taskManager.addTask(taskFour);

		taskManager.deleteTaskList();
		Assertions.assertEquals(0, taskManager.taskList().size());
	}

	@Test
	void shouldDeleteEpic() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);

		taskManager.removeEpic(epic.getId());
		Assertions.assertEquals(0, taskManager.epicList().size());
	}

	@Test
	void shouldUpdateEpic() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Sub", "Sub", epic.getId());
		taskManager.addSubtask(subtask);

		Epic epicNew = new Epic("EpicNew", epic.getId());
		taskManager.updateEpic(epicNew);

		Assertions.assertEquals(epic.getId(), taskManager.getEpic(epicNew.getId()).getId());
	}

	@Test
	void shouldDeleteEpicList() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		Epic epicNew = new Epic("EpicNew");
		taskManager.addEpic(epic);
		taskManager.addEpic(epicNew);

		taskManager.deleteEpicList();
		Assertions.assertEquals(0, taskManager.epicList().size());
	}

	@Test
	void shouldReturnSubtaskList() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Sub", "Sub", epic.getId());
		Subtask subtask2 = new Subtask("Sub2", "Sub2", epic.getId());
		taskManager.addSubtask(subtask);
		taskManager.addSubtask(subtask2);

		Assertions.assertEquals(2, taskManager.subtaskList().size());
	}

	@Test
	void shouldUpdateSubtask() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Sub", "Sub",Status.NEW, epic.getId());
		Subtask subtask2 = new Subtask("Sub2", "Sub2",Status.DONE, epic.getId());
		taskManager.addSubtask(subtask);

		taskManager.updateSubtask(subtask2);

		Assertions.assertEquals(Status.DONE, taskManager.getSubtask(subtask2.getId()).getStatus());
	}

	@Test
	void shouldDeleteSubtaskList() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Sub", "Sub", epic.getId());
		Subtask subtask2 = new Subtask("Sub2", "Sub2", epic.getId());
		taskManager.addSubtask(subtask);
		taskManager.addSubtask(subtask2);

		taskManager.deleteSubtaskList();

		Assertions.assertEquals(0, taskManager.subtaskList().size());
	}
}
