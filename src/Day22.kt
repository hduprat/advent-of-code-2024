fun nextSecret(n: Binary): Binary {
    val a = ((n shl 6) xor n) % 24
    val b = ((a shr 5) xor a) % 24
    return ((b shl 11) xor b) % 24
}

val secretsMap = mutableMapOf<Int, List<Int>>()
fun generateSecrets(n: Int): Int {
    if (n in secretsMap) return secretsMap[n]!!.last()

    var b = n.toBinary()
    val line = mutableListOf("${b.dec}".last().digitToInt())
    for (a in 1..2000) {
        b = nextSecret(b)
        line.add("${b.dec}".last().digitToInt())
    }
    secretsMap[n] = line
    return b.dec
}

fun main() {
    fun part1(input: List<String>): Long {
        val secrets = input.map { generateSecrets(it.toInt()) }

        return secrets.sumOf { it.toLong() }
    }

    fun part2(input: List<String>): Int {
        input.map { generateSecrets(it.toInt()) }

        val sec0 = secretsMap[input[0].toInt()] ?: throw Exception("snif")
        val index = sec0.subList(4, sec0.size).indexOf(9) + 4
        val interval = sec0.slice(index - 4..index).windowed(2) { (a, b) -> b - a }.also(Any::println)

        val sec1 = secretsMap[input[1].toInt()] ?: throw Exception("snif")
        sec1.windowed(5).indexOfFirst { u -> u.windowed(2) { (x, y) -> y - x } == interval }.println()

        return input.size
    }

    val testInput = readInput("Day22_test")
    val testInput2 = readInput("Day22_test_2")

    val input = readInput("Day22")

    check(part1(testInput).also(Any::println) == 37327623L)
    part1(input).println()

    check(part2(testInput2) == 23)
    part2(input).println()
}
