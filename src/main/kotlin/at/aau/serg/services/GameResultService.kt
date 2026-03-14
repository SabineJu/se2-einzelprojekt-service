package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class GameResultService {

    private val gameResults = mutableListOf<GameResult>() // Liste, in der alle GameResult Objekte gespeichert werden
    private var nextId = AtomicLong(1) // AtomicLong erzeugt fortlaufende IDs (1,2,3,...)

    fun addGameResult(gameResult: GameResult) {     // Fügt ein neues Spielergebnis hinzu
        gameResult.id = nextId.getAndIncrement()    // Setzt eine neue eindeutige ID für das GameResult
        gameResults.add(gameResult)                 // Fügt das Ergebnis zur Liste hinzu
    }

    fun getGameResult(id: Long): GameResult? = gameResults.find { it.id == id } // ? allows null // Sucht ein GameResult anhand der ID // find durchsucht die Liste nach dem ersten Element mit passender ID // wenn nichts gefunden wird -> null
    //GameResult? bedeutet durch das ?, dass es auch null sein kann
    fun getGameResults(): List<GameResult> = gameResults.toList() // returns immutable list copy // Gibt alle gespeicherten Ergebnisse zurück // toList() erstellt eine neue unveränderbare Kopie der Liste // Dadurch kann der Controller die Liste nicht verändern

    /**
     * Kotlin-idiomatic for:
     * fun deleteGameResult(gameResultId: Long) {
     *     gameResults.removeIf({ gameResult -> gameResult.id == gameResultId })
     * }
     */
    fun deleteGameResult(id: Long) = gameResults.removeIf { it.id == id }  // Löscht ein Ergebnis anhand der ID // removeIf entfernt alle Elemente aus der Liste, bei denen die Bedingung true ist

}