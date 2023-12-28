package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Random;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.square.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class BouncingPathGenerator implements IPathGenerator {

    SquarePuzzle puzzle;
    int width;
    int height;
    int innerWidth;
    int innerHeight;
    boolean[][] inner;
    Random rand;

    @Override
    public IPath generate(IPuzzle ipuzzle) throws WrongComponentException {
        if (!(ipuzzle instanceof SquarePuzzle)) throw new WrongComponentException(this, SquarePuzzle.class, ipuzzle);
        this.puzzle = (SquarePuzzle) ipuzzle;
        width = puzzle.getWidth();
        height = puzzle.getHeight();
        innerWidth = width - 1;
        innerHeight = height - 1;
        rand = new Random();

        inner = new boolean[innerWidth][innerHeight];

        Path path = new Path();

        Boolean goFirstRight = rand.nextBoolean();
        Boolean fromRight = goFirstRight;
        Log.d("XXX", "first: " + fromRight);

        Range range = new Range(0, rangeLength() - 1);
        while (range.length() > 3) {
            Log.d("XXX", range.toString());
            int p1 = range.randomPoint();
            int p2 = range.randomPointNo(p1);
            if (!range.arePointsSorted(p1, p2)) {
                int temp = p1;
                p1 = p2;
                p2 = temp;
            }
            Log.d("XXX", "rand: " + p1 + ", " + p2);
            Vector2i v1 = mapIndexToPoint(p1);
            Vector2i v2 = mapIndexToPoint(p2);
            // Make path
            path.steps.add(new Vector2i(v1.x + 1, v1.y + 1));
            path.steps.add(new Vector2i(v2.x + 1, v2.y + 1));
            //
            inner[v1.x][v1.y] = true;
            inner[v2.x][v2.y] = true;
            Range r1 = new Range(range.start, p1).searchMaxRange();
            Range r2 = new Range(p1, p2).searchMaxRange();
            Range r3 = new Range(p2, range.end).searchMaxRange();
            range = r2;
            if (fromRight && r3.length() > r2.length()) { range = r3; fromRight = true; }
            else if (!fromRight && r1.length() > r2.length()) { range = r1; fromRight = false; }
            else { fromRight = !fromRight; }
        }
        path.start = path.steps.get(0);

        return path;
    }

    Vector2i mapIndexToPoint(int index) {
        if (index <= innerWidth - 1) return new Vector2i(index, innerHeight - 1);
        if (index <= innerWidth + innerHeight - 2) return new Vector2i(innerWidth - 1, innerHeight - index + innerWidth - 2);
        if (index <= 2 * innerWidth + innerHeight - 3) return new Vector2i(2 * innerWidth + innerHeight - 3 - index, 0);
        return new Vector2i(0, index - 2 * innerWidth - innerHeight + 3);
    }

    int rangeLength() {
        return 2 * innerWidth + 2 * innerHeight - 4;
    }

    class Range {
        int start;
        int end;
        Range(int s, int e) {
            start = s;
            end = e;
        }

        int length() {
            if (end >= start) return end - start + 1;
            return rangeLength() - start + end + 1;
        }
        int randomPoint() {
            int p = rand.nextInt(length());
            if (end >= start) return p + start;
            if (p <= end) return p;
            return start - end - 1 + p;
        }
        int randomPointNo(int excludedPoint) {
            int p = rand.nextInt(length() - 1);
            int mapped;
            if (end >= start) {
                mapped = p + start;
                if (mapped < excludedPoint) return mapped;
                return mapped + 1;
            }
            if (p <= end) {
                if (p < excludedPoint) return p;
                return p + 1;
            }
            mapped = start - end - 1 + p;
            if (mapped < excludedPoint) return mapped;
            return mapped + 1;
        }
        boolean arePointsSorted(int p1, int p2) {
            if (start < end) return p1 < p2;
            int vp1 = p1;
            int vp2 = p2;
            if (vp1 >= start) vp1 -= rangeLength();
            if (vp2 >= start) vp2 -= rangeLength();
            return vp1 < vp2;
        }

        Range searchMaxRange() {
            // Log.d("XXX", "Searching: " + this);
            int max_st = 0;
            int max_en = 0;
            int max_len = 0;
            int st = start;
            int it = start;
            int len = 0;
            int last = rangeLength() - 1;
            boolean loop = true;
            boolean begin = false;
            Vector2i v = mapIndexToPoint(it);
            while (inner[v.x][v.y] && it != end) {
                it++;
                if (it > last) it = 0;
                v = mapIndexToPoint(it);
            }
            len = 1;
            st = it;
            if (it == end) return new Range(end, end);
            while (loop) {
                it++;
                if (it > last) it = 0;
                if (it == end) loop = false;
                v = mapIndexToPoint(it);
                if (inner[v.x][v.y]) {
                    if (begin) continue;
                    if (len > max_len) {
                        max_len = len;
                        max_st = st;
                        max_en = it - 1;
                        if (max_en < 0) max_en = last;
                    }
                    begin = true;
                    continue;
                }
                if (begin) {
                    len = 0;
                    begin = false;
                    st = start;
                }
                len++;
            }
            if (!begin && len > max_len) {
                max_st = st;
                max_en = it;
            }
            // Log.d("XXX", "Found max len = " + max_len);
            return new Range(max_st, max_en);
        }

        @NonNull
        @Override
        public String toString() {
            return "(" + start + " -> " + end + ')';
        }
    }
}
