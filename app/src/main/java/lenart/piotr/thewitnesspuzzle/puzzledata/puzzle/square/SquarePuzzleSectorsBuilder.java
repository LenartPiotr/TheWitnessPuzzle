package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SquarePuzzleSectorsBuilder {

    SquarePuzzle puzzle;
    Path path;
    List<SectorData> sectors;
    Random random;

    public SquarePuzzleSectorsBuilder(SquarePuzzle puzzle, Path path) {
        this.puzzle = puzzle;
        this.path = path;
        sectors = Sector.getSectors(puzzle.getWidth(), puzzle.getHeight(), path).stream().map(SectorData::new).collect(Collectors.toList());
        random = new Random();
    }

    public List<SectorData> getSectors() {
        return new ArrayList<>(sectors);
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public class SectorData {
        Sector sector;
        Map<Integer, Integer> colors;
        Set<Integer> lockColors;
        List<Vector2i> freePoints;
        int nextFreeColor;

        SectorData(Sector sector) {
            this.sector = sector;
            colors = new HashMap<>();
            lockColors = new HashSet<>();
            freePoints = sector.getFreeFields(puzzle);
            nextFreeColor = 0;
        }

        public int getFreeCount() { return freePoints.size(); }
        public int getFirstFreeColor() { return nextFreeColor; }
        public int getFirstAvailableColor() {
            int col = 0;
            while (lockColors.contains(col)) col++;
            return col;
        }
        public int countFieldsWithColor(int color) { return colors.getOrDefault(color, 0); }
        public int getColorContainsLessOrEqualsFieldsThan(int value) {
            if (value < 0) throw new RuntimeException("Count of fields cannot be negative value");
            int col = 0;
            while (lockColors.contains(col) || colors.getOrDefault(col, 0) > value) col++;
            return col;
        }
        public boolean isColorAvailable(int color) { return !lockColors.contains(color); }
        public void lockColor(int colorToLock) { lockColors.add(colorToLock); }
        public void putElement(Vector2i field, int color) {
            if (lockColors.contains(color)) throw new RuntimeException("Cannot add element with this color");
            freePoints.remove(field);
            colors.put(color, colors.getOrDefault(color, 0) + 1);
            if (!puzzle.reserveField(field, color)) throw new RuntimeException("Cannot add element");
            if (color >= nextFreeColor) nextFreeColor = color + 1;
        }
        public Vector2i getRandomFreeField() { return freePoints.get(random.nextInt(freePoints.size())); }
        public Vector2i getRandomFreeField(Vector2i except) {
            if (!freePoints.contains(except)) return getRandomFreeField();
            int exceptIndex = freePoints.indexOf(except);
            int randIndex = random.nextInt(freePoints.size() - 1);
            if (randIndex >= exceptIndex) randIndex++;
            return freePoints.get(randIndex);
        }
    }
}
