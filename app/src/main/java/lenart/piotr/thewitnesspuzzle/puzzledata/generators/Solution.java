package lenart.piotr.thewitnesspuzzle.puzzledata.generators;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;

public class Solution {
    private final IPuzzle puzzle;
    private final IPath path;
    public Solution(IPuzzle puzzle, IPath path) {
        this.puzzle = puzzle;
        this.path = path;
    }

    public IPath getPath() {
        return path;
    }
    public IPuzzle getPuzzle() {
        return puzzle;
    }
}
