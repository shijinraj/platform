package car.hey.platform.exception;

/**
 * This class provides the error codes and basic description about the error
 * 
 * @author Shijin Raj
 * 
 */
public enum ErrorCodes {

	/**
	 * Below are the error code and description
	 */
	INVALID_REQUESTS(10001, "invalid_request"), UNAUTHORIZED(10002, "unauthorized"), // The security header verification
																						// failed.
	NOT_FOUND(10003, "not_found"), // The API Resource was not found; incorrect resource path or HTTP Verb.
	INVALID_HTTP_VERB(10004, "invalid_http_verb"), // The HTTP Verb provided on the URL is not valid for the resource.
	UNSUPPORTED_MEDIA_TYPE(10005, "unsupported_media_type"), // The content-type supplied on the request is not valid
																// for the resource.
	INTERNAL_ERROR(10006, "internal_error"), // The server is currently unavailable, or a severe error has occurred.
												// Please try again.
	SERVICE_UNAVIALABLE(10007, "service_unavailable"), // The API is currently unavailable. Please try again.
	CONNECTION_TIMEOUT(10008, "connection_timeout");// The API gateway has experienced a connection timeout.

	/**
	 * error code
	 */
	private Integer code;

	/**
	 * error type
	 */
	private String type;

	/**
	 * @param code
	 * @param type
	 */
	ErrorCodes(Integer code, String type) {
		this.code = code;
		this.type = type;
	}

	/**
	 * @return
	 */
	public Integer code() {
		return code;
	}

	/**
	 * @return
	 */
	public String type() {
		return type;
	}

}
