class Lock(input: List<String>) {
    companion object {
        fun isLock(input: List<String>): Boolean {
            return input[0] == "#####"
        }
    }

    private val pinHeights = buildList(5) {
        if (!isLock(input)) throw IllegalArgumentException("You did not provide a lock.")
        for (x in 0 until 5) {
            val pin = input.takeWhile { it[x] == '#' }
            add(pin.size - 1)
        }
    }

    fun fits(key: Key): Boolean {
        pinHeights.forEachIndexed { index, lockPinHeight ->
            val keyPinHeight = key.pinHeights[index]
            if (lockPinHeight + keyPinHeight > 5) return false
        }
        return true
    }

    override fun toString(): String = pinHeights.joinToString(",", "(", ")")
}

class Key(input: List<String>) {
    companion object {
        fun isKey(input: List<String>): Boolean {
            return input.last() == "#####"
        }
    }

    val pinHeights = buildList(5) {
        if (!isKey(input)) throw IllegalArgumentException("You did not provide a lock.")
        for (x in 0 until 5) {
            val pin = input.reversed().takeWhile { it[x] == '#' }
            add(pin.size - 1)
        }
    }

    override fun toString(): String = pinHeights.joinToString(",", "(", ")")
}

fun main() {
    fun part1(input: List<String>): Int {
        val locksAndKeys = input.split("")
        val locks = locksAndKeys.filter { Lock.isLock(it) }.map { Lock(it) }
        val keys = locksAndKeys.filter { Key.isKey(it) }.map { Key(it) }

        return locks.sumOf { lock -> keys.count { lock.fits(it) } }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day25_test")
    val input = readInput("Day25")

    check(part1(testInput) == 3)
    part1(input).println()
}
