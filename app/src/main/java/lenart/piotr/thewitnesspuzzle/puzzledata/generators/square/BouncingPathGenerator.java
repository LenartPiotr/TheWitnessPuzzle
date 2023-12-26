package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;

public class BouncingPathGenerator implements IPathGenerator {
    @Override
    public IPath generate(IPuzzle ipuzzle) throws WrongComponentException {
        if (!(ipuzzle instanceof SquarePuzzle)) throw new WrongComponentException(this, SquarePuzzle.class, ipuzzle);
        SquarePuzzle puzzle = (SquarePuzzle) ipuzzle;
        int width = puzzle.getWidth();
        int height = puzzle.getHeight();



        return null;
    }
}
