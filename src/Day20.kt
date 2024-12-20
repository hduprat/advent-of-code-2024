import java.util.*
import kotlin.math.absoluteValue

fun disc(center: IntVector, radius: Int): Set<IntVector> {
    val result = mutableSetOf(center)

    (-radius..radius).forEach { y ->
        (-radius..radius).forEach { x ->
            val p = x to y
            if (p.norm <= radius) result.add(center + p)
        }
    }

    return result
}

class UniquePair<T>(val first: T, val second: T) {
    constructor(p: Pair<T, T>) : this(p.first, p.second)

    override fun equals(other: Any?): Boolean {
        if (other !is UniquePair<*>) return false
        if (first == other.first) return second == other.second
        if (first == other.second) return second == other.first
        return false
    }

    operator fun component1(): T = first
    operator fun component2(): T = second
    override fun toString(): String = "${first to second}"
    override fun hashCode(): Int = Objects.hash(setOf(first, second))
}

class Racetrack(input: List<String>, private val cheatDuration: Int) {
    private lateinit var start: IntVector
    private lateinit var end: IntVector

    private val walls = mutableSetOf<IntVector>()
    private val track = mutableMapOf<IntVector, Int>()

    private fun buildTrack(start: IntVector, initialDirection: Direction) {
        var position = start
        var number = 0
        var direction = initialDirection

        do {
            track[position] = number
            direction = listOf(direction, direction.previous, direction.next).first {
                val newPosition = position + it.vector
                (newPosition !in walls)
            }
            position += direction.vector
            number++
        } while (position != end)
        track[end] = number
    }

    init {
        parseGrid(input) { p, c ->
            when (c) {
                'S' -> start = p
                'E' -> end = p
                '#' -> walls += p
            }
        }

        buildTrack(start, Direction.UP)
    }

    private val cheats: Set<UniquePair<IntVector>>
        get() = track.keys.flatMap { p ->
            disc(p, cheatDuration)
                .filter { it in track }
                .map { UniquePair(p to it) }
        }.toSet()

    val cheatTimes: List<Int>
        get() = cheats.map { (p1, p2) ->
            val time1 = track[p1] ?: throw IllegalStateException()
            val time2 = track[p2] ?: throw IllegalStateException()

            (time2 - time1).absoluteValue - (p2-p1).norm
        }
}

fun main() {
    fun part1(input: List<String>): Int {
        val race = Racetrack(input, cheatDuration = 2)
        return race.cheatTimes.count { it >= 100 }
    }

    fun part2(input: List<String>): Int {
        val race = Racetrack(input, cheatDuration = 20)
        return race.cheatTimes.count { it >= 100 }
    }

    val input = readInput("Day20")

    part1(input).println()
    part2(input).println()
}
