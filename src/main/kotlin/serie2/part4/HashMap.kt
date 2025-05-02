package serie2.part4

class HashMap<K, V>(initialCapacity: Int = 16, private val loadFactor: Float = 0.75f) : MutableMap<K, V> {

    private var table: Array<HashNode<K, V>?> = arrayOfNulls(initialCapacity.coerceAtLeast(1))
    private var _size = 0

    override val size: Int get() = _size
    override val capacity: Int get() = table.size

    private class HashNode<K, V>(
        override val key: K,
        override var value: V,
        var next: HashNode<K, V>?,
        val hc: Int = key.hashCode() // Cache do hash code
    ) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            val old = value
            value = newValue
            return old
        }
    }

    private fun index(hc: Int): Int {
        return (hc and 0x7FFFFFFF) % table.size
    }

    private fun shouldExpand(): Boolean {
        return _size >= table.size * loadFactor
    }

    private fun expand() {
        val newSize = table.size * 2
        val newTable = arrayOfNulls<HashNode<K, V>?>(newSize)

        for (node in table) {
            var current = node
            while (current != null) {
                val next = current.next
                val newIndex = index(current.hc)
                current.next = newTable[newIndex]
                newTable[newIndex] = current
                current = next
            }
        }
        table = newTable
    }

    override fun put(key: K, value: V): V? {
        val hc = key.hashCode()
        if (shouldExpand()) {
            expand()
        }

        val idx = index(hc)
        var current = table[idx]

        while (current != null) {
            if (current.key == key) {
                val old = current.value
                current.value = value
                return old
            }
            current = current.next
        }

        table[idx] = HashNode(key, value, table[idx], hc)
        _size++
        return null
    }

    override fun get(key: K): V? {
        val hc = key.hashCode()
        val idx = index(hc)
        var current = table[idx]

        while (current != null) {
            if (current.key == key) {
                return current.value
            }
            current = current.next
        }

        return null
    }

    override fun iterator(): Iterator<MutableMap.MutableEntry<K, V>> {
        return object : Iterator<MutableMap.MutableEntry<K, V>> {
            private var bucketIndex = 0
            private var current: HashNode<K, V>? = findNextNode()
            private var nextNode: HashNode<K, V>? = current?.next

            private fun findNextNode(): HashNode<K, V>? {
                while (bucketIndex < table.size) {
                    val node = table[bucketIndex]
                    if (node != null) {
                        bucketIndex++
                        return node
                    }
                    bucketIndex++
                }
                return null
            }

            override fun hasNext(): Boolean = current != null

            override fun next(): MutableMap.MutableEntry<K, V> {
                val node = current ?: throw NoSuchElementException()
                current = if (nextNode != null) {
                    val temp = nextNode
                    nextNode = temp?.next
                    temp
                } else {
                    nextNode = findNextNode()?.next
                    findNextNode()
                }
                return node
            }
        }
    }
}
/* USAR SIM OU NÂO????

    // Implementações vazias para outros métodos obrigatórios (simplificação)
    override fun clear() { table.fill(null); _size = 0 }
    override fun isEmpty(): Boolean = _size == 0
    override fun containsKey(key: K): Boolean = get(key) != null
    override fun containsValue(value: V): Boolean {
        for (entry in this) {
            if (entry.value == value) return true
        }
        return false
    }
    override val keys: MutableSet<K> get() = mutableSetOf<K>().also { for (entry in this) it.add(entry.key) }
    override val values: MutableCollection<V> get() = mutableListOf<V>().also { for (entry in this) it.add(entry.value) }
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = mutableSetOf<MutableMap.MutableEntry<K, V>>().also { for (entry in this) it.add(entry) }

 */