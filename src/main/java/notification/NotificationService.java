package notification;

import notification.dao.Message;
import notification.service.IEmailService;
import notification.service.ILoggingService;
import notification.service.ISMSService;
import notification.exceptions.NotificationServiceException;


public class NotificationService {
	private IEmailService emailService;
	private ISMSService smsService;
	private ILoggingService loggingService;

	public NotificationService(IEmailService emailService, ISMSService smsService, ILoggingService loggingService) {
		this.emailService = emailService;
		this.smsService = smsService;
		this.loggingService = loggingService;
	}


	public void notifyAbout(Message message) throws NotificationServiceException {

		try {
			if(emailService.sendEmail(message.getText(),message.getRecipient().getEmail())){
				return;
			}
		}catch (NotificationServiceException exception){
			loggingService.log("There was an error sending the email, no message was sent",exception.getMessage());
		}

		try {
			if(smsService.sendSMS(message.getText(),message.getRecipient().getPhoneNumber())){
				return;
			};

		}catch (NotificationServiceException exception){
			loggingService.log("There was an error sending the sms, no message was sent",exception.getMessage());
		}

		throw new NotificationServiceException("Recipient could not be notified");

		// - send email
		// - send SMS if email does not work (sendEmail returns false)
		// - if exceptions occurs, log information about exception
	}
}

