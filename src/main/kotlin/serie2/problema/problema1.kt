package serie2.problema

data class Point(val id: String, val x: Double, val y: Double) {
    override fun equals(other: Any?) = other is Point && id == other.id
    override fun hashCode() = id.hashCode()
    override fun toString() = "v $id $x $y"
}

object ProcessPointsCollections {
    private val points1 = HashMap<String, Point>()
    private val points2 = HashMap<String, Point>()

    fun load(file1: String, file2: String) {
        points1.clear(); points2.clear()
        loadFile(file1, points1)
        loadFile(file2, points2)
    }

    private fun loadFile(filename: String, map: HashMap<String, Point>) {
        val file = java.io.File(filename)
        if (!file.exists()) return
        file.forEachLine { line ->
            if (line.startsWith("v")) {
                val parts = line.split(" ")
                if (parts.size == 4) {
                    val point = Point(parts[1], parts[2].toDouble(), parts[3].toDouble())
                    map.put(point.id, point)
                }
            }
        }
    }

    private fun writeToFile(filename: String, data: List<Point>) {
        val file = java.io.File(filename)
        file.printWriter().use { out -> data.forEach { out.println(it) } }
    }

    fun union(output: String) {
        val result = mutableMapOf<String, Point>()
        for (entry in points1) result[entry.key] = entry.value
        for (entry in points2) result[entry.key] = entry.value
        writeToFile(output, result.values.toList())
    }

    fun intersection(output: String) {
        val result = points1.filter { (k, _) -> points2[k] != null }
        writeToFile(output, result.values.toList())
    }

    fun difference(output: String) {
        val result = points1.filter { (k, _) -> points2[k] == null }
        writeToFile(output, result.values.toList())
    }

    fun run() {
        while (true) {
            print("> ")
            val input = readlnOrNull()?.split(" ") ?: break
            when (input.firstOrNull()) {
                "load" -> if (input.size == 3) load(input[1], input[2])
                "union" -> if (input.size == 2) union(input[1])
                "intersection" -> if (input.size == 2) intersection(input[1])
                "difference" -> if (input.size == 2) difference(input[1])
                "exit" -> return
                else -> println("Comando inválido.")
            }
        }
    }
}