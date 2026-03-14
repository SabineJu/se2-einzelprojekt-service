package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException



@RestController // Diese Klasse verarbeitet HTTP Requests und gibt JSON zurück
@RequestMapping("/leaderboard") // Alle Endpunkte dieser Klasse beginnen mit /leaderboard; Beispiel: http://localhost:8080/leaderboard
class LeaderboardController(
    private val gameResultService: GameResultService // Spring injiziert automatisch den GameResultService (Dependency Injection); Der Controller kann damit auf gespeicherte Spielergebnisse zugreifen
) {

    @GetMapping
    fun getLeaderboard(
        // optionaler Query Parameter
        @RequestParam(required = false) rank: Int?
    ): List<GameResult> {

        // Leaderboard sortieren
        // 1. Score absteigend
        // 2. Zeit aufsteigend (kürzer = besser)
        val leaderboard = gameResultService.getGameResults()
            .sortedWith(
                compareByDescending<GameResult> { it.score }
                    .thenBy { it.time }
            )

        // wenn kein rank übergeben wurde → ganze Liste
        if (rank == null) {
            return leaderboard
        }

        // rank validieren
        if (rank <= 0 || rank > leaderboard.size) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid rank parameter"
            )
        }

        // rank beginnt bei 1 → Liste beginnt bei 0
        val index = rank - 1

        // 3 über + 3 unter berechnen
        val start = maxOf(0, index - 3)
        val end = minOf(leaderboard.size, index + 4)

        return leaderboard.subList(start, end)
    }
}






 /*   @GetMapping
    fun getLeaderboard(): List<GameResult> = // Dieser Endpunkt reagiert auf HTTP GET Requests
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.id })) // Holt zuerst alle GameResults aus dem Service; sortiert die Liste mit einer Vergleichsfunktion; compareBy erstellt einen Comparator mit mehreren Kriterien

        // erstes Sortierkriterium:
        // -it.score -> minus bedeutet absteigend sortieren
        // also: höherer Score kommt zuerst
        //{ -it.score },

    // zweites Sortierkriterium (Tiebreaker):
    // wenn Score gleich ist, wird nach ID sortiert
    // kleinere ID kommt zuerst
    //{ it.id }
