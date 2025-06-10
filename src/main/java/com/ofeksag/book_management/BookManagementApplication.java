package com.ofeksag.book_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BookManagementApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		setMandatory("DB_NAME", dotenv);
		setMandatory("DB_USERNAME", dotenv);
		setMandatory("DB_PASSWORD", dotenv);

		String port = dotenv.get("PORT");
		if (port != null) {
			System.setProperty("PORT", port);
		}

		SpringApplication.run(BookManagementApplication.class, args);
	}

	private static void setMandatory(String key, Dotenv dotenv) {
		String value = dotenv.get(key);
		if (value == null) {
			System.err.println("❌ Missing required environment variable: " + key);
			System.exit(1);
		}
		System.setProperty(key, value);
	}
}
