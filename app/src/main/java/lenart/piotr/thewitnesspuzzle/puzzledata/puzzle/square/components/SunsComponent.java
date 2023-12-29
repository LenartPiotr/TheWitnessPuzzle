package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components;

import android.graphics.Canvas;
import android.graphics.Color;
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
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IDrawableComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SunsComponent implements IComponent, IDrawableComponent {

    List<Sun> suns = new ArrayList<>();
    Random rand = new Random();

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
            Map<Integer, Integer> sunsCount = new HashMap<>();
            for (Vector2i position : s.getFields()) {
                for (Sun sun : suns) {
                    if (!sun.pos.equals(position)) continue;
                    sunsCount.put(sun.color, sunsCount.getOrDefault(sun.color, 0) + 1);
                }
            }
            for (int val : sunsCount.values()) {
                if (val != 0 && val != 2) return false;
            }
        }
        return true;
    }

    @Override
    public void reset() {
        suns.clear();
    }

    @Override
    public void addRandomElement(SquarePuzzle puzzle, Path path, List<Sector> sectors, int percent) {
        class SectorData {
            Sector sector;
            List<Vector2i> freePoints;
            int nextSunColor = 0;
            SectorData(Sector sector) {
                this.sector = sector;
                freePoints = sector.getFreeFields(puzzle).stream().map(Vector2i::clone).collect(Collectors.toList());
                Collections.shuffle(freePoints);
            }
        }
        List<SectorData> s = sectors.stream().map(SectorData::new).filter(sd -> sd.freePoints.size() > 1).collect(Collectors.toList());
        int countAll = 0;
        for (SectorData sd : s) countAll += (sd.freePoints.size() / 2) * 2;
        double sumProgress = 0;
        double progressPerStep = 100.0 / countAll * 2.0;
        while (sumProgress < percent && s.size() > 0) {
            int randSector = rand.nextInt(s.size());
            SectorData sectorData = s.get(randSector);
            Vector2i p1 = sectorData.freePoints.get(0);
            Vector2i p2 = sectorData.freePoints.get(1);
            sectorData.freePoints.remove(p1);
            sectorData.freePoints.remove(p2);
            if (sectorData.freePoints.size() < 2) s.remove(sectorData);
            int color = sectorData.nextSunColor;
            suns.add(new Sun(p1, color));
            suns.add(new Sun(p2, color));
            if (!puzzle.reserveField(p1)) return;
            if (!puzzle.reserveField(p2)) return;
            sectorData.nextSunColor++;
            sumProgress += progressPerStep;
        }
    }

    protected static class Sun {
        Vector2i pos;
        int color;
        Sun(Vector2i p, int col) { pos = p; color = col; }
    }

    @Override
    public void draw(Canvas canvas, int pixelsPerPart, int marginTop, int marginLeft) {
        Paint paint = new Paint();
        paint.setStrokeWidth(pixelsPerPart);
        paint.setStrokeCap(Paint.Cap.ROUND);
        for (Sun sun : suns) {
            setColor(paint, sun.color);
            Vector2i center = new Vector2i(
                    (sun.pos.x * 3 + 3) * pixelsPerPart + marginLeft,
                    (sun.pos.y * 3 + 3) * pixelsPerPart + marginTop
            );
            canvas.drawArc(center.x - 10f, center.y - 10f, center.x + 10f, center.y + 10f, 0, 360, true, paint);
            drawSun(canvas, center, pixelsPerPart, paint);
        }
    }

    void setColor(Paint p, int color) {
        switch (color){
            case 0: p.setColor(Color.rgb(255, 140, 0)); break;
            case 1: p.setColor(Color.rgb(200, 0, 255)); break;
            case 2: p.setColor(Color.rgb(0, 255, 0)); break;
            case 3: p.setColor(Color.rgb(255, 255, 255)); break;
            default: p.setColor(Color.rgb(0, 0, 0));
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
