package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Edge;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IExcludeEdgesComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;

public class MissingEdgesComponent implements IComponent, IExcludeEdgesComponent {

    List<Edge> edges = new ArrayList<>();

    public MissingEdgesComponent() { }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(edges.size());
        for (Edge e : edges) {
            parcel.writeInt(e.v1.x);
            parcel.writeInt(e.v1.y);
            parcel.writeInt(e.v2.x);
            parcel.writeInt(e.v2.y);
        }
    }

    @Override
    public void readFromParcel(@NonNull Parcel in) {
        int c = in.readInt();
        for (int i = 0; i < c; i++) {
            Edge e = new Edge();
            e.v1.x = in.readInt();
            e.v1.y = in.readInt();
            e.v2.x = in.readInt();
            e.v2.y = in.readInt();
        }
    }

    @Override
    public boolean isMatching(SquarePuzzle puzzle, Path path, List<Sector> sectors) {
        List<Edge> ePath = Edge.generateEdgesFromVertices(path.steps);
        for (Edge e : ePath) {
            if (edges.contains(e)) return false;
        }
        return true;
    }

    @Override
    public void reset() {
        edges.clear();
    }

    @Override
    public void addRandomElement(SquarePuzzle puzzle, Path path, int percent) {
        List<Edge> allEdges = Edge.generateAllEdges(puzzle.getWidth(), puzzle.getHeight());
        List<Edge> ePath = Edge.generateEdgesFromVertices(path.steps);
        allEdges.removeAll(ePath);
        Collections.shuffle(allEdges);

        int breakPoint = allEdges.size() * percent / 100;
        for (int i = 0; i < breakPoint; i++) {
            edges.add(allEdges.get(i));
        }
    }

    @Override
    public void setRandom(Random random) { }

    public boolean contains(int x1, int y1, int x2, int y2) {
        return edges.contains(new Edge(x1, y1, x2, y2));
    }

    @Override
    public List<Edge> getExcludedEdges() {
        return edges;
    }
}
