package com.plus33.erp.integration.eventmesh;

public interface MessageListener {
    void onMessage(CloudEvent event);
}