package serverTests;

import taskmanager.*;
import taskpackage.*;
import server.*;
import server.tokens.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerPrioritizedTest {
	TaskManager manager = Managers.getDefault();
	HttpTaskServer server = new HttpTaskServer(manager);
	Gson gson = server.getGson();

	HttpServerPrioritizedTest() throws IOException {
	}

	@BeforeEach
	public void BeforeEach() {
		manager.deleteTaskList();
		manager.deleteSubtaskList();
		manager.deleteEpicList();
		server.start();
	}

	@AfterEach
	public void AfterEach() {
		server.stop();
	}

	@Test
	public void shouldGetPriorityList() throws IOException, InterruptedException {
		Task task = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		Task task1 = new Task("Task1", "Task1", Status.NEW, 90, "2023-10-10T20:20");
		Task task2 = new Task("Task2", "Task2", Status.NEW, 90, "2022-10-10T20:20");
		manager.addTask(task);
		manager.addTask(task1);
		manager.addTask(task2);
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2026-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/prioritized");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		TreeSet<Task> tasksFromManager = manager.getPrioritizedTasks();

		List<Task> tasksFromResponse = gson.fromJson(response.body(), new TasksListTypeToken().getType());

		assertEquals(200, response.statusCode());
		assertEquals(tasksFromResponse.size(), tasksFromManager.size());
	}
}
