package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces;

import android.graphics.Canvas;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleDisplay;

public interface IDrawableComponent {
    void draw(SquarePuzzleDisplay display, Canvas canvas, int pixelsPerPart, int marginTop, int marginLeft);
}
