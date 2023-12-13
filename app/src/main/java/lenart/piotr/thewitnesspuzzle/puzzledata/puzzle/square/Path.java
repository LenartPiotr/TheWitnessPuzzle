package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class Path {
   public Vector2i start;
   public List<Vector2i> steps;
   public double lastStepPercent;
   public double startPercent;
   public boolean end;

   public Path() {
      start = new Vector2i();
      steps = new ArrayList<>();
      lastStepPercent = 1;
      startPercent = 1;
      end = false;
   }
   public Path(Vector2i start) {
      this.start = start;
      steps = new ArrayList<>();
      lastStepPercent = 1;
      startPercent = 1;
      end = false;
   }
   public Path(Vector2i start, List<Vector2i> steps) {
      this.start = start;
      this.steps = new ArrayList<>(steps);
      lastStepPercent = 1;
      startPercent = 1;
      end = false;
   }
   public Path(Path path) {
      start = path.start;
      steps = new ArrayList<>(path.steps);
      lastStepPercent = path.lastStepPercent;
      startPercent = path.startPercent;
      end = path.end;
   }

   @NonNull
   @Override
   public String toString() {
      return "Path{" +
              "steps=" + steps +
              ", lastStepPercent=" + lastStepPercent +
              '}';
   }
}
