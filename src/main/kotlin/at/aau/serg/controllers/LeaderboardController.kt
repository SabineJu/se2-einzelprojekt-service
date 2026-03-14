package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController // Diese Klasse verarbeitet HTTP Requests und gibt JSON zurück
@RequestMapping("/leaderboard") // Alle Endpunkte dieser Klasse beginnen mit /leaderboard; Beispiel: http://localhost:8080/leaderboard
class LeaderboardController(
    private val gameResultService: GameResultService // Spring injiziert automatisch den GameResultService (Dependency Injection); Der Controller kann damit auf gespeicherte Spielergebnisse zugreifen
) {

    @GetMapping
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
}