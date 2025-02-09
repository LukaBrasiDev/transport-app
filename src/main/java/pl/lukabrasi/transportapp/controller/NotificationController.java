package pl.lukabrasi.transportapp.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/change")
    @SendTo("/topic/changes")
    public String sendNotification(String message){
        return message;
    }
}


