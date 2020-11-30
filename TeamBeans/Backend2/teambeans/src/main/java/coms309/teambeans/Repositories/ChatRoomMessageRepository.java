package coms309.teambeans.Repositories;

import coms309.teambeans.Models.ChatRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long> {
}
