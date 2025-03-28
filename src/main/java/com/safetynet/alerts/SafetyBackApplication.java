package com.safetynet.alerts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyBackApplication {

	private static final Logger logger = LogManager.getLogger(SafetyBackApplication.class);

	public static void main(String[] args) {
		logger.info("✅ Lancement de l'application SafetyBack...");
		SpringApplication.run(SafetyBackApplication.class, args);
		logger.info("🚀 Application démarrée avec succès !");
	}
}
