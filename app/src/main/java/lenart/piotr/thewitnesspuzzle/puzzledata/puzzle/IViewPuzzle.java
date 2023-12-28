package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle;

import android.graphics.Canvas;

import lenart.piotr.thewitnesspuzzle.puzzledata.paths.square.Path;
import lenart.piotr.thewitnesspuzzle.utils.callbacks.ICallback1;

public interface IViewPuzzle {
    void draw(Canvas canvas);
    void enableDrawing(ICallback1<Path> drawPathCallback);
    void clearPath();
    void destroy();
}
