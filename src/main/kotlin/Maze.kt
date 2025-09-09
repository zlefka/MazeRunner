package mazerunner

import java.io.File
import kotlin.random.Random

class Maze() {
    private lateinit var maze: Array<IntArray>

    fun hasMaze(): Boolean = ::maze.isInitialized

    fun generateNewMaze(size: Int) {
        val actualSize = if (size % 2 == 0) size + 1 else size

        maze = Array(actualSize) { IntArray(actualSize) { 1 } }

        val start = Point(Random.nextInt(1, actualSize - 1), 0)
        val exit = Point(actualSize - 2, actualSize - 1)

        maze[start.x][start.y] = 0
        maze[exit.x][exit.y - 1] = 0



        generateMaze(start.x, start.y, actualSize)

        maze[exit.x][exit.y] = 0
        maze[exit.x][exit.y - 1] = 0

    }

    private fun generateMaze(x: Int, y: Int, size: Int) {
        maze[x][y] = 0

        val directions = Direction.entries.shuffled()

        for (dir in directions) {
            val newX = x + dir.dx * 2
            val newY = y + dir.dy * 2
            if (newX in 1 until size - 1 && newY in 1 until size - 1 && maze[newX][newY] == 1) {
                maze[x + dir.dx][y + dir.dy] = 0
                generateMaze(newX, newY, size)
            }
        }
    }

    fun loadMaze() {
        val path = readln()
        val file = File(path)

        if (!file.exists()) {
            println("The file $path does not exist")
            return
        }

        val error = "Cannot load the maze. It has an invalid format"
        try {
            val lines = file.readLines()

            if (lines.isEmpty()) {
                println(error)
                return
            }

            val loadedMaze = lines.map { line ->
                line.trim().split(" ").map { section ->
                    when (section) {
                        "1"  -> 1
                        "0"  -> 0
                        else -> throw IllegalArgumentException()
                    }
                }.toIntArray()
            }.toTypedArray()

            val size = loadedMaze.size

            if (loadedMaze.any { it.size != size }) {
                println(error)
                return
            }

            maze = loadedMaze
        } catch (_: Exception) {
            println(error)
        }
    }

    fun saveMaze() {
        val currentMaze = maze
        val path = readln()
        File(path).printWriter().use { out ->
            currentMaze.forEach { row ->
                out.println(row.joinToString(" "))
            }
        }
    }

    fun displayMaze() {
        val currentMaze = maze
        currentMaze.forEach { row ->
            row.forEach {
                print(if (it == 1) "\u2588\u2588" else "  ")
            }
            println()
        }
    }

    fun findEscape() {
        val currentMaze = maze
        val size = currentMaze.size
        val pathMaze = currentMaze.map { it.copyOf() }.toTypedArray()
        val startX = maze.indexOfFirst { it[0] == 0 }
        if (startX == -1) return

        val start = Point(startX, 0)

        var exitX = maze.indexOfFirst { it[size - 1] == 0 }

        if (exitX == -1) return

        val exit = Point(exitX, size - 1)
        dfs(start, pathMaze, exit, size)

        printEscape(size, pathMaze)
    }

    private fun printEscape(size: Int, pathMaze: Array<IntArray>) {
        for (i in 0 until size) {
            for (j in 0 until size) {
                print(
                    when (pathMaze[i][j]) {
                        1    -> "\u2588\u2588"  // wall
                        2    -> "//"  // path
                        else -> "  "  // way
                    }
                )
            }
            println()
        }
    }

    private fun dfs(
        current: Point,
        pathMaze: Array<IntArray>,
        exit: Point,
        size: Int
    ): Boolean {
        if (current.x !in 0 until size || current.y !in 0 until size) return false

        if (pathMaze[current.x][current.y] != 0) return false

        if (current == exit) {
            pathMaze[current.x][current.y] = 2
            return true
        }

        pathMaze[current.x][current.y] = 3

        for (dir in Direction.entries) {
            val next = Point(current.x + dir.dx, current.y + dir.dy)
            if (dfs(next, pathMaze, exit, size)) {
                pathMaze[current.x][current.y] = 2
                return true
            }
        }

        pathMaze[current.x][current.y] = 0
        return false
    }

}