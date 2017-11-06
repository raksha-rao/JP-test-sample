package com.jptest.payments.fulfillment.testonia.model.money;

public class NotificationDTO {

    private String content;
    private String timeCreated;

    public NotificationDTO(String content, String timeCreated) {
        this.content = content;
        this.timeCreated = timeCreated;
    }

    public String getContent() {
        return content;
    }

    public String getTimeCreated() {
        return timeCreated;
    }
}
