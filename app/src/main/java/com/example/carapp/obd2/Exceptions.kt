package com.example.carapp.obd2

abstract class BadObdResponseException(private val response: String) : RuntimeException() {
    companion object {
        fun check(response: String) {
            when {
                response.contains("BUS INIT... ERROR") -> throw BusInitObdException(response)
                response.contains("NO DATA") -> throw NoDataObdException(response)
                response.contains("?") -> throw UnknownCommandObdException(response)
                response.contains("STOPPED") -> throw StoppedObdException(response)
                response.contains("UNABLE TO CONNECT") -> throw UnableToConnectObdException(response)
                response.contains("CAN ERROR") -> throw CanErrorObdException(response)
                response.contains("ERROR") -> throw UnknownErrorObdException(response)
            }
        }
    }

    override fun toString(): String {
        return "BadObdResponseException(response='$response')"
    }

}

class StoppedObdException(response: String) : BadObdResponseException(response) {

}

class BusInitObdException(response: String) : BadObdResponseException(response) {

}

class NoDataObdException(response: String) : BadObdResponseException(response) {

}
class UnableToConnectObdException(response: String) : BadObdResponseException(response) {

}

class UnknownCommandObdException(response: String) : BadObdResponseException(response) {

}

class CanErrorObdException(response: String) : BadObdResponseException(response) {

}

class UnknownErrorObdException(response: String) : BadObdResponseException(response) {

}

