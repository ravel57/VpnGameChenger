package ru.ravel.vpngamechanger.record

record ParsedProcess(
		String command,
		String ip,
		String port,
		String user
) {
	ParsedProcess(String[] rawHeader, String[] line) {
		def ipPort = line[rawHeader.findIndexOf { it == 'NAME' }].split('->')[1].split(':')
		this.ip= ipPort[0]
		this.port= ipPort[1]
		this.command= line[rawHeader.findIndexOf { it == 'COMMAND' }]
		this.user= line[rawHeader.findIndexOf { it == 'USER' }]
	}
}