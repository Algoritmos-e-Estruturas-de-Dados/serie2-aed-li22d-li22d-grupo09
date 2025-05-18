package serie2.problema

import java.io.File
import serie2.part4.HashMap

object ProcessPointsCollectionsCustom2 {
    private data class Point(val id: String, val x: Double, val y: Double) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false
            return id == other.id && x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }
    }

    // Using our custom HashMap implementation
    private val pointsMap = HashMap<Point, BooleanArray>()

    fun load(file1: String, file2: String) {
        parseFile(file1, 0)
        parseFile(file2, 1)
    }

    private fun parseFile(filename: String, fileIndex: Int) {
        val reader = File(filename).bufferedReader()

        reader.useLines { lines ->
            lines.forEach { line ->
                when {
                    line.startsWith("v ") -> {
                        val parts = line.split(' ')
                        if (parts.size >= 4) {
                            val id = parts[1]
                            val x = parts[2].toDouble()
                            val y = parts[3].toDouble()
                            val point = Point(id, x, y)

                            val existing = pointsMap[point]
                            if (existing != null) {
                                existing[fileIndex] = true
                                pointsMap.put(point, existing)
                            } else {
                                val newArray = BooleanArray(2) { false }
                                newArray[fileIndex] = true
                                pointsMap.put(point, newArray)
                            }
                        }
                    }
                    // Ignore comments and problem lines
                    line.startsWith("c ") || line.startsWith("p ") -> {}
                    else -> {} // Ignore malformed lines
                }
            }
        }
    }

    fun union(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
            }
        }
    }

    fun intersection(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                if (entry.value[0] && entry.value[1]) {
                    writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
                }
            }
        }
    }

    fun difference(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                if (entry.value[0] != entry.value[1]) {
                    writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
                }
            }
        }
    }
}



fun main() {
    println("""Bem-vindo ao ProcessPointsCollection2 (usando AEDHashMap)!
Comandos disponíveis:
- load <file1.co> <file2.co>
- union <output.co>
- intersection <output.co>
- difference <output.co>
- exit
""")

    while (true) {
        print("> ")
        val input = readLine()?.trim()?.split(' ') ?: continue

        when (input[0]) {
            "load" -> {
                if (input.size != 3) {
                    println("Uso: load <file1.co> <file2.co>")
                    continue
                }
                try {
                    ProcessPointsCollectionsCustom2.load(input[1], input[2])
                    println("Arquivos carregados com sucesso.")
                } catch (e: Exception) {
                    println("Erro ao carregar arquivos: ${e.message}")
                }
            }
            "union" -> {
                if (input.size != 2) {
                    println("Uso: union <output.co>")
                    continue
                }
                try {
                    ProcessPointsCollectionsCustom2.union(input[1])
                    println("Operação union concluída. Resultado salvo em ${input[1]}")
                } catch (e: Exception) {
                    println("Erro ao executar union: ${e.message}")
                }
            }
            "intersection" -> {
                if (input.size != 2) {
                    println("Uso: intersection <output.co>")
                    continue
                }
                try {
                    ProcessPointsCollectionsCustom2.intersection(input[1])
                    println("Operação intersection concluída. Resultado salvo em ${input[1]}")
                } catch (e: Exception) {
                    println("Erro ao executar intersection: ${e.message}")
                }
            }
            "difference" -> {
                if (input.size != 2) {
                    println("Uso: difference <output.co>")
                    continue
                }
                try {
                    ProcessPointsCollectionsCustom2.difference(input[1])
                    println("Operação difference concluída. Resultado salvo em ${input[1]}")
                } catch (e: Exception) {
                    println("Erro ao executar difference: ${e.message}")
                }
            }
            "exit" -> {
                println("Encerrando aplicação...")
                return
            }
            else -> {
                println("Comando desconhecido. Comandos disponíveis: load, union, intersection, difference, exit")
            }
        }
    }
}