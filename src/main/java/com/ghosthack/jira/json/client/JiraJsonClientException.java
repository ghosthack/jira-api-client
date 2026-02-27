package com.ghosthack.jira.json.client;

/**
 * Exception thrown by JIRA JSON API client operations.
 * Wraps underlying transport, parsing, and JIRA error responses.
 */
public class JiraJsonClientException extends Exception {

    private static final long serialVersionUID = 2L;

    private final int statusCode;

    public JiraJsonClientException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public JiraJsonClientException(Throwable cause) {
        super(cause);
        this.statusCode = -1;
    }

    public JiraJsonClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public JiraJsonClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code associated with this exception,
     * or -1 if not applicable.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Creates an exception for an unexpected HTTP response status.
     *
     * @param expectedStatus the expected HTTP status code
     * @param actualStatus the actual HTTP status code received
     * @return a new JiraJsonClientException with status details
     */
    public static JiraJsonClientException createResponseStatusException(
            int expectedStatus, int actualStatus) {
        return new JiraJsonClientException(
                "Unexpected response status: expected " + expectedStatus
                        + " but received " + actualStatus, actualStatus);
    }

    /**
     * Creates an exception for an unexpected HTTP response status (legacy compatibility).
     *
     * @param actualStatus the actual HTTP status code received
     * @return a new JiraJsonClientException with status details
     */
    public static JiraJsonClientException createResponseStatusException(int actualStatus) {
        return createResponseStatusException(200, actualStatus);
    }

    @Override
    public String toString() {
        if (statusCode > 0) {
            return "JiraJsonClientException{statusCode=" + statusCode
                    + ", message=" + getMessage() + "}";
        }
        return super.toString();
    }
}
