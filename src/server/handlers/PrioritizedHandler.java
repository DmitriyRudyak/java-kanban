package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Endpoint;
import taskmanager.*;

import java.io.IOException;
import java.util.Objects;

import static server.Endpoint.getEndpoint;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

	private final TaskManager manager;
	private final Gson gson;

	public PrioritizedHandler(TaskManager manager, Gson gson) {
		this.manager = manager;
		this.gson = gson;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
		Endpoint endpoint = getEndpoint(exchange.getRequestMethod());
		String[] path = exchange.getRequestURI().getPath().split("/");
		System.out.println("Обработка запроса " + exchange.getRequestMethod() + " /" + path[1]);

		if (Objects.requireNonNull(endpoint) == Endpoint.GET) {
			sendSuccess(exchange, gson.toJson(manager.getPrioritizedTasks()));
		} else {
			sendNoTaskError(exchange, "Неверный запрос.");
		}
		} catch (Throwable e) {
			e.printStackTrace();
			sendServerError(exchange);
		}
	}
}