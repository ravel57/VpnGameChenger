package ru.ravel.vpngamechanger.model

class IpParams {
	String gateWay
	String processName

	boolean equals(o) {
		if (this.is(o)) return true
		if (o == null || getClass() != o.class) return false
		IpParams ipParams = (IpParams) o
		if (gateWay != ipParams.gateWay) return false
		if (processName != ipParams.processName) return false
		return true
	}

	int hashCode() {
		int result
		result = (gateWay != null ? gateWay.hashCode() : 0)
		result = 31 * result + (processName != null ? processName.hashCode() : 0)
		return result
	}
}