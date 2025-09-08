
package mazerunner

fun main() {
    val maze = Maze()
    var input: Int = -1
    do {
        val print1 = " === Menu ===\n1. Generate a new maze\n2. Load a maze\n0. Exit"
        val print2 = "=== Menu ===\n1. Generate a new maze\n2. Load a maze\n" +
                "3. Save the maze\n4. Display the maze\n5. Find the escape\n0. Exit"

        if (maze.hasMaze()) println(print2) else println(print1)

        input = try {
            readln().toInt()
        } catch (e: Exception) {
            println("Incorrect option. Please try again")
            continue
        }

        when (input) {
            1    -> {
                println("Enter the size of a new maze")
                val size = try {
                    readln().toInt()
                } catch (e: Exception) {
                    println("Incorrect option. Please try again")
                    continue
                }
                maze.generateNewMaze(size)
                maze.displayMaze()
            }
            2    -> maze.loadMaze()
            3    -> if (maze.hasMaze()) maze.saveMaze()
            4    -> if (maze.hasMaze()) maze.displayMaze()
            5    -> if (maze.hasMaze()) maze.findEscape()
            0    -> println("Bye!")
            else -> println("Incorrect option. Please try again")
        }
    } while (input != 0)
}