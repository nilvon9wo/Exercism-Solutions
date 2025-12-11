import java.util.*;

class Alphametics {
    private final String puzzleInput;
    private final PuzzleFactory parser;
    private final AlphaSolver solver;
    Alphametics(String puzzleInput, PuzzleFactory parser, AlphaSolver solver) {
        this.puzzleInput = puzzleInput;
        this.parser = parser;
        this.solver = solver;
    }

    Alphametics(String puzzleInput) {
        this(puzzleInput, new PuzzleFactory(), new AlphaSolver());
    }

    public Map<Character, Integer> solve() throws UnsolvablePuzzleException {
        Puzzle puzzle = this.parser.parsePuzzle(puzzleInput);
        SolverContext solverContext = new SolverContext(puzzle);
        boolean solved = this.solver.assignDigitsDepthFirst(solverContext, 0);
        if (!solved) {
            throw new UnsolvablePuzzleException();
        }

        return solverContext.valueByLetters();
    }
}
