val ButtonARegex = """Button A: X\+(\d+), Y\+(\d+)""".toRegex()
val ButtonBRegex = """Button B: X\+(\d+), Y\+(\d+)""".toRegex()
val PrizeRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

typealias LongVector = Pair<Long, Long>

operator fun LongVector.plus(other: LongVector): LongVector = this.first + other.first to this.second + other.second
operator fun LongVector.minus(other: LongVector): LongVector = this.first - other.first to this.second - other.second
operator fun LongVector.times(n: Long): LongVector = n * this.first to n * this.second
operator fun Long.times(vec: LongVector): LongVector = vec.times(this)

class ClawMachine(input: List<String>, prizeOffset: Long = 0) {
    private val aVector: LongVector = ButtonARegex.find(input[0]).let {
        if (it == null) throw IllegalStateException("No button A")
        it.groupValues[1].toLong() to it.groupValues[2].toLong()
    }

    private val bVector: LongVector = ButtonBRegex.find(input[1]).let {
        if (it == null) throw IllegalStateException("No button B")
        it.groupValues[1].toLong() to it.groupValues[2].toLong()
    }

    private val prizeLocation: LongVector = PrizeRegex.find(input[2]).let {
        if (it == null) throw IllegalStateException("No prize")
        it.groupValues[1].toLong() + prizeOffset to it.groupValues[2].toLong() + prizeOffset
    }

    val cheapestCost: Long
        get() {
            // Let's MATH
            val b =
                (prizeLocation.first * aVector.second - aVector.first * prizeLocation.second) / (bVector.first * aVector.second - bVector.second * aVector.first)
            val a = (prizeLocation.second - bVector.second * b) / aVector.second

            // As we got integers we have to check they were not just rounded floats before returning the cost in tokens
            if(a * aVector + b * bVector == prizeLocation) return 3 * a + b
            return 0
        }

    override fun toString() =
        """Button A vector: $aVector
            |Button B vector: $bVector
            |Prize is at $prizeLocation
        """.trimMargin()
}

class Arcade(input: List<String>, prizeOffset: Long = 0) {
    private val machines = input.chunked(4) { ClawMachine(it, prizeOffset) }

    val cheapestCombo = machines.sumOf {
        it.cheapestCost
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val arcade = Arcade(input)
        return arcade.cheapestCombo
    }

    fun part2(input: List<String>): Long {
        val arcade = Arcade(input, 10000000000000L)
        return arcade.cheapestCombo
    }

    val testInput = readInput("Day13_test")
    val input = readInput("Day13")

    check(part1(testInput) == 480L)
    part1(input).println()

    part2(input).println()
}
