class ThreeBitComputer(input: List<String>, private val isDebug: Boolean = false) {
    private val initA = input[0].split(" ").last().toInt()
    private val initB = input[1].split(" ").last().toInt()
    private val initC = input[2].split(" ").last().toInt()

    private var a = initA
    private var b = initB
    private var c = initC

    private val program = input.last().split(" ").last().split(",").map(String::toInt)
    private var pointer = 0
    private val output = mutableListOf<Int>()

    private fun combo(n: Int): Int = when (n) {
        4 -> a
        5 -> b
        6 -> c
        7 -> throw IllegalArgumentException("7 is reserved, do not use")
        else -> n
    }

    private fun dv(n: Int): Int {
        val numerator = a
        var denominator = 1
        for (i in 0 until combo(n)) {
            denominator *= 2
        }

        return numerator / denominator
    }

    private fun adv(n: Int) {
        a = dv(n)
    }

    private fun bxl(n: Int) {
        b = b xor n
    }

    private fun bst(n: Int) {
        b = combo(n) % 8
    }

    private fun jnz(n: Int) {
        if (a != 0) pointer = n - 2
    }

    private fun bxc() {
        b = b xor c
    }

    private fun out(n: Int) {
        output += combo(n) % 8
    }

    private fun bdv(n: Int) {
        b = dv(n)
    }

    private fun cdv(n: Int) {
        c = dv(n)
    }

    private fun execInstruction(opcode: Int, operand: Int) {
        when (opcode) {
            0 -> adv(operand)
            1 -> bxl(operand)
            2 -> bst(operand)
            3 -> jnz(operand)
            4 -> bxc()
            5 -> out(operand)
            6 -> bdv(operand)
            7 -> cdv(operand)
            else -> throw IllegalArgumentException("Illegal opcode provided")
        }

        pointer += 2
    }

    fun exec(a: Int? = null): String {
        reset()
        if (a != null) this.a = a
        while (pointer in program.indices) {
            execInstruction(program[pointer], program[pointer + 1])
        }

        return output.joinToString(",")
    }

    private fun reset() {
        output.clear()
        pointer = 0
        a = initA
        b = initB
        c = initC
    }

    override fun toString(): String = """
        Value in register A: $a
        Value in register B: $b
        Value in register C: $c

        Program: $program
    """.trimIndent()
}

fun main() {
    fun part1(input: List<String>): String {
        val computer = ThreeBitComputer(input)
        return computer.exec()
    }

    tailrec fun debugProgram(computer: ThreeBitComputer, a: Int = 0): Int {
        try {
            computer.exec(a)
            return a
        } catch (_: Exception) {
        }
        return debugProgram(computer, a + 1)
    }

    /*
     * I should try to treat the numbers are written in binary (e.g. "1101001") and reverse the program, while writing "X" on unknown bits.
     */
    fun part2(input: List<String>): Int {
        val computer = ThreeBitComputer(input, isDebug = true)

        return debugProgram(computer).also { it.println() }
    }

    val testInput = readInput("Day17_test")
    val testInput2 = readInput("Day17_test_2")
    val input = readInput("Day17")

    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    part1(input).println()

    check(part2(testInput2) == 117440)
    part2(input).println()
}
