package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
    ): List<GameResult> {

        // 1️⃣ Sortierung: Score absteigend, bei Gleichstand Zeit aufsteigend
        val leaderboard = gameResultService.getGameResults()
            .sortedWith(
                compareByDescending<GameResult> { it.score }
                    .thenBy { it.time }
            )

        // 2️⃣ Wenn kein rank angegeben → gesamte Liste
        if (rank == null) return leaderboard

        // 3️⃣ rank validieren
        if (rank <= 0 || rank > leaderboard.size) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid rank parameter"
            )
        }

        // 4️⃣ Indexberechnung (Rank beginnt bei 1, Liste bei 0)
        val index = rank - 1

        // 5️⃣ 3 Spieler über und 3 Spieler unter dem Rank
        val start = maxOf(0, index - 3)
        val end = minOf(leaderboard.size, index + 4)

        return leaderboard.subList(start, end)
    }
}