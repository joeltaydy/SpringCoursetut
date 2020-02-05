package rewards;

import config.RewardsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

// TODO-01 : Open pom.xml or build.gradle, look for TO-DO-01

// TODO-02 : In pom.xml or build.gradle, look for TO-DO-02

// TODO-03 : Turn the 'RewardsApplication' into a Spring Boot application
//           by adding an annotation.

// TODO-10 : Disable 'DataSource' auto-configuration
//           - Use 'exclude' attribute of '@SpringBootApplication'
//             excluding 'DataSourceAutoConfiguration' class
//           - Import 'RewardsConfig' class. (Think about why.)

// TODO-11 : Look in application.properties for the next step.

// TODO-12 : Follow the instruction in the lab document.
//           The section titled "Build and Run using Command Line tools".
@SpringBootConfiguration
@Import(RewardsConfig.class)
public class RewardsApplication {
	static final String SQL = "SELECT count(*) FROM T_ACCOUNT";
	
    final Logger logger
            = LoggerFactory.getLogger(RewardsApplication.class);
    
    public static void main(String[] args) {
		SpringApplication.run(RewardsApplication.class, args);
	}

    // TODO-05 : Move the SQL scripts from the
    //             `src/test/resources/rewards/testdb` to the
    //             `src/main/resources/`
    //  Why are you doing this?  See detailed instructions if you do not know.

    // TODO-06 : Setup a command line runner that will query count from
    //           T_ACCOUNT table and log the count to the console
    //           Use the SQL query and logger provided above.
    //           Use the JdbcTemplate bean that Spring Boot creates for you
    //           automatically.
    //
    // TODO-07   Enable full debugging in order to observe how Spring Boot
	//           performs its auto-configuration logic
	//           - Follow TO-DO-07 in application.properties
    //             then come back here.
    //           - Run the application
    //           - In the console output, find "CONDITIONS EVALUATION REPORT"
    //             What is the first positive match? REMEMBER its name.
	@Bean
	CommandLineRunner QueryAccountCountRunner(JdbcTemplate jdbcTemplate){

		Long nAccounts =jdbcTemplate.queryForObject(SQL, Long.class);
		logger.info("Number of accounts: {}" , nAccounts);
		return args -> System.out.println("Hello, there are "
				+ nAccounts
				+ " accounts");
	}
}
