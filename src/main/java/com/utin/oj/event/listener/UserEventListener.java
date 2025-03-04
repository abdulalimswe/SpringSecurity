package com.utin.oj.event.listener;

import com.utin.oj.event.UserEvent;
import com.utin.oj.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.utin.oj.enumeration.EventType.REGISTRATION;
import static com.utin.oj.enumeration.EventType.RESETPASSWORD;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event){
        switch(event.getType()){
            case REGISTRATION -> emailService.sendNewAccountEmail(event.getUser().getFirstName(),event.getUser().getEmail(),(String)event.getData().get("key"));
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(event.getUser().getFirstName(),event.getUser().getEmail(),(String)event.getData().get("key"));
            default -> {}
        }
    }
}
