package serie2.problema

import java.io.File
import serie2.part4.HashMap

object ProcessPointsCollection2 {

    // Representa um ponto no plano com identificador e coordenadas (x, y)
    private data class Point(val id: String, val x: Float, val y: Float) {
        // Compara pontos por id, x e y (duas coordenadas iguais e mesmo id são iguais)
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false
            return id == other.id && x == other.x && y == other.y
        }

        // Gera um hashCode consistente com equals, baseado em id, x e y
        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }
    }

    // Mapa que associa cada ponto a um vetor de 2 posições booleanas:
    // posição 0: true se veio do primeiro ficheiro
    // posição 1: true se veio do segundo ficheiro
    private val pointsMap = HashMap<Point, BooleanArray>()

    // Carrega os dois ficheiros e marca a origem dos pontos (file1 = índice 0, file2 = índice 1)
    fun load(file1: String, file2: String) {
        parseFile(file1, 0) // Lê e marca pontos do primeiro ficheiro
        parseFile(file2, 1) // Lê e marca pontos do segundo ficheiro
    }

    // Lê um ficheiro linha a linha, processa apenas as linhas que representam pontos (linhas que começam com "v")
    private fun parseFile(filename: String, fileIndex: Int) {
        val reader = File(filename).bufferedReader()

        reader.useLines { lines ->
            lines.forEach { line ->
                when {
                    // Processa linhas que representam um ponto (ex: v A 1.0 2.0)
                    line.startsWith("v ") -> {
                        val parts = line.split(' ')
                        if (parts.size >= 4) {
                            val id = parts[1]
                            val x = parts[2].toFloat()
                            val y = parts[3].toFloat()
                            val point = Point(id, x, y)

                            // Se o ponto já existe no mapa, atualiza a origem
                            val existing = pointsMap[point]
                            if (existing != null) {
                                existing[fileIndex] = true
                                pointsMap.put(point, existing)
                            } else {
                                // Caso contrário, cria um novo vetor de origem e insere
                                val newArray = BooleanArray(2) { false }
                                newArray[fileIndex] = true
                                pointsMap.put(point, newArray)
                            }
                        }
                    }

                    // Ignora comentários e declarações de problema
                    line.startsWith("c ") || line.startsWith("p ") -> {}

                    // Ignora quaisquer outras linhas inválidas
                    else -> {}
                }
            }
        }
    }

    // Escreve todos os pontos no ficheiro de saída (união dos dois ficheiros)
    fun union(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
            }
        }
    }

    // Escreve apenas os pontos que apareceram nos dois ficheiros (interseção)
    fun intersection(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                // Só escreve se o ponto veio de ambos os ficheiros
                if (entry.value[0] && entry.value[1]) {
                    writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
                }
            }
        }
    }

    // Escreve apenas os pontos exclusivos de um dos ficheiros (diferença simétrica)
    fun difference(outputFile: String) {
        File(outputFile).bufferedWriter().use { writer ->
            pointsMap.iterator().forEach { entry ->
                // Só escreve se o ponto estiver em apenas um dos ficheiros (XOR lógico)
                if (entry.value[0] != entry.value[1]) {
                    writer.write("v ${entry.key.id} ${entry.key.x} ${entry.key.y}\n")
                }
            }
        }
    }
}



fun main() {
    // Mensagem de boas-vindas e instruções
    println("""Bem-vindo ao ProcessPointsCollection!
Comandos disponíveis:
- load <file1.co> <file2.co>       
- union <output.co>                
- intersection <output.co>         
- difference <output.co> 
- exit       
""")
// Carrega os ficheiros de entrada
// Exporta a união dos pontos
// Exporta a interseção dos pontos
// Exporta a diferença dos pontos
// Sai do programa


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
                    ProcessPointsCollection2.load(input[1], input[2])
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
                    ProcessPointsCollection2.union(input[1])
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
                    ProcessPointsCollection2.intersection(input[1])
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
                    ProcessPointsCollection2.difference(input[1])
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
