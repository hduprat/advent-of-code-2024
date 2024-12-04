fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }
        var total = 0

        fun findInDirection(letter: Char, point: Pair<Int, Int>, direction: Pair<Int, Int>) {
            val x = point.first + direction.first
            val y = point.second + direction.second

            try {
                if (grid[y][x] != letter) return

                when (letter) {
                    'M' -> findInDirection('A', x to y, direction)
                    'A' -> findInDirection('S', x to y, direction)
                    'S' -> total++
                }
            } catch (exception: IndexOutOfBoundsException) {
                return
            }
        }

        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'X') {
                    val point = x to y
                    findInDirection('M', point, 1 to 0)
                    findInDirection('M', point, -1 to 0)
                    findInDirection('M', point, 0 to 1)
                    findInDirection('M', point, 0 to -1)
                    findInDirection('M', point, 1 to 1)
                    findInDirection('M', point, 1 to -1)
                    findInDirection('M', point, -1 to 1)
                    findInDirection('M', point, -1 to -1)
                }
            }
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList() }
        var total = 0

        fun findXmas(point: Pair<Int, Int>) {
            val x = point.first
            val y = point.second

            try {
                val firstDiag = "${grid[y - 1][x - 1]}${grid[y][x]}${grid[y + 1][x + 1]}"
                val secondDiag = "${grid[y + 1][x - 1]}${grid[y][x]}${grid[y - 1][x + 1]}"

                if (firstDiag != "MAS" && firstDiag != "SAM") return
                if (secondDiag != "MAS" && secondDiag != "SAM") return

                total++
            } catch (exception: IndexOutOfBoundsException) {
                return
            }
        }

        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, c -> if (c == 'A') findXmas(x to y) }
        }

        return total
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 18)
    part1(input).println()

    check(part2(testInput) == 9)
    part2(input).println()
}
