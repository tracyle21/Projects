package coms309.teambeans.Repositories;

import coms309.teambeans.Models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
        public Conversation findFirstByOrderByIdDesc();
//        Conversation findById(Long id);
}
