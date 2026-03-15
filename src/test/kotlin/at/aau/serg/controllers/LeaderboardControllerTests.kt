package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import org.springframework.web.server.ResponseStatusException
import kotlin.test.assertEquals

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock(GameResultService::class.java)
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res = controller.getLeaderboard(rank = null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])   // Score 20
        assertEquals(second, res[1])  // Score 15
        assertEquals(third, res[2])   // Score 10
    }

    @Test
    fun test_getLeaderboard_sameScore_correctTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second, third))

        val res = controller.getLeaderboard(rank = null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)

        // gleiche Punkte → Zeit entscheidet
        assertEquals(second, res[0])  // Zeit 10.0
        assertEquals(third, res[1])   // Zeit 15.0
        assertEquals(first, res[2])   // Zeit 20.0
    }

    @Test
    fun test_getLeaderboard_rank_returnsNeighbors() {
        val r1 = GameResult(1, "p1", 100, 10.0)
        val r2 = GameResult(2, "p2", 90, 20.0)
        val r3 = GameResult(3, "p3", 80, 30.0)
        val r4 = GameResult(4, "p4", 70, 40.0)
        val r5 = GameResult(5, "p5", 60, 50.0)
        val r6 = GameResult(6, "p6", 50, 60.0)
        val r7 = GameResult(7, "p7", 40, 70.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(r1, r2, r3, r4, r5, r6, r7))

        val res = controller.getLeaderboard(rank = 4)

        verify(mockedService).getGameResults()

        // Index 4 → Spieler 4 + 3 darüber + 3 darunter
        // Hier: start = 0, end = 7 → alle Spieler
        assertEquals(7, res.size)
        assertEquals(r1, res[0])
        assertEquals(r4, res[3])
        assertEquals(r7, res[6])
    }

    @Test
    fun test_getLeaderboard_rank_topOfList() {
        val r1 = GameResult(1, "p1", 100, 10.0)
        val r2 = GameResult(2, "p2", 90, 20.0)
        val r3 = GameResult(3, "p3", 80, 30.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(r1, r2, r3))

        val res = controller.getLeaderboard(rank = 1)

        // Rank = 1 → nur Start = 0, End = min(3, 4) → Spieler 1-3
        assertEquals(3, res.size)
        assertEquals(r1, res[0])
        assertEquals(r2, res[1])
        assertEquals(r3, res[2])
    }

    @Test
    fun test_getLeaderboard_rank_negative_throwsException() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(rank = -1)
        }

        verify(mockedService).getGameResults()
    }

    @Test
    fun test_getLeaderboard_rank_tooLarge_throwsException() {
        val r1 = GameResult(1, "p1", 100, 10.0)
        whenever(mockedService.getGameResults()).thenReturn(listOf(r1))

        assertThrows<ResponseStatusException> {
            controller.getLeaderboard(rank = 5)
        }

        verify(mockedService).getGameResults()
    }
}