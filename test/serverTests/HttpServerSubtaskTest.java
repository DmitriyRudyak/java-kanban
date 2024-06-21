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

class HttpServerSubtaskTest {
	TaskManager manager = Managers.getDefault();
	HttpTaskServer server = new HttpTaskServer(manager);
	Gson gson = server.getGson();

	HttpServerSubtaskTest() throws IOException {
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
	public void shouldGetSubtaskList() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Subtask> subTasksFromResponse = gson.fromJson(response.body(), new SubtasksListTypeToken().getType());

		assertEquals(manager.subtaskList(), subTasksFromResponse);
	}

	@Test
	public void shouldGetSubtaskByID() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		Task taskFromResponse = gson.fromJson(response.body(), Subtask.class);

		assertEquals(subtask, taskFromResponse);
	}

	@Test
	public void shouldDeleteSubtask() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Subtask> subtasksFromManager = manager.subtaskList();

		assertEquals(200, response.statusCode());
		assertEquals(1, subtasksFromManager.size());
	}

	@Test
	public void shouldAddNewSubtaskToManager() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());

		String subtaskJson = gson.toJson(subtask);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.subtaskList().size());
	}

	@Test
	public void shouldUpdateSubtask() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		String taskJson = gson.toJson(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.subtaskList().size());
		assertEquals("Subtask2", manager.getSubtask(subtask.getId()).getName());
	}

	@Test
	public void shouldMakeTimeCrossingError() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2024-10-10T20:40", epic.getId());
		manager.addSubtask(subtask);
		String taskJson = gson.toJson(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/subtasks");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(406, response.statusCode());
		assertEquals(1, manager.subtaskList().size());
	}
}