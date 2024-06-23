import exceptions.TimeCrossingException;
import taskmanager.*;
import taskpackage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	@Test
	void shouldChangeEpicStatusCorrectly() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask", Status.NEW, epic.getId());
		Subtask subtask3 = new Subtask("Subtask3", "Subtask", Status.NEW, epic.getId());
		taskManager.addSubtask(subtask);
		taskManager.addSubtask(subtask2);
		taskManager.addSubtask(subtask3);

		Assertions.assertEquals(Status.NEW, epic.getStatus());
		subtask.setStatus(Status.DONE);
		Subtask subtask4 = new Subtask("Subtask4", "Subtask", Status.IN_PROGRESS, epic.getId());
		taskManager.addSubtask(subtask4);
		taskManager.setEpicStatus(epic);
		Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
		subtask2.setStatus(Status.DONE);
		subtask3.setStatus(Status.DONE);
		subtask4.setStatus(Status.DONE);
		taskManager.setEpicStatus(epic);
		Assertions.assertEquals(Status.DONE, epic.getStatus());
	}

	@Test
	void epicShouldHaveSubtask() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, epic.getId());
		taskManager.addSubtask(subtask);

		Assertions.assertEquals(1, epic.getSubTaskIDList().size());
	}

	@Test
	void subtaskShouldHaveEpic() {
		TaskManager taskManager = Managers.getDefault();
		Epic epic = new Epic("Epic");
		taskManager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, epic.getId());
		taskManager.addSubtask(subtask);

		Assertions.assertEquals(epic.getId(), subtask.getEpicID());
	}

	@Test
	void intervalShouldBeCrossCheckedCorrectly() {
		TaskManager taskManager = Managers.getDefault();
		Task taskWithTime = new Task("Time", " ", Status.NEW, 90, "2024-10-10T20:20");
		Task taskWithTime2 = new Task("Time2", " ", Status.NEW, 60, "2024-10-10T21:00");
		Task taskWithTime3 = new Task("Time3", " ", Status.NEW, 300, "2024-10-10T23:20");
		taskManager.addTask(taskWithTime);
		assertThrows(TimeCrossingException.class, () -> taskManager.addTask(taskWithTime2));
		taskManager.addTask(taskWithTime3);

		Assertions.assertEquals(2, taskManager.getPrioritizedTasks().size());
	}
}