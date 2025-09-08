package mazerunner

import java.io.File
import kotlin.random.Random

class Maze() {
    private lateinit var maze: Array<IntArray>

    fun hasMaze(): Boolean = ::maze.isInitialized

    fun generateNewMaze(size: Int) {
        val actualSize = if (size % 2 == 0) size + 1 else size

        maze = Array(actualSize) { IntArray(actualSize) { 1 } }

        val start = Pair(Random.nextInt(1, actualSize - 1), 0)
        val exit = Pair(actualSize - 2, actualSize - 1)

        maze[start.first][start.second] = 0
        maze[exit.first][exit.second - 1] = 0



        generateMaze(start.first, start.second, actualSize)

        maze[exit.first][exit.second] = 0
        maze[exit.first][exit.second - 1] = 0

    }

    private fun generateMaze(x: Int, y: Int, param: Int) {
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
            if (newX in 1 until param - 1 && newY in 1 until param - 1 && maze[newX][newY] == 1) {
                maze[x + dirX / 2][y + dirY / 2] = 0
                generateMaze(newX, newY, param)
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
                        "1" -> 1
                        "0" -> 0
                        else -> throw IllegalArgumentException()
                    }
                }.toIntArray()
            }.toTypedArray() // преобразует List<IntArray> в Array<IntArray>

            val size = loadedMaze.size

            if (loadedMaze.any { it.size != size }) { // проверка квадратности лабиринта
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
        var startX = -1
        for (i in 0 until size) {
            if (currentMaze[i][0] == 0) {
                startX = i
                break
            }
        }
        if (startX == -1) return // нет старта

        val start = Pair(startX, 0)

        var exitX = -1
        for (i in 0 until size) {
            if (currentMaze[i][size - 1] == 0) {
                exitX = i
                break
            }
        }
        if (exitX == -1) return // нет выхода

        val exit = Pair(exitX, size - 1)
        dfs(start.first, start.second, pathMaze, exit, size)

        for (i in 0 until size) {
            for (j in 0 until size) {
                print(
                    when (pathMaze[i][j]) {
                        1 -> "\u2588\u2588"   // стена
                        2 -> "//"             // путь
                        else -> "  "          // пустая клетка
                    }
                )
            }
            println()
        }
    }

    private fun dfs(x: Int, y: Int, pathMaze: Array<IntArray>, exit: Pair<Int, Int>, size: Int, ): Boolean {
        if (x !in 0 until size || y !in 0 until size) return false
        if (pathMaze[x][y] != 0) return false // стена или уже посещено
        if (x == exit.first && y == exit.second) { // достигли выхода
            pathMaze[x][y] = 2 // пометка пути
            return true
        }

        pathMaze[x][y] = 2 // временная отметка

        val directions = listOf(Pair(-1,0), Pair(1,0), Pair(0,-1), Pair(0,1))
        for ((dx, dy) in directions) {
            if (dfs(x + dx, y + dy, pathMaze, exit, size)) return true
        }

        pathMaze[x][y] = 0 // откат, если путь не найден
        return false
    }

}