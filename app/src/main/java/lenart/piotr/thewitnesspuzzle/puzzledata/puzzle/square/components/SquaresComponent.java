package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleDisplay;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IDrawableComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SquaresComponent implements IComponent, IDrawableComponent {

    List<Square> squares = new ArrayList<>();
    Random rand = new Random();

    public SquaresComponent() { }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(squares.size());
        for (Square s : squares) {
            parcel.writeInt(s.pos.x);
            parcel.writeInt(s.pos.y);
            parcel.writeInt(s.color);
        }
    }

    @Override
    public void readFromParcel(@NonNull Parcel in) {
        squares.clear();
        int s = in.readInt();
        for (int i = 0; i < s; i++) {
            squares.add(new Square(new Vector2i(in.readInt(), in.readInt()), in.readInt()));
        }
    }

    @Override
    public boolean isMatching(SquarePuzzle puzzle, Path path, List<Sector> sectors) {
        for (Sector s : sectors) {
            int sectorColor = -1;
            for (Vector2i position : s.getFields()) {
                for (Square square : squares) {
                    if (!square.pos.equals(position)) continue;
                    if (sectorColor == -1) {
                        sectorColor = square.color;
                        continue;
                    }
                    if (sectorColor != square.color) return false;
                }
            }
        }
        return true;
    }

    @Override
    public void reset() {
        squares.clear();
    }

    @Override
    public void addRandomElement(SquarePuzzle puzzle, Path path, List<Sector> sectors, int percent) {
        int maxColors = rand.nextInt(2) + 2;
        class SectorData {
            Sector sector;
            int color;
            List<Vector2i> freePoints;
            SectorData(Sector sector) {
                this.sector = sector;
                freePoints = sector.getFreeFields(puzzle).stream().map(Vector2i::clone).collect(Collectors.toList());
                Collections.shuffle(freePoints);
                color = rand.nextInt(maxColors);
            }
        }

        List<SectorData> s = sectors.stream().map(SectorData::new).collect(Collectors.toList());
        int countAll = 0;
        for (SectorData sd : s) countAll += sd.freePoints.size();
        double progress = 0;
        double progressPerStep = 100.0 / countAll;
        while (progress < percent && s.size() > 0) {
            int randSector = rand.nextInt(s.size());
            SectorData sectorData = s.get(randSector);
            Vector2i p1 = sectorData.freePoints.get(0);
            sectorData.freePoints.remove(p1);
            if (sectorData.freePoints.size() == 0) s.remove(sectorData);
            int color = sectorData.color;
            squares.add(new Square(p1, color));
            if (!puzzle.reserveField(p1)) return;
            progress += progressPerStep;
        }
    }

    @Override
    public void draw(SquarePuzzleDisplay display, Canvas canvas, int pixelsPerPart, int marginTop, int marginLeft) {
        Paint paint = new Paint();
        paint.setStrokeWidth(pixelsPerPart);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (Square square : squares) {
            display.setColor(paint, square.color);
            Vector2i center = new Vector2i(
                    (square.pos.x * 3 + 3) * pixelsPerPart + marginLeft,
                    (square.pos.y * 3 + 3) * pixelsPerPart + marginTop
            );
            drawSquare(canvas, center, pixelsPerPart, paint);
        }
    }

    private void drawSquare(Canvas canvas, Vector2i point, int pixelsPerPart, Paint paint) {
        double scale = 0.9;
        int del = (int)(pixelsPerPart * scale / 2);
        int radius = pixelsPerPart / 4;
        canvas.drawRoundRect(point.x - del, point.y - del, point.x + del, point.y + del, radius, radius, paint);
    }

    protected static class Square {
        Vector2i pos;
        int color;
        Square(Vector2i p, int col) { pos = p; color = col; }
    }
}
