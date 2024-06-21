package serverTests;

import taskmanager.*;
import taskpackage.*;
import server.*;
import server.tokens.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

class HttpServerTaskTest {
	TaskManager manager = Managers.getDefault();
	HttpTaskServer server = new HttpTaskServer(manager);
	Gson gson = server.getGson();

	HttpServerTaskTest() throws IOException {
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
	public void shouldGetTaskList() throws IOException, InterruptedException {
		Task task = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		Task task1 = new Task("Task1", "Task1", Status.NEW, 90, "2023-10-10T20:20");
		Task task2 = new Task("Task2", "Task2", Status.NEW, 90, "2022-10-10T20:20");
		manager.addTask(task);
		manager.addTask(task1);
		manager.addTask(task2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Task> taskListFromResponse = gson.fromJson(response.body(), new TasksListTypeToken().getType());

		assertEquals(manager.taskList(), taskListFromResponse);
	}

	@Test
	public void shouldGetTaskByID() throws IOException, InterruptedException {
		Task task = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		Task task1 = new Task("Task1", "Task1", Status.NEW, 90, "2023-10-10T20:20");
		Task task2 = new Task("Task2", "Task2", Status.NEW, 90, "2022-10-10T20:20");
		manager.addTask(task);
		manager.addTask(task1);
		manager.addTask(task2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/tasks/" + task1.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		Task taskFromResponse = gson.fromJson(response.body(), Task.class);

		assertEquals(task1, taskFromResponse);
	}

	@Test
	public void shouldDeleteTask() throws IOException, InterruptedException {
		Task task = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		Task task1 = new Task("Task1", "Task1", Status.NEW, 90, "2023-10-10T20:20");
		Task task2 = new Task("Task2", "Task2", Status.NEW, 90, "2022-10-10T20:20");
		manager.addTask(task);
		manager.addTask(task1);
		manager.addTask(task2);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Task> tasksFromManager = manager.taskList();

		assertEquals(200, response.statusCode());
		assertEquals(2, tasksFromManager.size());
	}

	@Test
	public void shouldAddNewTaskToManager() throws IOException, InterruptedException {
		Task task = new Task("TaskNew", "TaskNew", Status.NEW, 90, "2028-10-10T20:20");
		String taskJson = gson.toJson(task);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.taskList().size());
	}

	@Test
	public void shouldUpdateTask() throws IOException, InterruptedException {
		Task task = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		manager.addTask(task);
		Task taskNew = new Task("TaskNew", "TaskNew", Status.NEW, 90, "2024-10-10T20:20");
		String taskJson = gson.toJson(taskNew);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.taskList().size());
		assertEquals(taskNew, manager.getTask(task.getId()));
	}

	@Test
	public void shouldMakeTimeCrossingError() throws IOException, InterruptedException {
		Task task2 = new Task("Task", "Task", Status.NEW, 90, "2024-10-10T20:20");
		manager.addTask(task2);

		Task task = new Task("TaskNew", "TaskNew", Status.NEW, 90, "2024-10-10T20:40");
		String taskJson = gson.toJson(task);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/tasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(406, response.statusCode());
		assertEquals(1, manager.taskList().size());
	}
}