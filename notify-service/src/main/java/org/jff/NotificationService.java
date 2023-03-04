package org.jff;

import org.jff.Entity.NotificationEvent;

public interface NotificationService {
    void sendNotification(NotificationEvent notificationEvent) throws Exception;
}
