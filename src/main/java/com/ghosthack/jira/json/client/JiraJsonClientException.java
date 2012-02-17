package com.ghosthack.jira.json.client;

public class JiraJsonClientException extends Exception {

    public JiraJsonClientException(Throwable e) {
        super(e);
    }

    public JiraJsonClientException(String mess) {
        super(mess);
    }

    public static JiraJsonClientException createResponseStatusException(
            int status) {
        return new JiraJsonClientException("Response status != 200; received: "
                + status);
    }

    private static final long serialVersionUID = 1L;

}
