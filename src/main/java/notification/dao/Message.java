package notification.dao;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Message {
    String text;
    Recipient recipient;
}
