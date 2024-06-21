package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import server.adapters.*;
import server.handlers.*;
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

public class HttpTaskServer {
	static final int PORT = 8080;
	private final HttpServer httpServer;
	private final TaskManager taskManager;
	private final Gson gson = new GsonBuilder()
		.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		.registerTypeAdapter(Duration.class, new DurationAdapter())
		.create();


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
		System.out.println("HTTP-сервер запущен на " + PORT + " порту");
	}

	public void stop() {
		System.out.println("HTTP-сервер на " + PORT + " порту остановлен");
		httpServer.stop(0);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		TaskManager manager = Managers.getDefault();
		HttpTaskServer server = new HttpTaskServer(manager);
		Gson gson1 = server.getGson();

		manager.deleteTaskList();
		manager.deleteSubtaskList();
		manager.deleteEpicList();

		server.start();

		Task taskWithTime = new Task("Time", " ", Status.NEW, 90, "2024-10-10T20:20");
		manager.addTask(taskWithTime);

		Task taskWithTime2 = new Task("Time2", " ", Status.NEW, 90, "2024-10-10T20:20");

		String taskJson = gson1.toJson(taskWithTime2);

		HttpClient client = HttpClient.newHttpClient();
		URI url = URI.create("http://localhost:8080/tasks/1");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.statusCode());
		System.out.println(manager.getTask(1));

		Task taskWithTime3 = new Task("Time3", " ", Status.NEW, 90, "2024-10-10T20:20");

		taskJson = gson1.toJson(taskWithTime3);
		url = URI.create("http://localhost:8080/tasks/1");
		request = HttpRequest.newBuilder()
				.uri(url)
				.POST(HttpRequest.BodyPublishers.ofString(taskJson))
				.build();

		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.statusCode());
		System.out.println(manager.getTask(1));

		server.stop();
	}
}