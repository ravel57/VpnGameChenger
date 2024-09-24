package ru.ravel.vpngamechanger.serice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ravel.vpngamechanger.model.IpParams
import ru.ravel.vpngamechanger.model.ParsedProcess

import java.util.concurrent.TimeUnit


@Service
class ConsoleParserService {

	private final Set<IpParams> ipParams = new HashSet<>()
	private final Set<String> ipToRemove = new HashSet<>()
	private final Logger logger = LoggerFactory.getLogger(this.class)

	private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows")


	private static ArrayList<String[]> executeProcess(String[] processParams) {
		def process = new ProcessBuilder().command(processParams).start()
		def output = new ArrayList<String[]>()
		def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
		String line
		while ((line = reader.readLine()) != null) {
			output.add(line.toString().replaceAll('\\s+', ' ').split(' '))
		}
		process.waitFor()
		return output
	}


	Set<ParsedProcess> getConsoleInfoByName(String name) {
		try {
			if (isWindows) {
				def IpPorts = executeProcess("netstat", "-ano")
				def pids = executeProcess("tasklist")
				def list = IpPorts.collect {
					str -> new ParsedProcess(str, pids)
				}.findAll {
					it?.command?.toLowerCase()?.contains(name.toLowerCase())
				}
				ipToRemove.addAll(list.collect { it.ip.toString() })
				return list.toSet()
			} else {
				def target = System.getenv("target_device")
				def ipPorts = executeProcess("lsof", "-nPi", "@$target")
				List<ParsedProcess> list = new ArrayList<>()
				for (i in 1..<ipPorts.size()) {
					list.add(new ParsedProcess(ipPorts[0], ipPorts[i]))
				}
				ipToRemove.addAll(list.collect { it.ip })
				return list.findAll {
					it != null && it.command != null
				}.findAll {
					it.command.toLowerCase().contains(name.toLowerCase())
				}.toSet()
			}
		} catch (IOException e) {
			logger.error(e.message)
			throw new RuntimeException(e)
		} catch (InterruptedException e) {
			logger.error(e.message)
			throw new RuntimeException(e)
		}
	}


	void setIpParams(IpParams ipParams) {
		this.ipParams.add(ipParams)
	}


	@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
	@Async
	void process() {
		ipParams.forEach { ipParams ->
			getConsoleInfoByName(ipParams.processName).forEach {
				try {
					if (isWindows) {
						String[] processParams = ["route", "add", it.ip, "MASK", "255.255.255.255", ipParams.gateWay]
						executeProcess(processParams)
						println(processParams.join(" "))
					}
				} catch (e) {
					logger.error(e.message, e)
				}
			}
		}
		if (!ipParams.isEmpty()) {
			println()
		}
	}

	void shutdown() {
		println("shutting down...")
		ipToRemove.forEach {
			if (isWindows) {
				String[] params = ["route", "delete", it, "mask", "255.255.255.255"]
				executeProcess(params)
				println(params.join(" "))
			}
		}
	}

	Set<IpParams> getRoutingIpParams() {
		return ipParams
	}

	void deleteIpParams(IpParams ipParams) {
		this.ipParams.remove(ipParams)
	}
}