package coms309.teambeans.Repositories;

import coms309.teambeans.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public Message findFirstByOrderByIdDesc();
}
