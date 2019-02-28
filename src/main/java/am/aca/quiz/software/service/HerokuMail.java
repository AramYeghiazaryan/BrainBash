package am.aca.quiz.software.service;

import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HerokuMail {


    //TODO ASHXATACNEL VAHE JAN
    public void sendEmail(String fromEmail, String toEmail, String subject, String text) throws IOException {
        try {
            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
            Request request = new Request();
            request.setMethod( Method.POST);
            request.setEndpoint( "mail/send");
            request.setBody("{\"personalizations\":[{\"to\":[{\"email\":\""
                +toEmail+"\"}],\"subject\":\""
                +subject+"\"}],\"from\":{\"email\":\""
                +fromEmail+"\"},\"content\":[{\"type\":\"text/plain\",\"value\": \""
                +text+"\"}]}");

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
