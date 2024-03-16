package ru.ravel.vpngamechanger.serice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ravel.vpngamechanger.record.ParsedProcess


@Service
class ConsoleParser {

	Logger logger = LoggerFactory.getLogger(this.class)

	@Scheduled(cron = '*/30 * * * * *')
	void getConsoleInfo() {
		try {
			Process process = new ProcessBuilder().command("lsof", "-nPi", "@${System.getenv("target_device")}").start()
			List<String[]> output = new ArrayList<>()
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
			String line
			while ((line = reader.readLine()) != null) {
				output.add(line.toString().replaceAll('\\s+', ' ').split(' '))
			}
			process.waitFor()

			List<ParsedProcess> list = new ArrayList<>()
			for (i in 1..<output.size()) {
				list.add(new ParsedProcess(output[0], output[i]))
			}
			println()
			println(list.findAll {it.command() == "steam"}.join('\n'))
		} catch (IOException e) {
			logger.error(e.message)
		} catch (InterruptedException e) {
			logger.error(e.message)
		}

	}

}
