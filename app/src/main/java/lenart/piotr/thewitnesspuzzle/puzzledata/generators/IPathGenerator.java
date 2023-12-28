package lenart.piotr.thewitnesspuzzle.puzzledata.generators;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;

public interface IPathGenerator {
    public IPath generate(IPuzzle puzzle) throws WrongComponentException;
}
