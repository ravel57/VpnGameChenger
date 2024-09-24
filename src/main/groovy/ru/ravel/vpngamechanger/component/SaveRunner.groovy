package ru.ravel.vpngamechanger.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import lombok.RequiredArgsConstructor
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.ravel.vpngamechanger.model.IpParams

@Component
@RequiredArgsConstructor
class SaveRunner implements CommandLineRunner {

	private static Set<IpParams> ipParams = new HashSet<>()
	private final static String saveFileName = "saved_data.json"

	@Override
	void run(String... args) throws Exception {
		ObjectMapper mapper = new ObjectMapper()
		File file = new File(saveFileName)
		if (file.exists()) {
			CollectionType type = mapper.getTypeFactory().constructCollectionType(Set.class, IpParams.class)
			ipParams = mapper.readValue(file.readLines().join("\n"), type)
		} else {
			file.write(mapper.writeValueAsString(ipParams))
		}
	}

	static Set<IpParams> getIpParams() {
		return ipParams
	}

	static void addIpParams(IpParams ipParams) {
		this.ipParams.add(ipParams)
		ObjectMapper mapper = new ObjectMapper()
		File file = new File(saveFileName)
		file.write(mapper.writeValueAsString(this.ipParams))
	}

	static void removeIpParams(IpParams ipParams) {
		this.ipParams.remove(ipParams)
		ObjectMapper mapper = new ObjectMapper()
		File file = new File(saveFileName)
		file.write(mapper.writeValueAsString(this.ipParams))
	}
}