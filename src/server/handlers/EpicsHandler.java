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

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
	private final TaskManager manager;
	private final Gson gson;

	public EpicsHandler(TaskManager manager, Gson gson) {
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
			default:
				sendNoTaskError(exchange, "Неверный запрос.");

		}
	}

	public void handleGet(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		if (path.length < 3) {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);
			if (manager.epicList().isEmpty()) {
				sendNoTaskError(exchange, "Эпики отсутствуют.");
			} else {
				sendSuccess(exchange, gson.toJson(manager.epicList()));
			}
		} else if (path.length == 3) {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
			Epic epic = manager.getEpic(Integer.parseInt(path[2]));
			if (epic != null) {
				sendSuccess(exchange, gson.toJson(epic));
			} else {
				sendNoTaskError(exchange, "Эпик с ID: " + path[2] + " отсутствует.");
			}
		} else {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2] + "/" + path[3]);
			try {
				sendSuccess(exchange, gson.toJson(manager.subtaskList(Integer.parseInt(path[2]))));
			} catch (TaskNotFoundException nullException) {
				sendNoTaskError(exchange, "Эпик с ID: " + path[2] + " отсутствует.");
			}
		}
	}

	public void handlePost(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		Epic epic = gson.fromJson(body, Epic.class);
		if (path.length < 3) {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);
			try {
				manager.addEpic(epic);
				sendPostSuccess(exchange);
			} catch (TimeCrossingException timeCrossingException) {
				sendTimeCrossingError(exchange, timeCrossingException.getMessage());
			}
		} else {
			System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
			try {
				epic.setId(Integer.parseInt(path[2]));
				manager.updateEpic(epic);
				sendPostSuccess(exchange);
			} catch (TaskNotFoundException nullException) {
				sendNoTaskError(exchange, "Эпик с ID: " + path[2] + " отсутствует.");
			}
		}
	}

	public void handleDelete(HttpExchange exchange) throws IOException {
		String[] path = exchange.getRequestURI().getPath().split("/");
		System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1] + "/" + path[2]);
		try {
			manager.removeEpic(Integer.parseInt(path[2]));
			sendSuccess(exchange, "Эпик удален.");
		} catch (TaskNotFoundException nullException) {
			sendNoTaskError(exchange, "Эпик с ID: " + path[2] + " отсутствует.");
		}
	}
}