package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle;

import android.content.Context;

import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;

public interface IPuzzle {
    IViewPuzzle createViewPuzzle(Context context, PuzzleCanvas canvas);
}
