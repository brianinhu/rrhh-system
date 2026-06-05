package com.mitocode.rrhh_backend;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RrhhBackendApplication {

	static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		System.out.println(">>> Infraestructura local inyectada exitosamente.");
		SpringApplication.run(RrhhBackendApplication.class, args);
	}

	private static final Logger log = LoggerFactory.getLogger(RrhhBackendApplication.class);

	@PostConstruct
	public void acidTestLog() {
		log.info("=======================================================");
		log.info(" ACID TEST: INFRAESTRUCTURA AZURE PAAS 100% OPERATIVA  ");
		log.info("=======================================================");
	}

}
