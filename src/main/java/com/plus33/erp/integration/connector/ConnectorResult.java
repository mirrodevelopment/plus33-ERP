package com.plus33.erp.integration.connector;

public class ConnectorResult {
    private boolean success;
    private String responsePayload;
    private String errorMessage;

    public ConnectorResult(boolean success, String responsePayload, String errorMessage) {
        this.success = success;
        this.responsePayload = responsePayload;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public String getResponsePayload() { return responsePayload; }
    public String getErrorMessage() { return errorMessage; }
}