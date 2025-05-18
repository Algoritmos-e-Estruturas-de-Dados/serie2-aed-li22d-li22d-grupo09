package serie2.problema

import java.io.File

object ProcessPointsCollection {
    private data class Point(val id: String, val x: Float, val y: Float) {
        // Sobrescrevemos equals e hashCode para considerar apenas x e y na comparação
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }
    }

    private val pointsMap = mutableMapOf<Point, Int>() // Mapa para armazenar pontos e origem (1 ou 2)
    private val duplicatedPoints = mutableSetOf<Point>() // Conjunto de pontos duplicados

    fun load(file1: String, file2: String) {
        pointsMap.clear()
        duplicatedPoints.clear()

        // Processar o primeiro arquivo
        File(file1).forEachLine { line ->
            if (line.startsWith("v ")) {
                val parts = line.split(" ")
                if (parts.size >= 4) {
                    val id = parts[1]
                    val x = parts[2].toFloatOrNull()
                    val y = parts[3].toFloatOrNull()
                    if (x != null && y != null) {
                        val point = Point(id, x, y)
                        val oldValue = pointsMap.put(point, 1)
                        if (oldValue != null) { // Ponto já existia
                            duplicatedPoints.add(point)
                        }
                    }
                }
            }
        }

        // Processar o segundo arquivo
        File(file2).forEachLine { line ->
            if (line.startsWith("v ")) {
                val parts = line.split(" ")
                if (parts.size >= 4) {
                    val id = parts[1]
                    val x = parts[2].toFloatOrNull()
                    val y = parts[3].toFloatOrNull()
                    if (x != null && y != null) {
                        val point = Point(id, x, y)
                        val oldValue = pointsMap.put(point, 2)
                        if (oldValue != null) { // Ponto já existia
                            duplicatedPoints.add(point)
                        }
                    }
                }
            }
        }
    }

    fun union(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            pointsMap.keys.forEach { point ->
                writer.println("v ${point.id} ${point.x} ${point.y}")
            }
        }
    }

    fun intersection(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            duplicatedPoints.forEach { point ->
                writer.println("v ${point.id} ${point.x} ${point.y}")
            }
        }
    }

    fun difference(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            pointsMap.forEach { (point, origin) ->
                // Inclui pontos que estão apenas em um arquivo (não estão em duplicatedPoints)
                if (point !in duplicatedPoints) {
                    writer.println("v ${point.id} ${point.x} ${point.y}")
                }
            }
        }
    }
}

fun main() {
    println("""Bem-vindo ao ProcessPointsCollection!
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
                    ProcessPointsCollection.load(input[1], input[2])
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
                    ProcessPointsCollection.union(input[1])
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
                    ProcessPointsCollection.intersection(input[1])
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
                    ProcessPointsCollection.difference(input[1])
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