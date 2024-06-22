package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import server.adapters.*;
import server.handlers.*;
import server.tokens.TasksListTypeToken;
import taskmanager.*;
import taskpackage.Status;
import taskpackage.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
	public static final int PORT = 8080;
	private final HttpServer httpServer;
	private final TaskManager taskManager;
	private final Gson gson = new GsonBuilder()
		.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		.registerTypeAdapter(Duration.class, new DurationAdapter())
		.create();
	private final String start = String.format("HTTP-сервер запущен на %s порту", PORT);
	private final String stop = String.format("HTTP-сервер на %s порту остановлен", PORT);

	public HttpTaskServer(TaskManager manager) throws IOException {
		this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
		this.taskManager = manager;
	}

	public Gson getGson() {
		return gson;
	}

	public void start() {
		httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
		httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
		httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
		httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
		httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
		httpServer.start();
		System.out.println(start);
	}

	public void stop() {
		httpServer.stop(0);
		System.out.println(stop);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		TaskManager manager = Managers.getDefault();
		HttpTaskServer server = new HttpTaskServer(manager);
		Gson gson1 = server.getGson();

		manager.deleteTaskList();
		manager.deleteSubtaskList();
		manager.deleteEpicList();

		server.start();

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

		List<Task> taskListFromResponse = gson1.fromJson(response.body(), new TasksListTypeToken().getType());

		System.out.println("taskListFromResponse = " + taskListFromResponse);
		System.out.println(response.statusCode());

		server.stop();
	}
}