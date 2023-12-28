package lenart.piotr.thewitnesspuzzle.puzzledata.generators.square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.IPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.square.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class DfsPathGenerator implements IPathGenerator {

    boolean[][] visited;
    int pWidth;
    int pHeight;
    Vector2i endPoint;

    List<Vector2i> path;

    @Override
    public IPath generate(IPuzzle ipuzzle) throws WrongComponentException {
        if (!(ipuzzle instanceof SquarePuzzle)) throw new WrongComponentException(this, SquarePuzzle.class, ipuzzle);
        SquarePuzzle puzzle = (SquarePuzzle) ipuzzle;
        pWidth = puzzle.getWidth() + 1;
        pHeight = puzzle.getHeight() + 1;
        visited = new boolean[pWidth][pHeight];

        Vector2i startPoint = new Vector2i(0, pHeight - 1);
        endPoint = new Vector2i(pWidth - 1, 0);
        path = new ArrayList<>();

        visited[startPoint.x][startPoint.y] = true;
        dfs(startPoint);
        path.add(startPoint);
        Collections.reverse(path);

        Path p = new Path();
        p.steps = path;
        p.start = path.get(0).clone();

        return p;
    }

    private boolean dfs(Vector2i position) {
        if (position.equals(endPoint)) return true;
        List<Vector2i> directions = new ArrayList<>();
        directions.add(new Vector2i(0, 1));
        directions.add(new Vector2i(0, -1));
        directions.add(new Vector2i(1, 0));
        directions.add(new Vector2i(-1, 0));
        Collections.shuffle(directions);
        for (int i = 0; i < 4; i++) {
            Vector2i dir = directions.get(i);
            Vector2i nextPos = position.add(dir);
            if (nextPos.x < 0 || nextPos.y < 0 || nextPos.x >= pWidth || nextPos.y >= pHeight) continue;
            if (visited[nextPos.x][nextPos.y]) continue;
            visited[nextPos.x][nextPos.y] = true;
            boolean result = dfs(nextPos);
            if (result) {
                path.add(nextPos);
                return true;
            } else {
                visited[nextPos.x][nextPos.y] = false;
            }
        }
        return false;
    }
}
