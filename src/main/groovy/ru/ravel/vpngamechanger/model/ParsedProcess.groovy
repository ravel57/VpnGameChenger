package ru.ravel.vpngamechanger.model

import lombok.Data
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Data
class ParsedProcess {
	String command
	String ip
	String port
	String user

	static boolean isIpAddress(String ip) {
		return ip ==~ ipRegex
	}

	private static final def ipRegex = /^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})$/
	private final Logger logger = LoggerFactory.getLogger(this.class)


	ParsedProcess(String[] rawHeader, String[] line) {
		def ipPort = line[rawHeader.findIndexOf { it == 'NAME' }].split('->')[1].split(':')
		this.ip = ipPort[0]
		this.port = ipPort[1]
		this.command = line[rawHeader.findIndexOf { it == 'COMMAND' }]
		this.user = line[rawHeader.findIndexOf { it == 'USER' }]
	}


	ParsedProcess(String[] line, List<String[]> pids) {
		try {
			if (line.length >= 6) {
				def ipPort = line[3].split(':')
				if (ipPort.length >= 2) {
					this.ip = ipPort[0]
					if (ip != null && isIpAddress(ip) && ip != '0.0.0.0' && ip != '127.0.0.1') {
						this.port = ipPort[1]
						this.command = pids.find { it.length >= 2 && it[1] == line[5] }?[0] ?: ""
					}
				}
			}
		} catch (e) {
			logger.error(e.message, e)
		}
	}


}