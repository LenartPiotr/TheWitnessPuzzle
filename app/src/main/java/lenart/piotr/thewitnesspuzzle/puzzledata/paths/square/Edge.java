package lenart.piotr.thewitnesspuzzle.puzzledata.paths.square;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class Edge {
    public Vector2i v1;
    public Vector2i v2;

    public Edge() {
        v1 = new Vector2i();
        v2 = new Vector2i();
    }

    public Edge(Vector2i _v1, Vector2i _v2) {
        v1 = _v1;
        v2 = _v2;
    }

    public Edge(int x1, int y1, int x2, int y2) {
        v1 = new Vector2i(x1, y1);
        v2 = new Vector2i(x2, y2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (Objects.equals(v1, edge.v1) && Objects.equals(v2, edge.v2)) || (Objects.equals(v1, edge.v2) && Objects.equals(v2, edge.v1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2);
    }

    @NonNull
    @Override
    public String toString() {
        return "<" + v1 + "-" + v2 + ">";
    }

    public static List<Edge> generateAllEdges(int width, int height) {
        List<Edge> list = new ArrayList<>();

        // verticals
        for (int x = 0; x < width + 1; x++) {
            for (int y = 0; y < height; y++) {
                list.add(new Edge(x, y, x, y + 1));
            }
        }

        // horizontals
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height + 1; y++) {
                list.add(new Edge(x, y, x + 1, y));
            }
        }

        return list;
    }

    public static List<Edge> generateEdgesFromVertices(List<Vector2i> vertices) {
        List<Edge> list = new ArrayList<>();
        for (int i = 1; i < vertices.size(); i++) {
            list.add(new Edge(vertices.get(i - 1).clone(), vertices.get(i).clone()));
        }
        return list;
    }
}