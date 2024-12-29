class Binary(private val values: List<Boolean>) {

    constructor(n: Int) : this(n.let {
        var tmp = it
        val list = mutableListOf<Boolean>()
        do {
            list.add(tmp % 2 == 1)
            tmp /= 2
        } while (tmp > 1)
        list.add(tmp % 2 == 1)
        list.reversed()
    })

    constructor(str: String) : this(str.map {
        when (it) {
            '0' -> false
            '1' -> true
            else -> throw IllegalArgumentException()
        }
    })

    override fun toString(): String =
        "0b" + values.joinToString("") {
            if (it) "1" else "0"
        }

    val length: Int get() = values.size

    val dec: Int get() = values.fold(0) { acc, isOne -> 2 * acc + if (isOne) 1 else 0 }

    fun toLong(): Long = values.fold(0L) { acc, isOne -> 2 * acc + if (isOne) 1L else 0L }

    infix fun xor(other: Binary): Binary {
        val maxLength = maxOf(length, other.length)
        val a = List(maxLength - length) { false } + this.values
        val b = List(maxLength - other.length) { false } + other.values

        return Binary((0 until maxLength).map { a[it] xor b[it] })
    }

    infix fun shl(n: Int): Binary {
        return Binary(values + List(n) { false })
    }

    infix fun shr(n: Int): Binary {
        return Binary(values.take(length - n))
    }

    /**
     * Careful! It computes the remainder of the division by 2^n, not n.
     */
    operator fun rem(n: Int): Binary = Binary(values.takeLast(n))
}

fun Int.toBinary(): Binary = Binary(this)

fun String.toBinary(): Binary = Binary(this)
fun String.intToBinary(): Binary = Binary(this.toInt())