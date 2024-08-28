package tr.gov.gib.fpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "tr.gov.gib")
public class FPosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FPosApplication.class, args);
	}

}
