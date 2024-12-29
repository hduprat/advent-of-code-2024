val operationRegex = """([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)""".toRegex()

class Registry(input: List<String>) {
    val values = mutableMapOf<String, Boolean>()
    val ops = mutableMapOf<String, Triple<String, String, String>>()

    init {
        val (initialValues, operations) = input.split("")
        values.putAll(initialValues.map {
            val (name, value) = it.split(": ").zipWithNext().single()
            name to (value == "1")
        })

        operations.forEach {
            val (_, op1, operator, op2, result) = operationRegex.find(it)?.groupValues
                ?: throw IllegalStateException("Bad operation")

            ops[result] = Triple(operator, op1, op2)
        }
    }

    fun getValue(key: String): Boolean {
        return values[key] ?: ops.let {
            val (operator, op1, op2) = it[key] ?: throw IllegalStateException("No operation to create $key")
            val result = when (operator) {
                "AND" -> getValue(op1) and getValue(op2)
                "OR" -> getValue(op1) or getValue(op2)
                "XOR" -> getValue(op1) xor getValue(op2)
                else -> throw IllegalStateException("Invalid operation")
            }

            values[key] = result
            result
        }
    }

    fun getZ(): Binary {
        return Binary(ops.keys.filter { it.startsWith('z') }.sorted().reversed().map { getValue(it) })
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val registry = Registry(input)
        val z = registry.getZ()

        return z.toLong()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day24_test")
    val largerTestInput = readInput("Day24_test_larger")
    val input = readInput("Day24")

    check(part1(testInput) == 4L)
    check(part1(largerTestInput) == 2024L)
    part1(input).println()

    check(part2(testInput) == 1)
    part2(input).println()
}
