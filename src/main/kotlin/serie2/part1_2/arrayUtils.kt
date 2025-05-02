package serie2.part1_2
import kotlin.random.Random

fun minimum(maxHeap: Array<Int>, heapSize: Int): Int {
    // O menor elemento em um max-heap está necessariamente em uma das folhas
    // As folhas estão nos índices a partir de heapSize/2 até heapSize-1
    var min = Int.MAX_VALUE
    val firstLeafIndex = heapSize / 2

    for (i in firstLeafIndex until heapSize) {
        if (maxHeap[i] < min) {
            min = maxHeap[i]
        }
    }

    return min
}