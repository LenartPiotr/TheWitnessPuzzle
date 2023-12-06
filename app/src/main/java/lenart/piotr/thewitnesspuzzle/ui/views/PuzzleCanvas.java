package lenart.piotr.thewitnesspuzzle.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;

public class PuzzleCanvas extends View {

   IViewPuzzle viewPuzzle;

   public PuzzleCanvas(Context context) { super(context); }
   public PuzzleCanvas(Context context, AttributeSet attrs) { super(context, attrs); }

   public void setViewPuzzle(IViewPuzzle puzzle) {
      this.viewPuzzle = puzzle;
      invalidate();
   }

   @Override
   protected void onDraw(@NonNull Canvas canvas) {
      super.onDraw(canvas);
      if (viewPuzzle != null) viewPuzzle.draw(canvas);
   }
}
