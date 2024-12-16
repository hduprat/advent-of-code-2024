class Labyrinth(input: List<String>) {
    private lateinit var start: IntVector
    private lateinit var goal: IntVector
    private val walls: List<IntVector>
    private val scoreMap = mutableMapOf<IntVector, Int>()

    init {
        val initWalls = mutableListOf<IntVector>()

        parseGrid(input) { point, c ->
            when (c) {
                '#' -> initWalls.add(point)
                'S' -> start = point
                'E' -> goal = point
            }
        }

        walls = initWalls
    }

    private fun canGoForward(point: IntVector, direction: Direction): Boolean = (point + direction.vector) !in walls

    fun fillPaths(
        startingPoint: IntVector = start,
        direction: Direction = Direction.RIGHT,
        currentScore: Int = 0
    ): Set<IntVector> {
        try {
            val scoreAtPoint = scoreMap[startingPoint]
            if (scoreAtPoint != null && scoreAtPoint < currentScore) throw Exception("This path is not the best")
            scoreMap[startingPoint] = currentScore

            if (startingPoint == goal) return setOf(goal)

            val points = mutableSetOf(startingPoint)

            if (canGoForward(startingPoint, direction)) {
                points.addAll(fillPaths(startingPoint + direction.vector, direction, currentScore + 1))
            }

            listOf(direction.previous, direction.next).forEach {
                if (canGoForward(startingPoint, it)) points.addAll(
                    fillPaths(
                        startingPoint + it.vector,
                        it,
                        currentScore + 1001
                    )
                )
            }

            return points
        } catch (e: Exception) {
            return emptySet()
        }
    }

    val lowestScore: Int get() = scoreMap[goal] ?: throw IllegalStateException("Sadly, there is no lowest score.")
}


fun main() {
    val fillPathsCache = mutableMapOf<Int, Set<IntVector>>()

    fun part1(input: List<String>): Int {
        val labyrinth = Labyrinth(input)
        fillPathsCache[input.size] = labyrinth.fillPaths().also { it.size.println() }

        return labyrinth.lowestScore
    }

    /*
     * To solve part 2, I think I have the right algorithm. I just need to get all the paths going to the goal, along with their score, and get only those with the min score
     */
    fun part2(input: List<String>): Int {
        val paths = fillPathsCache[input.size] ?: run {
            val labyrinth = Labyrinth(input)
            labyrinth.fillPaths().also {
                it.println()
                fillPathsCache[input.size] = it
            }
        }

        return paths.size
    }

    val testInput = readInput("Day16_test")
    val largerTestInput = readInput("Day16_test_larger")
    val input = readInput("Day16")

    check(part1(testInput) == 7036)
    check(part1(largerTestInput) == 11048)
//    part1(input).println()

    check(part2(testInput) == 45)
    check(part2(largerTestInput) == 64)
    part2(input).println()
}
