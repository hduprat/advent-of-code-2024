val robotRegex = """p=([-\d,]+) v=([-\d,]+)""".toRegex()

class Robot(input: String) {
    var position: IntVector
    private var velocity: IntVector

    init {
        val results = robotRegex.find(input) ?: throw IllegalStateException("Not a robot")
        val (px, py) = results.groupValues[1].split(",").map(String::toInt)
        val (vx, vy) = results.groupValues[2].split(",").map(String::toInt)

        position = px to py
        velocity = vx to vy
    }

    fun advance(steps: Int = 1) {
        position = (position + steps * velocity)
    }
}

class RobotRoom(input: List<String>, private val height: Int, private val width: Int) {
    private val robots = input.map { Robot(it) }

    fun getSafetyFactorAfterTime(time: Int): Long {
        val finalPositions = robots.map {
            it.advance(time)
            val (x, y) = it.position
            x.mod(width) to y.mod(height)
        }

        val (upper, lower) = finalPositions.filter { (x, y) -> x != width / 2 && y != height / 2 }
            .partition { it.second < height / 2 }

        val (q1, q2) = upper.partition { it.first < width / 2 }
        val (q3, q4) = lower.partition { it.first < width / 2 }

        return q1.size.toLong() * q2.size.toLong() * q3.size.toLong() * q4.size.toLong()
    }
}

class RobotPicture(input: List<String>, private val height: Int, private val width: Int) {
    var iteration = 0
    private val robots = input.map { Robot(it) }

    private fun advance() {
        iteration++
        robots.map { it.advance() }
    }

    fun advanceToCluster() {
        while (robots.count {
                val x = it.position.first.mod(width)
                val y = it.position.second.mod(height)
                x in width / 3..2 * width / 3 && y in height / 3..2 * height / 3
            } < robots.size / 2) {
            advance()
        }
    }

    /**
     * Helper to generate an image from the picture
     */
    fun generateImage(name: String) {
        generateColorImage(name, width, height) { (x, y) ->
            if (robots.find {
                    val (rx, ry) = it.position
                    rx.mod(width) == x && ry.mod(height) == y
                } != null) 0x00ff00 else 0x000000
        }
    }

    override fun toString() = (0 until height).joinToString("\n") { y ->
        (0 until width).joinToString("") { x ->
            if (robots.find {
                    val (rx, ry) = it.position
                    rx.mod(width) == x && ry.mod(height) == y
                } != null) "#" else "."
        }
    }
}

fun main() {
    fun part1(input: List<String>, height: Int, width: Int): Long {
        val room = RobotRoom(input, height, width)
        return room.getSafetyFactorAfterTime(100)
    }

    fun part2(input: List<String>, height: Int, width: Int): Int {
        val picture = RobotPicture(input, height, width)

        picture.advanceToCluster()
        picture.generateImage("christmastree")
        return picture.iteration
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput, 7, 11) == 12L)
    part1(input, 103, 101).println()

    part2(input, 103, 101).println()
}
