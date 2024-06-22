package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.*;
import server.Endpoint;
import taskpackage.*;
import taskmanager.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.Endpoint.getEndpoint;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
	private final TaskManager manager;
	private final Gson gson;

	public TasksHandler(TaskManager manager, Gson gson) {
		this.manager = manager;
		this.gson = gson;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Endpoint endpoint = getEndpoint(exchange.getRequestMethod());

		switch (endpoint) {
			case GET:
				handleGet(exchange);
				break;
			case POST:
				handlePost(exchange);
				break;
			case DELETE:
				handleDelete(exchange);
				break;
			default:
				sendNoTaskError(exchange, "Неверный запрос.");

		}
	}

	public void handleGet(HttpExchange exchange) throws IOException {
		try {
			String[] path = exchange.getRequestURI().getPath().split("/");
			if (path.length <= 2) {
				System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);
				if (manager.taskList().isEmpty()) {
					sendNoTaskError(exchange, "Задачи отсутствуют.");
				} else {
					sendSuccess(exchange, gson.toJson(manager.taskList()));
				}
			} else {
				System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
				Task task = manager.getTask(Integer.parseInt(path[2]));
				if (task != null) {
					sendSuccess(exchange, gson.toJson(task));
				} else {
					sendNoTaskError(exchange, "Задача с ID: " + path[2] + " отсутствует.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}


	public void handlePost(HttpExchange exchange) throws IOException {
		try {
		String[] path = exchange.getRequestURI().getPath().split("/");

		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		Task task = gson.fromJson(body, Task.class);
		if (path.length <= 2) {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);
			try {
				manager.addTask(task);
				sendPostSuccess(exchange);
			} catch (TimeCrossingException timeCrossingException) {
				sendTimeCrossingError(exchange, timeCrossingException.getMessage());
			}
		} else {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
			try {
				task.setId(Integer.parseInt(path[2]));
				manager.updateTask(task);
				sendPostSuccess(exchange);
			} catch (TimeCrossingException timeCrossingException) {
				sendTimeCrossingError(exchange, timeCrossingException.getMessage());
			} catch (TaskNotFoundException nullException) {
				sendNoTaskError(exchange, "Задача с ID: " + path[2] + " отсутствует.");
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}

	public void handleDelete(HttpExchange exchange) throws IOException {
		try {
		String[] path = exchange.getRequestURI().getPath().split("/");
		System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
		try {
			manager.removeTask(Integer.parseInt(path[2]));
			sendSuccess(exchange, "Задача удалена.");
		} catch (TaskNotFoundException nullException) {
			sendNoTaskError(exchange, "Задача с ID: " + path[2] + " отсутствует.");
		}
		} catch (Exception e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}
}