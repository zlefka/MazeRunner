package mazerunner

data class Point(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1)
}