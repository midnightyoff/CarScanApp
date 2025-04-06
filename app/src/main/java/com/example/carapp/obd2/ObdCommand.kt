package com.example.carapp.obd2

abstract class ObdCommand {
    abstract val mode: String
    abstract val pid: String
    val value: String
        get() = listOf(mode, pid).joinToString(" ")
    // TODO maybe change data type to ObdFrame
    abstract fun decode(data: List<String>): ObdResponse

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ObdCommand) return false
        return this.mode == other.mode && this.pid == other.pid
    }
    override fun hashCode(): Int {
        return 31 * mode.hashCode() + pid.hashCode()
    }
    override fun toString(): String { return value }
}
// TODO ObdCustomCommand надо как-то регистрировать команду и позволить передать decode
class ObdCustomCommand(override val mode: String, override val pid: String) : ObdCommand() {
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.joinToString())
    }
    companion object {
        fun getCommand(input: String): ObdCustomCommand {
            val cleaned = input.replace(" ", "").trim()
            require(cleaned.length >= 4) { "Command string must be at least 4 characters long" }
            val mode = cleaned.take(2)
            val pid = cleaned.drop(2)
            return ObdCustomCommand(mode, pid)
        }
    }
}