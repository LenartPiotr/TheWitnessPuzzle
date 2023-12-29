package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces;

import android.graphics.Canvas;

public interface IDrawableComponent {
    void draw(Canvas canvas, int pixelsPerPart, int marginTop, int marginLeft);
}
