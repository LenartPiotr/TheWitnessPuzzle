package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleDisplay;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleSectorsBuilder;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleSectorsBuilder.SectorData;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IDrawableComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.ISectorsComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SunsComponent implements IComponent, IDrawableComponent, ISectorsComponent {

    List<Sun> suns = new ArrayList<>();
    Random rand = new Random();
    SquarePuzzleSectorsBuilder sectorsBuilder;

    public SunsComponent() { }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(suns.size());
        for (Sun s : suns) {
            parcel.writeInt(s.pos.x);
            parcel.writeInt(s.pos.y);
            parcel.writeInt(s.color);
        }
    }

    @Override
    public void readFromParcel(@NonNull Parcel in) {
        suns.clear();
        int s = in.readInt();
        for (int i = 0; i < s; i++) {
            suns.add(new Sun(new Vector2i(in.readInt(), in.readInt()), in.readInt()));
        }
    }

    @Override
    public boolean isMatching(SquarePuzzle puzzle, Path path, List<Sector> sectors) {
        for (Sector s : sectors) {
            Set<Integer> sunsColors = new HashSet<>();
            Map<Integer, Integer> colors = new HashMap<>();
            for (Vector2i position : s.getFields()) {
                for (Sun sun : suns) {
                    if (sun.pos.equals(position)) {
                        sunsColors.add(sun.color);
                        break;
                    }
                }
                int puzzleColor = puzzle.getFieldColor(position);
                colors.put(puzzleColor, colors.getOrDefault(puzzleColor, 0) + 1);
            }
            for (int val : sunsColors) {
                if (colors.get(val) != 2) return false;
            }
        }
        return true;
    }

    @Override
    public void reset() {
        suns.clear();
    }

    @Override
    public void addRandomElement(SquarePuzzle puzzle, Path path, int percent) {
        List<SectorData> sectors = sectorsBuilder.getSectors().stream().filter(s -> s.getFreeCount() > 1).collect(Collectors.toList());
        int countAll = 0;
        int sunsPutted = 0;
        for (SectorData sd : sectors) countAll += (sd.getFreeCount() / 2) * 2;
        while (sunsPutted < countAll * percent / 100.0 && sectors.size() > 0) {
            int randSector = rand.nextInt(sectors.size());
            SectorData sectorData = sectors.get(randSector);
            int color = sectorData.getColorContainsLessOrEqualsFieldsThan(1);

            Vector2i p1 = sectorData.getRandomFreeField();
            if (sectorData.countFieldsWithColor(color) == 0) {
                Vector2i p2 = sectorData.getRandomFreeField(p1);
                sectorData.putElement(p2, color);
                suns.add(new Sun(p2, color));
                sunsPutted++;
            }
            sectorData.putElement(p1, color);
            sectorData.lockColor(color);
            suns.add(new Sun(p1, color));
            sunsPutted++;

            if (sectorData.getFreeCount() < 2) sectors.remove(sectorData);
        }
    }

    @Override
    public void setRandom(Random random) {
        this.rand = random;
    }

    @Override
    public void setSectorsBuilder(SquarePuzzleSectorsBuilder sectorsBuilder) {
        this.sectorsBuilder = sectorsBuilder;
    }

    protected static class Sun {
        Vector2i pos;
        int color;
        Sun(Vector2i p, int col) { pos = p; color = col; }
    }

    @Override
    public void draw(SquarePuzzleDisplay display, Canvas canvas, int pixelsPerPart, int marginTop, int marginLeft) {
        Paint paint = new Paint();
        paint.setStrokeWidth(pixelsPerPart);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (Sun sun : suns) {
            display.setColor(paint, sun.color);
            Vector2i center = new Vector2i(
                    (sun.pos.x * 3 + 3) * pixelsPerPart + marginLeft,
                    (sun.pos.y * 3 + 3) * pixelsPerPart + marginTop
            );
            drawSun(canvas, center, pixelsPerPart, paint);
        }
    }

    private void drawSun(Canvas canvas, Vector2i point, int pixelsPerPart, Paint paint) {
        Matrix matrix = new Matrix();
        matrix.postRotate(45, point.x, point.y);

        double scale = 0.9;
        int del = (int)(pixelsPerPart * scale / 2);

        canvas.save();
        canvas.setMatrix(matrix);
        canvas.drawRect(point.x - del, point.y - del, point.x + del, point.y + del, paint);
        canvas.restore();
        canvas.drawRect(point.x - del, point.y - del, point.x + del, point.y + del, paint);
    }
}
