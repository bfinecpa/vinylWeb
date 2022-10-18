package project.vinyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@EnableJpaAuditing
@SpringBootApplication
public class VinylApplication {

	public static void main(String[] args) {
		SpringApplication.run(VinylApplication.class, args);
	}

	@PostConstruct
	public void newMemberFor(){

	}

}
