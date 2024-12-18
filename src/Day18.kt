class ByteGrid(input: List<String>, private val height: Int, private val width: Int) {
    private val allBytes = input.map { it.split(",").map(String::toInt).zipWithNext().single() }
    private var bytes: List<IntVector> = emptyList()
    var currentByte = allBytes[0]

    private val routeMap = mutableMapOf<IntVector, Int>()

    fun fall(number: Int = bytes.size + 1) {
        routeMap.clear()
        bytes = allBytes.take(number)
        currentByte = bytes.last()
    }

    fun findExit(position: IntVector = 0 to 0, direction: Direction? = null, pathSize: Int = 0) {
        if (position == width - 1 to height - 1) {
            routeMap[position] = pathSize
            return
        }

        if (position in bytes) return

        if (!position.inRect(height, width)) return

        val shortestRouteAlreadyAtPosition = routeMap[position]
        if (shortestRouteAlreadyAtPosition == null || shortestRouteAlreadyAtPosition > pathSize) {
            routeMap[position] = pathSize
            val availableDirections = if(direction == null) Direction.entries else listOf(direction.previous, direction, direction.next)
            availableDirections.forEach {
                val nextPosition = position + it.vector
                findExit(nextPosition, it, pathSize + 1)
            }
            return
        }
    }

    val shortestExitSteps: Int? get() = routeMap[width - 1 to height - 1]
}


fun main() {
    fun part1(input: List<String>, gridSize: Int, numberOfFirstBytes: Int): Int {
        val grid = ByteGrid(input, gridSize, gridSize)

        grid.fall(numberOfFirstBytes)
        grid.findExit()
        return grid.shortestExitSteps ?: -1
    }

    fun part2(input: List<String>, gridSize: Int, startingByteNumber: Int): String {
        val grid = ByteGrid(input, gridSize, gridSize)
        grid.fall(startingByteNumber)
        // maybe dichotomy if I can't find a faster way
        do {
            grid.fall()
            grid.findExit()
        } while (grid.shortestExitSteps != null)

        return grid.currentByte.let { (x, y) -> "$x,$y" }
    }

    val testInput = readInput("Day18_test")
    val input = readInput("Day18")

    check(part1(testInput, 7, 12).also { it.println() } == 22)
    part1(input, 71, 1024).println()

    check(part2(testInput, 7, 12).also { it.println() } == "6,1")
    part2(input, 71, 1024).println()
}
