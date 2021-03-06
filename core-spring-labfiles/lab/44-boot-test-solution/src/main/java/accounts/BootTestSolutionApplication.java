package accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

import config.AppConfig;

/**
 * Runs the Account Server.
 */
@SpringBootApplication
@Import(AppConfig.class)
@EntityScan("rewards.internal")
public class BootTestSolutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootTestSolutionApplication.class, args);
	}

}
