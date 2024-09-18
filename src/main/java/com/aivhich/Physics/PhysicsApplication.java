package com.aivhich.Physics;

import com.aivhich.Physics.auth.AuthenticationService;
import com.aivhich.Physics.auth.dto.request.RegisterRequest;
import com.aivhich.Physics.auth.models.states.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PhysicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhysicsApplication.class, args);
	}
	/*@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.name("ivan")
					.surname("prohorevich")
					.username("Admin")
					.email("aivhich@yandex.ru")
					.password("APPLaNg8129&@*@)!")
					.role(Role.ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.username("Manager")
					.email("itech.ivanprohorevich@gmail.com")
					.password("ApplAng1248%(@0!")
					.role(Role.MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());
		};
	}*/
}
