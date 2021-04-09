package mastermind

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class MastermindCheckerTest {
    lateinit var mastermindChecker: MastermindChecker;

    @BeforeEach
    fun setUp() {
        mastermindChecker = MastermindChecker();
    }

    @ParameterizedTest
    @MethodSource("mastermind.MastermindCheckerCombinationsProvider#buildValidCombinations")
    fun checkValidCombination(attempt: String, solution: String) {
        val mastermindState = mastermindChecker.check(attempt, solution)

        assertSame(4, mastermindState.placed)
        assertSame(0, mastermindState.present)
    }

    @ParameterizedTest
    @MethodSource("mastermind.MastermindCheckerCombinationsProvider#buildInvalidCombinations")
    fun checkInvalidCombination(attempt: String, solution: String) {
        val mastermindState = mastermindChecker.check(attempt, solution)

        assertSame(0, mastermindState.placed)
        assertSame(0, mastermindState.present)
    }

    @ParameterizedTest
    @MethodSource("mastermind.MastermindCheckerCombinationsProvider#buildPartiallyValidCombinations")
    fun checkPartiallyValidCombination(attempt: String, solution: String, placed: Int, present: Int) {
        val mastermindState = mastermindChecker.check(attempt, solution)

        assertSame(placed, mastermindState.placed)
        assertSame(present, mastermindState.present)
    }
}

class MastermindCheckerCombinationsProvider {
    companion object {
        @JvmStatic
        fun buildValidCombinations(): Stream<Arguments> {
            return Stream.of(
                arguments("ABCD", "ABCD"),
                arguments("AAAA", "AAAA"),
                arguments("ABAB", "ABAB")
            )
        }

        @JvmStatic
        fun buildInvalidCombinations(): Stream<Arguments> {
            return Stream.of(
                arguments("AAAA", "BBBB"),
                arguments("ABAB", "CDCD")
            )
        }

        @JvmStatic
        fun buildPartiallyValidCombinations(): Stream<Arguments> {
            return Stream.of(
                arguments("ABCD", "ACBD", 2, 2),
                arguments("ABAB", "ABBC", 2, 1),
                arguments("ABCA", "BDDD", 0, 1)
            )
        }
    }
}