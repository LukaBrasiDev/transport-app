package pl.lukabrasi.transportapp.service;
import pl.lukabrasi.transportapp.dto.DatabaseChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener
    public void handleDatabaseChangeEvent(DatabaseChangeEvent event) {
        messagingTemplate.convertAndSend("/topic/changes", "Aktualizacja: " + event.getChangeDescription());
    }
}