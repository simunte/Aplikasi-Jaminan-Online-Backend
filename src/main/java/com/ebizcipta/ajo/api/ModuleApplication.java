package com.ebizcipta.ajo.api;

import io.swagger.models.auth.In;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@SpringBootApplication
public class ModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(ModuleApplication.class, args);
	}
}
