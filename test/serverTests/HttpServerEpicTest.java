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

class HttpServerEpicTest {
	TaskManager manager = Managers.getDefault();
	HttpTaskServer server = new HttpTaskServer(manager);
	Gson gson = server.getGson();

	HttpServerEpicTest() throws IOException {
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
	public void shouldGetEpicsList() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		Epic epic2 = new Epic("Epic2");
		Epic epic3 = new Epic("Epic3");
		manager.addEpic(epic);
		manager.addEpic(epic2);
		manager.addEpic(epic3);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Epic> epicsFromResponse = gson.fromJson(response.body(), new EpicsListTypeToken().getType());

		assertEquals(manager.epicList(), epicsFromResponse);
	}

	@Test
	public void shouldGetEpicByID() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/epics/" + epic.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		Task taskFromResponse = gson.fromJson(response.body(), Epic.class);

		assertEquals(epic, taskFromResponse);
	}

	@Test
	public void shouldDeleteEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		Subtask subtask2 = new Subtask("Subtask2", "Subtask2", Status.NEW, 30, "2025-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		manager.addSubtask(subtask2);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.DELETE()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		List<Epic> epicsFromManager = manager.epicList();
		List<Subtask> subtasksFromManager = manager.subtaskList();

		assertEquals(200, response.statusCode());
		assertEquals(0, epicsFromManager.size());
		assertEquals(0, subtasksFromManager.size());
	}

	@Test
	public void shouldAddNewEpicToManager() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");

		String epicJson = gson.toJson(epic);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/epics");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(epicJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.epicList().size());
	}

	@Test
	public void shouldUpdateEpic() throws IOException, InterruptedException {
		Epic epic = new Epic("Epic");
		manager.addEpic(epic);
		Subtask subtask = new Subtask("Subtask", "Subtask", Status.NEW, 30, "2024-10-10T20:20", epic.getId());
		manager.addSubtask(subtask);
		Epic epicNew = new Epic("EpicNew");
		String taskJson = gson.toJson(epicNew);

		HttpClient client = HttpClient.newHttpClient();
		URI uri = URI.create("http://localhost:8080/epics/" + epic.getId());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(201, response.statusCode());
		assertEquals(1, manager.epicList().size());
		assertEquals("EpicNew", manager.getEpic(epic.getId()).getName());
	}
}