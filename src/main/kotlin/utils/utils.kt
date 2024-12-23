package utils

class Grid<T>(private val contents: List<List<T>>) : Iterable<T> {
    private val flattened = contents.flatten()
    val hSize = contents.size
    val vSize = contents.first().size
    val indices: List<Coordinate> = contents.indices.flatMap { i ->
        contents[i].indices.map { j -> i to j }
    }

    operator fun get(i: Int, j: Int) = contents[i][j]

    operator fun get(i: Int) = contents[i]

    operator fun get(coord: Coordinate) = contents[coord.first][coord.second]

    fun getOrNull(i: Int, j: Int) = contents.getOrNull(i)?.getOrNull(j)

    override fun iterator(): Iterator<T> = flattened.iterator()

    override fun toString(): String = contents.joinToString(separator = "\n")

    /*
    Returns a new grid with the value at position (i, j) replaced by 'value'
     */
    fun set(i: Int, j: Int, value: T) = contents.toMutableList().apply {
        this[i] = this[i].toMutableList().also { it[j] = value }
    }.toGrid()

    fun set(coord: Coordinate, value: T): Grid<T> = set(coord.first, coord.second, value)

    fun switch(first: Coordinate, second: Coordinate) = set(first, this[second]).set(second, this[first])

    fun indexOf(element: T): Coordinate? = contents.indexOfFirst { it.contains(element) }.takeIf { it >= 0 }?.let { row ->
        row to contents[row].indexOf(element)
    }

    fun map(block: (T) -> T): Grid<T> = contents.map { row -> row.map(block)}.toGrid()

    fun flatMap(block: (T) -> List<T>): Grid<T> = contents.map { row -> row.flatMap(block) }.toGrid()
}

fun<T> List<List<T>>.toGrid() = Grid(this)

fun String.toGrid() = split('\n').map { it.toList() }.toGrid()

typealias Coordinate = Pair<Int, Int>

typealias LongCoordinate = Pair<Long, Long>

fun Coordinate.getAdjacents() = let { (i, j) -> listOf(i-1 to j, i to j+1, i+1 to j, i to j-1) }

fun<T> Coordinate.isWithinGrid(grid: Grid<T>) = x >= 0 && x < grid.vSize && y >= 0 && y < grid.hSize

val <N: Number>Pair<N, N>.x: N
    get() = first

val <N: Number>Pair<N, N>.y: N
    get() = second

enum class Direction{
    UP, RIGHT, DOWN, LEFT
}

fun Coordinate.getAdjacent(direction: Direction) = when (direction) {
    Direction.UP -> x-1 to y
    Direction.RIGHT -> x to y+1
    Direction.DOWN -> x+1 to y
    Direction.LEFT -> x to y-1
}

fun String.stripWindowsLineFeed() = replace("\r", "")

//fun <T> applyDijksta(nodes: List<T>, edges: Map<T, List<Pair<T, Long>>>, startPoint: T, endPoint: T?) {
//    val visitedNodes = mutableListOf<Pair<T, Long>>()
//    val reachableNodes: MutableList<Pair<T, Long>> = mutableListOf(startPoint to 0)
//
//    fun <T> dijkstraRecursive(visitedNodes: List<Pair<T, Long>>, reachableNodes: List<Pair<T, Long>>): Pair<List<Pair<T, Long>>, List<Pair<T, Long>>> {
//        val nextNode = reachableNodes.minByOrNull { it.second }
//        edges[nextNode.first]!!.forEach {
//
//        }
//    }
//
//    fun step() {
//        val nextNode = reachableNodes.minByOrNull { it.second }
//    }
//}




fun main() {
    val grid = Grid(listOf(listOf(1, 2), listOf(3, 4)))
    println(grid)
}