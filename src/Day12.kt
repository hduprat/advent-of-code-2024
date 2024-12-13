data class Crop(val kind: Char, var area: Int, var perimeter: Int, val points: MutableSet<IntVector>) {
    val price get() = area * perimeter
    override fun toString(): String = "Crop $kind (area : $area, perimeter : $perimeter)"
}

class Garden(input: List<String>) {
    val map = input.map(String::toList)

    private fun findCrop(point: IntVector, currentCrop: Crop? = null): Crop {
        var crop = currentCrop ?: Crop(map.at(point), 1, 0, mutableSetOf(point))
        crop.points.add(point)

        Direction.entries.forEach {
            val neighbor = point + it.vector
            if (neighbor in crop.points) return@forEach

            val neighborKind = map.atOrNull(neighbor)
            if (crop.kind == neighborKind) {
                crop.area += 1
                crop = findCrop(neighbor, crop)
            } else {
                crop.perimeter += 1
            }
        }

        return crop
    }

    fun findAllCrops(): List<Pair<Char, Crop>> {
        val checkedPoints = mutableSetOf<IntVector>()
        val crops = mutableListOf<Pair<Char, Crop>>()

        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (x to y !in checkedPoints) {
                    val newCrop = findCrop(x to y)
                    checkedPoints.addAll(newCrop.points)
                    crops.add(newCrop.kind to newCrop)
                }
            }
        }

        return crops
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val garden = Garden(input)
        val crops = garden.findAllCrops()
        return crops.sumOf { (kind, crop) -> crop.price }
    }

    /*
     * It will be painful to count the number of sides.
     * It can be done by computing the boundary, then count the number of changes in direction when walking through it. If no direction change: 4 sides.
     */
    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 1930)
    part1(input).println()

    check(part2(testInput) == 1206)
    part2(input).println()
}
