package server;

public enum Endpoint {
	GET,
	POST,
	DELETE,
	UNKNOWN;

	public static Endpoint getEndpoint(String method) {
		switch (method) {
			case "GET" : return Endpoint.GET;
			case "POST" : return Endpoint.POST;
			case "DELETE" : return Endpoint.DELETE;
			default : return Endpoint.UNKNOWN;
		}
	}
}