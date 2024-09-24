package ru.ravel.vpngamechanger.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ravel.vpngamechanger.model.IpParams
import ru.ravel.vpngamechanger.model.Name
import ru.ravel.vpngamechanger.serice.ConsoleParserService

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
class ApiController {

	private final ConsoleParserService consoleParser

	ApiController(ConsoleParserService consoleParser) {
		this.consoleParser = consoleParser
	}

	@PostMapping("/get-processes-by-name")
	ResponseEntity<Object> getProcessesByName(@RequestBody Name name) {
		return ResponseEntity.ok().body(consoleParser.getConsoleInfoByName(name.name))
	}

	@PostMapping("/send-ip-params")
	ResponseEntity<Object> setIpParams(@RequestBody IpParams ipParams) {
		return ResponseEntity.ok().body(consoleParser.setIpParams(ipParams))
	}

	@GetMapping("/get-routing-ip-params")
	ResponseEntity<Object> getRoutingIpParams() {
		return ResponseEntity.ok().body(consoleParser.getRoutingIpParams())
	}

	@PostMapping("/delete-ip-params")
	ResponseEntity<Object> deleteIpParams(@RequestBody IpParams ipParams) {
		consoleParser.deleteIpParams(ipParams)
		return ResponseEntity.ok().build()
	}

}
