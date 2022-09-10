package app.ideea.secretdiary

data class Note(val date: String, val message: String) {
    override fun toString(): String {
        return "$date\n$message"
    }
}