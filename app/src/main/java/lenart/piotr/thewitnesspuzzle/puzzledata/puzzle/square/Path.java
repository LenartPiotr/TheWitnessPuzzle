package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import java.util.ArrayList;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class Path {
   public Vector2i start;
   public List<Vector2i> steps;
   public double lastStepPercent;
   public boolean end;

   public Path() {
      start = new Vector2i();
      steps = new ArrayList<>();
      lastStepPercent = 1;
      end = false;
   }
   public Path(Vector2i start) {
      this.start = start;
      steps = new ArrayList<>();
      lastStepPercent = 1;
      end = false;
   }
   public Path(Vector2i start, List<Vector2i> steps) {
      this.start = start;
      this.steps = new ArrayList<>(steps);
      lastStepPercent = 1;
      end = false;
   }
   public Path(Path path) {
      start = path.start;
      steps = new ArrayList<>(path.steps);
      lastStepPercent = path.lastStepPercent;
      end = path.end;
   }
}
