import notification.NotificationService;
import notification.dao.Message;
import notification.dao.Recipient;
import notification.exceptions.NotificationServiceException;
import notification.service.IEmailService;
import static org.easymock.EasyMock.*;

import notification.service.ILoggingService;
import notification.service.ISMSService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private IEmailService emailService;
    private ILoggingService loggingService;
    private ISMSService smsService;

    @BeforeEach
    public void SetUp(){
        emailService = mock(IEmailService.class);
        loggingService = mock(ILoggingService.class);
        smsService = mock(ISMSService.class);
        notificationService = new NotificationService(emailService,smsService,loggingService);

    }

    @Test
    public void NoEmailButSmsWasSent() {
        // Arrange
        Recipient recipient = new Recipient("mail@mail.com","118811");
        Message message = new Message("Hi Mark",recipient);

        // recording phase
        try {
            expect(emailService.sendEmail(anyString(),anyString())).andReturn(false);
            expect(smsService.sendSMS(message.getText(),message.getRecipient().getPhoneNumber())).andReturn(true);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
        }


        replay(emailService,smsService);


        // test
        try {
            notificationService.notifyAbout(message);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
            Assertions.fail();
        }

        // verify
        verify();

    }

    @Test
    public void EmailSentAndNoSms(){
        // Arrange
        Recipient recipient = new Recipient("mail@mail.com","118811");
        Message message = new Message("Hi Mark",recipient);

        // recording phase
        try {
            expect(emailService.sendEmail(message.getText(),message.getRecipient().getEmail())).andReturn(true);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
        }

        replay(emailService,smsService);

        // test
        try {
            notificationService.notifyAbout(message);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
            Assertions.fail();
        }

        // verify
        verify();

    }

    @Test
    public void EmailExceptionAndSmsSent(){
        // Arrange
        Recipient recipient = new Recipient("mail@mail.com","118811");
        Message message = new Message("Hi Mark",recipient);

        // recording phase
        try {
            expect(emailService.sendEmail(anyString(),anyString())).andThrow(new NotificationServiceException(""));
            expect(smsService.sendSMS(message.getText(),message.getRecipient().getPhoneNumber())).andReturn(true);
            loggingService.log("There was an error sending the email, no message was sent","");
            expectLastCall();
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
        }

        replay(emailService,smsService,loggingService);

        // test
        try {
            notificationService.notifyAbout(message);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
            Assertions.fail();
        }

        // verify
        verify();
    }

    @Test
    public void SmsExceptionEmailException(){
        // Arrange
        Recipient recipient = new Recipient("mail@mail.com","118811");
        Message message = new Message("Hi Mark",recipient);


        try {
            expect(emailService.sendEmail(anyString(),anyString())).andThrow(new NotificationServiceException("email"));
            expect(smsService.sendSMS(anyString(),anyString())).andThrow(new NotificationServiceException("sms"));
            loggingService.log("There was an error sending the email, no message was sent","email");
            expectLastCall();
            loggingService.log("There was an error sending the sms, no message was sent","sms");
            expectLastCall();
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
        }

        replay(emailService,smsService,loggingService);

        // test
        try {
            notificationService.notifyAbout(message);
        } catch (NotificationServiceException exception) {
            exception.printStackTrace();
            Assertions.assertEquals("Recipient could not be notified",exception.getMessage());
        }
    }
}
