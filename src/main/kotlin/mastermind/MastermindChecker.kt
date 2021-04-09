package mastermind

fun check(attempt: String, solution: String): MastermindState {
    val attemptChecker = MastermindAttemptChecker(solution)
    return attemptChecker.process(attempt)
}

data class MastermindState(val placed: Int = 0, val misplaced: Int = 0) {
    fun addPlaced() = MastermindState(placed + 1, misplaced)
    fun addMisplaced() = MastermindState(placed, misplaced + 1)
}

class MastermindAttemptChecker(solution: String) {
    private val solutionPlaces: MastermindSolutionPlaces = MastermindSolutionPlaces(solution)

    private enum class PlacementType { Placed, Misplaced, Absent }

    fun process(attempt: String): MastermindState {
        return attempt.foldIndexed(MastermindState()) { index, mastermindState, char ->
            when (processChar(index, char)) {
                PlacementType.Placed -> mastermindState.addPlaced()
                PlacementType.Misplaced -> mastermindState.addMisplaced()
                PlacementType.Absent -> mastermindState
            }
        }
    }

    private fun processChar(index: Int, char: Char): PlacementType {
        val charPlacementType = when {
            solutionPlaces.isPlaced(index, char) -> PlacementType.Placed
            solutionPlaces.isPresent(char) -> PlacementType.Misplaced
            else -> PlacementType.Absent
        }
        solutionPlaces.addSeen(char)
        return charPlacementType
    }
}

class MastermindSolutionPlaces(private val solution: String) {
    data class MastermindItemWithPlaces(val char: Char, private val total: Int = 0) {
        private var seen: Int = 0

        fun hasUnseen() = total - seen > 0
        fun addSeen() = seen++
    }

    private val itemsWithPlaces: List<MastermindItemWithPlaces> = solution.groupBy { it }
        .map { (char, chars) -> MastermindItemWithPlaces(char, chars.size) }

    fun isPlaced(index: Int, char: Char) = solution[index] == char
    fun isPresent(char: Char) = getItem(char)?.hasUnseen() ?: false
    fun addSeen(char: Char) = getItem(char)?.addSeen()

    private fun getItem(char: Char): MastermindItemWithPlaces? {
        return itemsWithPlaces.firstOrNull { it.char == char }
    }
}