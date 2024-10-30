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

	private static final def ipRegex = /^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})$/
	private final Logger logger = LoggerFactory.getLogger(this.class)


	static boolean isIpAddress(String ip) {
		return ip ==~ ipRegex
	}


	/**
	 * For Linux
	 * @param rawHeader
	 * @param line
	 */
	ParsedProcess(String[] rawHeader, String[] line) {
		String[] split = line[rawHeader.findIndexOf { it == 'NAME' }].split('->')
		if (split.length > 1) {
			def ipPort = split[1].split(':')
			this.ip = ipPort[0]
			if (ip != null && isIpAddress(ip) && ip != '0.0.0.0' && ip != '127.0.0.1') {
				this.port = ipPort[1]
				this.command = line[rawHeader.findIndexOf { it == 'COMMAND' }]
				this.user = line[rawHeader.findIndexOf { it == 'USER' }]
			} else {
				this.ip = null
			}
		}
	}


	/**
	 * For Windows
	 * @param line
	 * @param pids
	 */
	ParsedProcess(String[] line, List<String[]> pids) {
		try {
			if (line.length >= 6) {
				def ipPort = line[3].split(':')
				if (ipPort.length >= 2) {
					this.ip = ipPort[0]
					if (ip != null && isIpAddress(ip) && ip != '0.0.0.0' && ip != '127.0.0.1') {
						this.port = ipPort[1]
						this.command = pids.find { it.length >= 2 && it[1] == line[5] }?[0] ?: ""
					} else {
						this.ip = null
					}
				}
			}
		} catch (e) {
			logger.error(e.message, e)
		}
	}


	@Override
	String toString() {
		return "ParsedProcess{" +
				"command='" + command + '\'' +
				", ip='" + ip + '\'' +
				", port='" + port + '\'' +
				", user='" + user + '\'' +
				'}';
	}


	boolean equals(o) {
		if (this.is(o)) return true
		if (o == null || getClass() != o.class) return false

		ParsedProcess that = (ParsedProcess) o

		if (command != that.command) return false
		if (ip != that.ip) return false
		if (logger != that.logger) return false
		if (port != that.port) return false
		if (user != that.user) return false

		return true
	}


	int hashCode() {
		int result
		result = (command != null ? command.hashCode() : 0)
		result = 31 * result + (ip != null ? ip.hashCode() : 0)
		result = 31 * result + (port != null ? port.hashCode() : 0)
		result = 31 * result + (user != null ? user.hashCode() : 0)
		result = 31 * result + (logger != null ? logger.hashCode() : 0)
		return result
	}

}