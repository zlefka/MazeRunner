import kotlin.random.Random

fun main() {
    println("Please, enter the size of a maze")
    val (height, width) = readln().split(" ").map { it.toInt() }
    val h = if (height % 2 == 0) height + 1 else height
    val w = if (width % 2 == 0) width + 1 else width

    val maze = Array(h) { IntArray(w) { 1 } }

    val start = Pair(Random.nextInt(1, h - 1), 0)
    val exit = Pair(h - 2, w - 1)

    maze[start.first][start.second] = 0
    maze[exit.first][exit.second - 1] = 0

    val startX = start.first
    val startY = start.second + 1


    generateMaze(startX, startY, maze, h, w, exit)

    for (i in 0 until h) {
        for (j in 0 until w) {
            if (maze[i][j] == 1) print("\u2588\u2588") else print("  ")
        }
        println()
    }

}

fun generateMaze(x: Int, y: Int, maze: Array<IntArray>, height: Int, width: Int, exit: Pair<Int, Int>) {
    maze[x][y] = 0

    val directions = listOf(
        Pair(-2, 0),
        Pair(0, -2),
        Pair(2, 0),
        Pair(0, 2),
    ).shuffled()

    for ((dirX, dirY) in directions) {
        val newX = x + dirX
        val newY = y + dirY
        if (newX in 1 until height - 1 && newY in 1 until exit.second && maze[newX][newY] == 1) {
            maze[x + dirX / 2][y + dirY / 2] = 0
            generateMaze(newX, newY, maze, height, width, exit)
        }
    }

    maze[exit.first][exit.second] = 0
    maze[exit.first][exit.second - 1] = 0
    maze[exit.first][exit.second - 2] = 0
}
