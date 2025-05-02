package serie2.part1_2

class IntArrayList(private val capacity: Int) {
    private val array = IntArray(capacity)
    private var start = 0
    private var end = 0
    private var count = 0

    fun append(x: Int): Boolean {
        if (count == capacity) return false
        array[end] = x
        end = (end + 1) % capacity
        count++
        return true
    }

    fun get(n: Int): Int? {
        if (n < 0 || n >= count) return null
        return array[(start + n) % capacity]
    }

    fun addToAll(x: Int) {
        for (i in 0 until count) {
            val index = (start + i) % capacity
            array[index] += x
        }
    }

    fun remove(): Boolean {
        if (count == 0) return false
        start = (start + 1) % capacity
        count--
        return true
    }
}