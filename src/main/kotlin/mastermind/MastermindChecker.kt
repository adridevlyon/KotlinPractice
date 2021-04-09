package mastermind

fun check(attempt: String, solution: String): MastermindState {
    val mastermindState = MastermindState(solution = solution)
    attempt.withIndex().forEach { (index, char) -> mastermindState.process(index, char) }
    return mastermindState
}

class MastermindState(val solution: String, var placed: Int = 0, var present: Int = 0) {
    fun process(index: Int, char: Char) {
        when {
            isPlaced(index, char) -> addPlaced(char)
            isPresent(char) -> addPresent(char)
        }
    }

    private fun isPlaced(index: Int, char: Char): Boolean {
        return solution[index] == char
    }

    private fun addPlaced(char: Char) {
        placed++
        removeCharFromCounts(char)
    }

    private fun isPresent(char: Char): Boolean {
        return solutionCountsByChar.getOrElse(char) { 0 } > 0
    }

    private fun addPresent(char: Char) {
        present++
        removeCharFromCounts(char)
    }

    private fun removeCharFromCounts(char: Char) {
        if (char !in solutionCountsByChar) return
        if (solutionCountsByChar[char] == 0) return

        solutionCountsByChar[char] = solutionCountsByChar.getOrElse(char) { 0 } - 1
    }

    private val solutionCountsByChar: MutableMap<Char, Int> = computeCountsByChar(solution)
    private fun computeCountsByChar(solution: String): MutableMap<Char, Int> {
        return mutableMapOf(
            *solution.groupBy { it }
                .map { (char, chars) -> char to chars.size }
                .toTypedArray()
        )
    }
}