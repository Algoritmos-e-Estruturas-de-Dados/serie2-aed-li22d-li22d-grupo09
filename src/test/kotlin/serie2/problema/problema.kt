package serie2.problema

fun testProcessPointsCollections() {
    val f1 = "test1.co"
    val f2 = "test2.co"
    val outUnion = "out_union.co"
    val outIntersection = "out_intersection.co"
    val outDifference = "out_difference.co"

    java.io.File(f1).printWriter().use {
        it.println("v A 1.0 1.0")
        it.println("v B 2.0 2.0")
        it.println("v C 3.0 3.0")
    }
    java.io.File(f2).printWriter().use {
        it.println("v B 2.0 2.0")
        it.println("v C 3.0 3.0")
        it.println("v D 4.0 4.0")
    }

    ProcessPointsCollections.load(f1, f2)
    ProcessPointsCollections.union(outUnion)
    ProcessPointsCollections.intersection(outIntersection)
    ProcessPointsCollections.difference(outDifference)

    println("-- Conteúdo de $outUnion --")
    println(java.io.File(outUnion).readText())

    println("-- Conteúdo de $outIntersection --")
    println(java.io.File(outIntersection).readText())

    println("-- Conteúdo de $outDifference --")
    println(java.io.File(outDifference).readText())
}


// ---------- Main ----------
fun main() {
    testProcessPointsCollections()
}