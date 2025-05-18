package serie2.problema

import java.io.File

// Objeto responsável por carregar, comparar e exportar pontos de dois arquivos
object ProcessPointsCollection {

    // Representação de um ponto 2D com identificador e coordenadas
    private data class Point(val id: String, val x: Float, val y: Float) {
        // Comparamos pontos apenas pelas coordenadas (x, y), ignorando o id
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        // hashCode coerente com equals (também baseado apenas em x e y)
        override fun hashCode(): Int {
            return 31 * x.hashCode() + y.hashCode()
        }
    }

    // Mapa que associa um ponto à origem (1 = primeiro ficheiro, 2 = segundo)
    private val pointsMap = mutableMapOf<Point, Int>()

    // Conjunto de pontos que aparecem em ambos os ficheiros
    private val duplicatedPoints = mutableSetOf<Point>()

    // Carrega os dados de dois ficheiros e preenche o mapa e o conjunto de duplicados
    fun load(file1: String, file2: String) {
        pointsMap.clear()            // Limpa os dados anteriores
        duplicatedPoints.clear()

        // Processa o primeiro ficheiro
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
                        if (oldValue != null) {
                            // Já existia (provavelmente do segundo ficheiro), então é duplicado
                            duplicatedPoints.add(point)
                        }
                    }
                }
            }
        }

        // Processa o segundo ficheiro
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
                        if (oldValue != null) {
                            duplicatedPoints.add(point)
                        }
                    }
                }
            }
        }
    }

    // Escreve todos os pontos (sem repetição de coordenadas) no ficheiro de saída
    fun union(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            pointsMap.keys.forEach { point ->
                writer.println("v ${point.id} ${point.x} ${point.y}")
            }
        }
    }

    // Escreve apenas os pontos que existem nos dois ficheiros (interseção)
    fun intersection(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            duplicatedPoints.forEach { point ->
                writer.println("v ${point.id} ${point.x} ${point.y}")
            }
        }
    }

    // Escreve os pontos que estão apenas em um dos ficheiros
    fun difference(outputFile: String) {
        File(outputFile).printWriter().use { writer ->
            pointsMap.forEach { (point, _) ->
                if (point !in duplicatedPoints) {
                    writer.println("v ${point.id} ${point.x} ${point.y}")
                }
            }
        }
    }
}


fun main() {
    // Mensagem de boas-vindas e instruções
    println("""Bem-vindo ao ProcessPointsCollection!
            Comandos disponíveis:
            - load <file1.co> <file2.co>       // Carrega os ficheiros de entrada
            - union <output.co>                // Exporta a união dos pontos
            - intersection <output.co>         // Exporta a interseção dos pontos
            - difference <output.co>           // Exporta a diferença dos pontos
            - exit                             // Sai do programa
            """)

    // Loop principal: lê comandos do utilizador repetidamente
    while (true) {
        print("> ")  // Prompt
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
