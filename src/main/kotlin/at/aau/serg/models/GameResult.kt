package at.aau.serg.models

data class GameResult(
    var id: Long,
    var playerName: String,
    var score: Int,
    var timeInSeconds: Double
) {
    // einfacher Getter, damit Controller 'it.time' benutzen kann
    val time: Double
        get() = timeInSeconds
}