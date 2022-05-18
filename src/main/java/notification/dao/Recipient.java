package notification.dao;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Recipient {
    String email;
    String phoneNumber;
}
