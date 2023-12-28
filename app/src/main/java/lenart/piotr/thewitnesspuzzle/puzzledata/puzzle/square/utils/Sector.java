package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.utils.tuples.Tuple;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class Sector {
    List<Vector2i> fields;

    public Sector() {
        fields = new ArrayList<>();
    }

    public List<Vector2i> getFields() { return fields; }
    public void addField(Vector2i v) {
        if (fields.contains(v)) return;
        fields.add(v);
    }
    public List<Vector2i> getFreeFields(SquarePuzzle puzzle) {
        return fields.stream().filter(puzzle::isFieldFree).collect(Collectors.toList());
    }
    public static List<Sector> getSectors(int width, int height, Path path) {
        List<Vector2i> directions = new ArrayList<>();
        directions.add(new Vector2i(0, 1));
        directions.add(new Vector2i(0, -1));
        directions.add(new Vector2i(1, 0));
        directions.add(new Vector2i(-1, 0));
        class Field{
            boolean visited = false;
            final Vector2i position;
            final List<Integer> connections;
            Field(Vector2i pos) {
                this.position = pos;
                connections = new ArrayList<>();
                for (Vector2i d : directions) {
                    Vector2i p = pos.add(d);
                    if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height) continue;
                    connections.add(p.y * width + p.x);
                }
            }
        }
        List<Field> fields = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fields.add(new Field(new Vector2i(x, y)));
            }
        }
        for (Edge e : Edge.generateEdgesFromVertices(path.steps)) {
            Tuple<Vector2i, Vector2i> separatedFields = e.getSeparatedFields();
            if (!separatedFields.v1.inRange(width, height) || !separatedFields.v2.inRange(width, height)) continue;
            int i1 = separatedFields.v1.y * height + separatedFields.v1.x;
            int i2 = separatedFields.v2.y * height + separatedFields.v2.x;
            fields.get(i1).connections.removeIf(index -> index == i2);
            fields.get(i2).connections.removeIf(index -> index == i1);
        }
        List<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).visited) continue;
            Sector sector = new Sector();
            Queue<Integer> Q = new ArrayDeque<>();
            Q.add(i);
            while (!Q.isEmpty()) {
                int index = Q.remove();
                Field f = fields.get(index);
                if (f.visited) continue;
                sector.fields.add(f.position);
                f.visited = true;
                Q.addAll(f.connections);
            }
            sectors.add(sector);
        }
        return sectors;
    }
}
