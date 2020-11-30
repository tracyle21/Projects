package coms309.teambeans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
public class TeambeansApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeambeansApplication.class, args);
	}

}
