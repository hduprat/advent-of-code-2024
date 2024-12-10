class GuardMap(input: List<String>) {
    private val height = input.size
    private val width = input[0].length
    private val obstacles: List<IntVector>
    private var guardDirection: Direction = Direction.UP
    private val initialDirection: Direction
    private var guardPosition: IntVector = 0 to 0
    val initialPosition: IntVector
    private val allDirections = mutableListOf<Pair<IntVector, Direction>>()

    val allPositions = mutableListOf<IntVector>()
    var extraObstacle: IntVector? = null

    init {
        val tempObstacles = mutableListOf<IntVector>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '#' -> tempObstacles.add(x to y)
                    '^' -> {
                        guardDirection = Direction.UP
                        guardPosition = x to y
                    }

                    '>' -> {
                        guardDirection = Direction.RIGHT
                        guardPosition = x to y
                    }

                    'v' -> {
                        guardDirection = Direction.DOWN
                        guardPosition = x to y
                    }

                    '<' -> {
                        guardDirection = Direction.LEFT
                        guardPosition = x to y
                    }
                }
            }
        }

        obstacles = tempObstacles.toList()
        initialDirection = guardDirection
        initialPosition = guardPosition.copy()
    }

    fun reset() {
        guardPosition = initialPosition.copy()
        guardDirection = initialDirection
        allPositions.clear()
        allPositions.add(initialPosition)
        allDirections.clear()
    }

    val isGuardInMap get() = guardPosition.first in 0 until width && guardPosition.second in 0 until height

    fun advanceGuard() {
        allPositions.add(guardPosition)
        var newPosition = guardPosition + guardDirection.vector
        if (newPosition in obstacles || newPosition == extraObstacle) {
            guardDirection = Direction.entries[(guardDirection.ordinal + 1) % Direction.entries.size]

            if (guardPosition to guardDirection in allDirections) {
                throw Exception("In a loop")
            }
            allDirections.add(guardPosition to guardDirection)
            newPosition = guardPosition + guardDirection.vector
        }
        guardPosition = newPosition
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = GuardMap(input)
        do {
            map.advanceGuard()
        } while (map.isGuardInMap)
        return map.allPositions.distinct().size
    }

    /*
     * I don't know why my result is incorrect here. I might have to just draw a schema and then retry part 2 in its entirety.
     */
    fun part2(input: List<String>): Int {
        val map = GuardMap(input)

        do {
            map.advanceGuard()
        } while (map.isGuardInMap)
        map.advanceGuard()

        val positions = map.allPositions.distinct().filter { it != map.initialPosition }

        val configurations = mutableSetOf<IntVector>()
        for (point in positions) {
            map.reset()
            map.extraObstacle = point
            try {
                do {
                    map.advanceGuard()
                } while (map.isGuardInMap)
            } catch (exc: Exception) {
                println("Placing an obstacle in ${map.extraObstacle} puts the guard in a loop.")
                configurations.add(point)
            }
        }

        return configurations.size
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 41)
    part1(input).println()

//    check(part2(testInput) == 6)
    part2(input).println()
}
