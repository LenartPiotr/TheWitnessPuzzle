package lenart.piotr.thewitnesspuzzle.puzzledata.components.square;

import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lenart.piotr.thewitnesspuzzle.puzzledata.components.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.square.Edge;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.square.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class MissingEdgesComponent implements IComponent {

    List<Edge> edges = new ArrayList<>();

    public MissingEdgesComponent() { }
    public MissingEdgesComponent(Parcel parcel) {
        int c = parcel.readInt();
        for (int i = 0; i < c; i++) {
            Edge e = new Edge();
            e.v1.x = parcel.readInt();
            e.v1.y = parcel.readInt();
            e.v2.x = parcel.readInt();
            e.v2.y = parcel.readInt();
        }
    }

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
    public boolean isMatching(IPuzzle ipuzzle, IPath ipath) throws WrongComponentException {
        // if (!(ipuzzle instanceof SquarePuzzle)) throw new WrongComponentException(this, SquarePuzzle.class, ipuzzle);
        if (!(ipath instanceof Path)) throw new WrongComponentException(this, Path.class, ipath);
        // SquarePuzzle puzzle = (SquarePuzzle) ipuzzle;
        Path path = (Path) ipath;

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
    public void addRandomElement(IPuzzle ipuzzle, IPath ipath, int percent) throws WrongComponentException {
        if (!(ipuzzle instanceof SquarePuzzle)) throw new WrongComponentException(this, SquarePuzzle.class, ipuzzle);
        if (!(ipath instanceof Path)) throw new WrongComponentException(this, Path.class, ipath);
        SquarePuzzle puzzle = (SquarePuzzle) ipuzzle;
        Path path = (Path) ipath;

        List<Edge> allEdges = Edge.generateAllEdges(puzzle.getWidth(), puzzle.getHeight());
        List<Edge> ePath = Edge.generateEdgesFromVertices(path.steps);
        allEdges.removeAll(ePath);
        Collections.shuffle(allEdges);

        int breakPoint = allEdges.size() * percent / 100;
        for (int i = 0; i < breakPoint; i++) {
            edges.add(allEdges.get(i));
        }
    }

    public boolean contains(int x1, int y1, int x2, int y2) {
        return edges.contains(new Edge(x1, y1, x2, y2));
    }

    public List<Edge> getEdges() { return edges; }
}
