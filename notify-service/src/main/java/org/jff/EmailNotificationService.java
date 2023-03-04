package org.jff;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.NotificationEvent;
import org.jff.client.UserServiceClient;
import org.jff.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService{

    private final JavaMailSender mailSender;

    private final UserServiceClient userServiceClient;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendNotification(NotificationEvent notificationEvent) throws Exception {

        Long recipientId = notificationEvent.getRecipientId();
        Integer type = notificationEvent.getType();
        if(type==NotificationEvent.ACTIVATION){
            log.info("Activation notification");
            UserVO activatingUser = userServiceClient.getUserInfoList(List.of(recipientId)).get(0);

            log.info("activatingUser {}",activatingUser);
            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(activatingUser.getEmail());
            mailMessage.setSubject("Your activation code");
            mailMessage.setText("Your activation code is "+notificationEvent.getContent());

            // Sending the mail
            mailSender.send(mailMessage);


        }


//        Long publisherId = notificationEvent.getPublisherId();
//
//        List<Long> userIdList = new ArrayList<>();
//        userIdList.add(recipientId);
//        userIdList.add(publisherId);
//
//        List<UserVO> userVOList = userServiceClient.getUserInfoList(userIdList);
//
//
//
//
//        // Creating a simple mail message
//        SimpleMailMessage mailMessage
//                = new SimpleMailMessage();
//
//        EmailDetails details = new EmailDetails();
//
//        log.info("sender {}",sender);
//        log.info("email {}",details);
//        // Setting up necessary details
//        mailMessage.setFrom(sender);
//        mailMessage.setTo(details.getRecipient());
//        mailMessage.setText(details.getMsgBody());
//        mailMessage.setSubject(details.getSubject());
//
//        // Sending the mail
//        mailSender.send(mailMessage);

    }
}
