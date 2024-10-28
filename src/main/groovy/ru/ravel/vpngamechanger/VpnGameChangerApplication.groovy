package ru.ravel.vpngamechanger

import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import ru.ravel.vpngamechanger.serice.ConsoleParserService

@SpringBootApplication
@EnableScheduling
@EnableAsync
class VpnGameChangerApplication {

	@Autowired
	ConsoleParserService consoleParserService


	static void main(String[] args) {
		SpringApplication.run(VpnGameChangerApplication, args)
	}


	@PreDestroy
	void onDestroy() {
		consoleParserService.shutdown()
	}

}