class Lab(
    private val height: Int,
    private val width: Int,
    private val obstacles: List<IntVector>,
    private val guardPosition: IntVector
) {

    constructor(input: List<String>) : this(input.size, input[0].length, buildList {
        parseGrid(input) { point, c -> if (c == '#') add(point) }
    }, input.run {
        lateinit var result: IntVector
        parseGrid(this) { point, c ->
            if (c == '^') {
                result = point
            }
        }
        result
    })

    fun getGuardRound(): List<IntVector> {
        var position = guardPosition
        var direction = Direction.UP

        val positions = mutableListOf<Pair<IntVector, Direction>>()

        do {
            positions += position to direction

            val nextPos = position + direction.vector
            if (nextPos to direction in positions) throw IllegalStateException("Loop encountered")
            if (nextPos in obstacles) {
                direction = direction.next
                position += direction.vector
            } else {
                position = nextPos
            }
        } while (position.inRect(height, width))



        return positions.map { it.first }
    }

    fun isInLoop(): Boolean {
        var position = guardPosition
        var direction = Direction.UP

        val positions = mutableListOf<Pair<IntVector, Direction>>()

        do {

            val nextPos = position + direction.vector
            if (nextPos to direction in positions) return true
            if (nextPos in obstacles) {
                positions += position to direction
                direction = direction.next
            } else {
                position = nextPos
            }
        } while (position.inRect(height, width))

        return false
    }

    fun withExtraObstacle(obstacle: IntVector): Lab = Lab(height, width, obstacles + obstacle, guardPosition)
}

fun main() {
    fun part1(input: List<String>): Int {
        val lab = Lab(input)

        return lab.getGuardRound().distinct().size
    }

    fun part2(input: List<String>): Int {
        val lab = Lab(input)
        val guardRound = lab.getGuardRound()

        val extraObstaclePotentialPositions = buildList {
            addAll(guardRound.distinct())
            removeAt(0)
        }

        return extraObstaclePotentialPositions.map {
            println("Simulating obstacle at $it.")
            lab.withExtraObstacle(it).isInLoop()
        }.count { it }
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 41)
    part1(input).println()

    check(part2(testInput) == 6)
    part2(input).println()
}
