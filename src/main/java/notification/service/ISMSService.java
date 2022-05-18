package notification.service;

import notification.exceptions.NotificationServiceException;

public interface ISMSService {
    Boolean sendSMS(String text, String phoneNumber) throws NotificationServiceException;
}
