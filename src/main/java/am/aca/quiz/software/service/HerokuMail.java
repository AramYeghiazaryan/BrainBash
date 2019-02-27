package am.aca.quiz.software.service;

import com.sendgrid.Email;

import java.io.IOException;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HerokuMail {


    public void sendEmail(String fromEmail, String toEmail,String subject,String text) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("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"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
