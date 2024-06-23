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


public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
	private final TaskManager manager;
	private final Gson gson;

	public SubtasksHandler(TaskManager manager, Gson gson) {
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
			if (manager.subtaskList().isEmpty()) {
				sendNoTaskError(exchange, "Подзадачи отсутствуют.");
			} else {
				sendSuccess(exchange, gson.toJson(manager.subtaskList()));
			}
		} else {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
			Subtask subtask = manager.getSubtask(Integer.parseInt(path[2]));
			if (subtask != null) {
				sendSuccess(exchange, gson.toJson(subtask));
			} else {
				sendNoTaskError(exchange, "Подзадача с ID: " + path[2] + " отсутствует.");
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}

	public void handlePost(HttpExchange exchange) throws IOException {
		try {
		String[] path = exchange.getRequestURI().getPath().split("/");

		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		Subtask subtask = gson.fromJson(body, Subtask.class);
		if (path.length <= 2) {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);
			try {
				manager.addSubtask(subtask);
				sendPostSuccess(exchange);
			} catch (TimeCrossingException timeCrossingException) {
				sendTimeCrossingError(exchange, timeCrossingException.getMessage());
			}
		} else {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
			try {
				subtask.setId(Integer.parseInt(path[2]));
				manager.updateSubtask(subtask);
				sendPostSuccess(exchange);
			} catch (TimeCrossingException timeCrossingException) {
				sendTimeCrossingError(exchange, timeCrossingException.getMessage());
			} catch (TaskNotFoundException nullException) {
				sendNoTaskError(exchange, "Подзадача с ID: " + path[2] + " отсутствует.");
			}
		}
		} catch (Throwable e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}

	public void handleDelete(HttpExchange exchange) throws IOException {
		try {
		String[] path = exchange.getRequestURI().getPath().split("/");
		System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
		try {
			manager.removeSubtask(Integer.parseInt(path[2]));
			sendSuccess(exchange, "Подзадача удалена.");
		} catch (TaskNotFoundException nullException) {
			sendNoTaskError(exchange, "Подзадача с ID: " + path[2] + " отсутствует.");
		}
		} catch (Throwable e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}
}