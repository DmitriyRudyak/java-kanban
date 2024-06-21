package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import server.Endpoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

	protected Endpoint getEndpoint(String method) {
		switch (method) {
			case "GET" : return Endpoint.GET;
			case "POST" : return Endpoint.POST;
			case "DELETE" : return Endpoint.DELETE;
			default : return Endpoint.UNKNOWN;
		}
	}

	protected void sendSuccess(HttpExchange httpExchange, String text) throws IOException {
		byte[] response = text.getBytes(StandardCharsets.UTF_8);
		httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		httpExchange.sendResponseHeaders(200, response.length);
		httpExchange.getResponseBody().write(response);
		httpExchange.close();
	}

	protected void sendPostSuccess(HttpExchange httpExchange) throws IOException {
		httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		httpExchange.sendResponseHeaders(201, 0);
		httpExchange.getResponseBody().write("".getBytes(StandardCharsets.UTF_8));
		httpExchange.close();
	}

	protected void sendNoTaskError(HttpExchange httpExchange, String text) throws IOException {
		byte[] response = text.getBytes(StandardCharsets.UTF_8);
		httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		httpExchange.sendResponseHeaders(404, response.length);
		httpExchange.getResponseBody().write(response);
		httpExchange.close();
	}

	protected void sendTimeCrossingError(HttpExchange httpExchange, String text) throws IOException {
		byte[] response = text.getBytes(StandardCharsets.UTF_8);
		httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
		httpExchange.sendResponseHeaders(406, response.length);
		httpExchange.getResponseBody().write(response);
		httpExchange.close();
	}
}