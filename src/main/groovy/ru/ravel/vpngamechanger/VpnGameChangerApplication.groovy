package ru.ravel.vpngamechanger

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class VpnGameChangerApplication {

	static void main(String[] args) {
		SpringApplication.run(VpnGameChangerApplication, args)
	}

}
