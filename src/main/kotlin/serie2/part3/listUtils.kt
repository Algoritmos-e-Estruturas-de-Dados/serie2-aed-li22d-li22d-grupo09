package serie2.part3

class Node<T> (
    var value: T = Any() as T,
    var next: Node<T>? = null,
    var previous: Node<T>? = null) {
}

fun splitEvensAndOdds(list: Node<Int>) {
    var current = list.next
    var lastEven: Node<Int>? = null

    while (current != list) {
        val next = current?.next
        if (current?.value?.rem(2) == 0) {
            // Remove o nó par da sua posição atual
            current.previous?.next = current.next
            current.next?.previous = current.previous

            // Insere o nó par após o último par encontrado (ou no início se for o primeiro)
            if (lastEven == null) {
                current.next = list.next
                current.previous = list
                list.next?.previous = current
                list.next = current
            } else {
                current.next = lastEven.next
                current.previous = lastEven
                lastEven.next?.previous = current
                lastEven.next = current
            }

            lastEven = current
        }
        current = next
    }
}

fun <T> intersection(list1: Node<T>, list2: Node<T>, cmp: Comparator<T>): Node<T>? {
    var resultHead: Node<T>? = null
    var resultTail: Node<T>? = null

    var curr1 = list1.next
    var curr2 = list2.next

    while (curr1 != list1 && curr2 != list2) {
        val comparison = cmp.compare(curr1?.value ?: return null, curr2?.value ?: return null)

        when {
            comparison < 0 -> curr1 = curr1?.next
            comparison > 0 -> curr2 = curr2?.next
            else -> {
                // Elementos iguais - adicionar à interseção
                val newNode = curr1 ?: return resultHead
                curr1 = curr1.next
                curr2 = curr2?.next

                // Remove o nó de list1
                newNode.previous?.next = newNode.next
                newNode.next?.previous = newNode.previous

                // Adiciona ao resultado
                if (resultHead == null) {
                    resultHead = newNode
                    resultTail = newNode
                    newNode.previous = null
                    newNode.next = null
                } else {
                    resultTail?.next = newNode
                    newNode.previous = resultTail
                    newNode.next = null
                    resultTail = newNode
                }

                // Pula elementos duplicados
                while (curr1 != list1 && cmp.compare(newNode.value, curr1?.value ?: break) == 0) {
                    curr1 = curr1.next
                }
                while (curr2 != list2 && cmp.compare(newNode.value, curr2?.value ?: break) == 0) {
                    curr2 = curr2?.next
                }
            }
        }
    }

    return resultHead
}