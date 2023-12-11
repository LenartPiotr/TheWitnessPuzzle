package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle;

import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;

public interface IPuzzle {
    IViewPuzzle createViewPuzzle(PuzzleCanvas canvas);
}
