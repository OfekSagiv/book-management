package com.ofeksag.book_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BookManagementApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		setMandatory("SECRET_KEY", dotenv);
		setMandatory("DB_USERNAME", dotenv);
		setMandatory("DB_PASSWORD", dotenv);
		setMandatory("DB_HOST", dotenv);
		setMandatory("DB_PORT", dotenv);
		setMandatory("DB_NAME", dotenv);
		setMandatory("DB_USER", dotenv);
		setMandatory("DB_PASS", dotenv);
		setOptional("PORT", dotenv); // לא חובה אם לא קבעת ידנית

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

	private static void setOptional(String key, Dotenv dotenv) {
		String value = dotenv.get(key);
		if (value != null) {
			System.setProperty(key, value);
		}
	}
}
