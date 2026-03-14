package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.*

@RestController //RestController verarbeitet HTTP Requests
@RequestMapping("/game-results") // Alle Requests beginnen mit /game-results
class GameResultController(
    private val gameResultService: GameResultService  //injiziert automatisch den GameResultService
) {

    @GetMapping("/{gameResultId}") // HTTP GET Request für ein einzelnes GameResult
    fun getGameResult(@PathVariable gameResultId: Long): GameResult? { // PathVariable liest den Wert aus der URL
        return gameResultService.getGameResult(gameResultId);
    }

    @GetMapping
    fun getAllGameResults(): List<GameResult> { // HTTP GET Request für alle GameResults
        return gameResultService.getGameResults(); // ruft Service Methode auf und gibt alle Ergebnisse zurück
    }

    @PostMapping
    fun addGameResult(@RequestBody gameResult: GameResult) { // HTTP POST Request um ein neues Ergebnis zu erstellen
        gameResultService.addGameResult(gameResult) // JSON aus dem Request Body wird automatisch in ein GameResult Objekt umgewandelt
    }

    @DeleteMapping("/{gameResultId}")
    fun deleteGameResult(@PathVariable gameResultId: Long) { // HTTP DELETE Request um ein Ergebnis zu löschen
        gameResultService.deleteGameResult(gameResultId) // löscht das Ergebnis mit der angegebenen ID
    }
    
}