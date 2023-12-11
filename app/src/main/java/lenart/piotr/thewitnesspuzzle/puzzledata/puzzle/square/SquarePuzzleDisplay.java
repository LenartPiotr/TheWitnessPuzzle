package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SquarePuzzleDisplay implements IViewPuzzle {

    protected SquarePuzzle puzzle;
    protected Path path;
    protected PuzzleCanvas puzzleCanvas;

    protected SquarePuzzleDisplay(SquarePuzzle puzzle, PuzzleCanvas puzzleCanvas) {
        this.puzzle = puzzle;
        this.puzzleCanvas = puzzleCanvas;
    }

    public void setPath(Path path) {
        this.path = path;
        puzzleCanvas.redraw();
    }

    @Override
    public void draw(Canvas canvas) {
        // lines background #3D2DC7 61/45/199
        // lines front #B6DAF7 182/218/247
        // background #5653FF 86/83/255

        // -- Draw grid --

        Paint paintLinesBg = new Paint();
        paintLinesBg.setColor(Color.rgb(61, 45, 199));

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int requiredPartsHorizontal = puzzle.width * 3 + 3;
        int requiredPartsVertical = puzzle.height * 3 + 3;

        int pixelsPerPartHorizontal = canvasWidth / requiredPartsHorizontal;
        int pixelsPerPartVertical = canvasHeight / requiredPartsVertical;
        int pixelsPerPart = Math.min(pixelsPerPartVertical, pixelsPerPartHorizontal);

        paintLinesBg.setStrokeWidth(pixelsPerPart);
        paintLinesBg.setStrokeCap(Paint.Cap.ROUND);

        int marginTop = 0;
        int marginLeft = 0;
        if (pixelsPerPartVertical < pixelsPerPartHorizontal) {
            marginLeft = (canvasWidth - (pixelsPerPart * requiredPartsHorizontal)) / 2;
        } else {
            marginTop = (canvasHeight - (pixelsPerPart * requiredPartsVertical)) / 2;
        }

        for (int x = 0; x <= puzzle.width; x++) {
            canvas.drawRoundRect(
                    new RectF(
                            (x * 3 + 1) * pixelsPerPart + marginLeft,
                            pixelsPerPart + marginTop,
                            (x * 3 + 2) * pixelsPerPart + marginLeft,
                            pixelsPerPart * (puzzle.height * 3 + 2) + marginTop),
                    pixelsPerPart / 2, pixelsPerPart / 2, paintLinesBg);
        }
        for (int y = 0; y <= puzzle.height; y++) {
            canvas.drawRoundRect(
                    new RectF(
                            pixelsPerPart + marginLeft,
                            (y * 3 + 1) * pixelsPerPart + marginTop,
                            pixelsPerPart * (puzzle.width * 3 + 2) + marginLeft,
                            (y * 3 + 2) * pixelsPerPart + marginTop),
                    pixelsPerPart / 2, pixelsPerPart / 2, paintLinesBg);
        }

        // == Draw start and end points --

        int pixelsPerDot = (int)(pixelsPerPart * 0.5);
        for (Vector2i point : puzzle.startPoints) {
            canvas.drawRoundRect(
                    new RectF(
                            (point.x * 3 + 1) * pixelsPerPart + marginLeft - pixelsPerDot,
                            (point.y * 3 + 1) * pixelsPerPart + marginTop - pixelsPerDot,
                            (point.x * 3 + 2) * pixelsPerPart + marginLeft + pixelsPerDot,
                            (point.y * 3 + 2) * pixelsPerPart + marginTop + pixelsPerDot
                    ), pixelsPerPart / 2 + pixelsPerDot, pixelsPerPart / 2 + pixelsPerDot, paintLinesBg);
        }

        // Ends
        for (Vector2i point : puzzle.endPoints) {
            Vector2i from = new Vector2i(
                    (point.x * 3 + 1) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                    (point.y * 3 + 1) * pixelsPerPart + marginTop + pixelsPerPart / 2
            );
            Vector2i to = new Vector2i(
                    (point.x * 3) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                    (point.y * 3) * pixelsPerPart + marginTop + pixelsPerPart / 2
            );
            boolean left = point.x == 0;
            boolean right = point.x == puzzle.width;
            boolean top = point.y == 0;
            boolean bottom = point.y == puzzle.height;
            if (top) {
                to = new Vector2i(
                        (point.x * 3 + 1) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        marginTop + pixelsPerPart / 2
                );
            }
            if (bottom) {
                to = new Vector2i(
                        (point.x * 3 + 1) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3 + 2) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (left) {
                to = new Vector2i(
                        marginLeft + pixelsPerPart / 2,
                        (point.y * 3 + 1) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (right) {
                to = new Vector2i(
                        (point.x * 3 + 2) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3 + 1) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (top && left) {
                to = new Vector2i(
                        (point.x * 3) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (top && right) {
                to = new Vector2i(
                        (point.x * 3 + 2) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (bottom && left) {
                to = new Vector2i(
                        (point.x * 3) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3 + 2) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            if (bottom && right) {
                to = new Vector2i(
                        (point.x * 3 + 2) * pixelsPerPart + marginLeft + pixelsPerPart / 2,
                        (point.y * 3 + 2) * pixelsPerPart + marginTop + pixelsPerPart / 2
                );
            }
            canvas.drawLine(from.x, from.y, to.x, to.y, paintLinesBg);
        }
    }
}
