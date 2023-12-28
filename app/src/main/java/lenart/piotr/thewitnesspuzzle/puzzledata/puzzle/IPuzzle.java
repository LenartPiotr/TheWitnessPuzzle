package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle;

import android.content.Context;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;

public interface IPuzzle {
    IViewPuzzle createViewPuzzle(Context context, PuzzleCanvas canvas);
    boolean isMatching(IPath iPath) throws WrongComponentException;
}
