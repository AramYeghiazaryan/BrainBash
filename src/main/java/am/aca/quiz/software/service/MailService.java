package am.aca.quiz.software.service;

import am.aca.quiz.software.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendText(String emailTo, String subject, String text) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public void sendActivationCode(String email, UserEntity userEntity) {

        if (!StringUtils.isEmpty(userEntity.getEmail())) {
            String message =
                "Hello," + userEntity.getName() + "\n" +
                    "Please, visit the following link: https://brainbash.herokuapp.com/user/activate/" +
                    userEntity.getActivationCode();
            sendText(email, "Activation", message);
        }
    }
}
