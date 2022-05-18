package notification.service;

import notification.exceptions.NotificationServiceException;

public interface IEmailService {
    Boolean sendEmail(String text, String toEmail) throws NotificationServiceException;
}
