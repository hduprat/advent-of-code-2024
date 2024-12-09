fun main() {
    fun parse(input: String): List<Int?> {
        val result = mutableListOf<Int?>()
        var id = 0
        input.forEachIndexed { index, c ->
            val n = c.digitToInt()
            result.addAll(MutableList(n) { if (index % 2 == 0) id else null })
            if (index % 2 == 0) id++
        }
        return result
    }

    fun checksum(disk: List<Int?>): Long {
        var lastIndex = disk.size - 1
        var index = 0
        var sum = 0L

        while (index <= lastIndex) {
            val elt = disk[index]
            if (elt == null) {
                while (disk[lastIndex] == null) {
                    lastIndex--
                }
                sum += disk[lastIndex]!! * index
                lastIndex--
                index++
            } else {
                sum += elt * index
                index++
            }
        }

        return sum
    }

    fun parse2(input: String): List<Pair<Int?, Int>> {
        val files = input.filterIndexed { index, _ -> index % 2 == 0 }
        val spaces = input.filterIndexed { index, _ -> index % 2 == 1 }

        val disk1 = files.mapIndexed { index, c -> index to c.digitToInt() }
        val disk2 = spaces.map { null to it.digitToInt() }

        return buildList {
            for (i in 0..input.length / 2) {
                if (i < disk1.size) add(disk1[i])
                if (i < disk2.size) add(disk2[i])
            }
        }
    }

    fun rearrange2(disk: List<Pair<Int?, Int>>): List<Pair<Int?, Int>> {
        val tempDisk = disk.toMutableList()
        val files = disk.filter { it.first != null }.asReversed().toMutableList()

        files.forEach { file ->
            val fileIndex = tempDisk.indexOf(file)
            val firstSpaceIndex = tempDisk.indexOfFirst { it.first == null && it.second >= file.second }
            if (firstSpaceIndex in 0 until fileIndex) {
                val firstSpace = tempDisk[firstSpaceIndex]
                tempDisk[fileIndex] = null to file.second
                tempDisk.add(firstSpaceIndex, file)
                tempDisk[firstSpaceIndex + 1] = null to firstSpace.second - file.second
            }
        }

        return tempDisk.filter { it != null to 0 }.toList()
    }

    fun checksum2(disk: List<Pair<Int?, Int>>): Long {
        var sum = 0L
        var index = 0

        disk.forEach { file ->
            val f = file.first
            when (f) {
                null -> index += file.second
                else -> {
                    for (i in 0 until file.second) {
                        sum += f * index
                        index++
                    }
                }
            }
        }

        return sum
    }

    fun part1(input: List<String>): Long {
        val disk = parse(input[0])
        return checksum(disk)
    }

    fun part2(input: List<String>): Long {
        val disk = parse2(input[0])
        return checksum2(rearrange2(disk))
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 1928L)
    part1(input).println()
    check(part1(input) == 6463499258318L)

    check(part2(testInput) == 2858L)
    part2(input).println()
}
