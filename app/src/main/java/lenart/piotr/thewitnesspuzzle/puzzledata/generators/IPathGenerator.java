package lenart.piotr.thewitnesspuzzle.puzzledata.generators;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;

public interface IPathGenerator {
    IPath generate(IPuzzle puzzle) throws WrongComponentException;
}
