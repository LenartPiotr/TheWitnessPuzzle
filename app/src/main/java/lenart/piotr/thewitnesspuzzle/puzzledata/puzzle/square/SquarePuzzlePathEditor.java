package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;
import lenart.piotr.thewitnesspuzzle.utils.callbacks.ICallback2;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

class SquarePuzzlePathEditor implements View.OnTouchListener{

   private final SquarePuzzle puzzle;
   private final SquarePuzzleDisplay display;
   private final ICallback2<Path, Boolean> onPathUpdate;
   private final Path path;
   private final Context context;

   private SoundPool soundPool;
   private int soundStart;

   private final double ENDING_TOLERATION = 0.7;

   @SuppressLint("ClickableViewAccessibility")
   protected SquarePuzzlePathEditor(Context context, SquarePuzzle puzzle, PuzzleCanvas canvas, SquarePuzzleDisplay display, ICallback2<Path, Boolean> onPathUpdate) {
      this.display = display;
      this.onPathUpdate = onPathUpdate;
      this.puzzle = puzzle;
      this.context = context;

      canvas.setOnTouchListener(this);
      path = new Path();

      prepareSounds();
   }

   @SuppressLint("ClickableViewAccessibility")
   @Override
   public boolean onTouch(View view, MotionEvent e) {
      switch (e.getAction()) {
         case MotionEvent.ACTION_DOWN:
            clickDown((int)e.getX(), (int)e.getY());
            return true;
         case MotionEvent.ACTION_MOVE:
            move((int)e.getX(), (int)e.getY());
            checkEnding();
            onPathUpdate.run(path, false);
            return true;
         case MotionEvent.ACTION_UP:
            up();
            return true;
      }
      return false;
   }

   protected void destroy() {
      soundPool.release();
   }

   private void prepareSounds() {
      AudioAttributes audioAttributes = new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build();
      soundPool = new SoundPool.Builder()
              .setMaxStreams(3)
              .setAudioAttributes(audioAttributes)
              .build();
      soundStart = soundPool.load(context, R.raw.begin, 1);
   }

   private Vector2i getPixelsPoint(Vector2i point) {
      int pixelsPerPart = display.getPixelsPerPart();
      Vector2i margin = display.getMargin();
      return new Vector2i(
              (point.x * 3 + 1) * pixelsPerPart + margin.x + pixelsPerPart / 2,
              (point.y * 3 + 1) * pixelsPerPart + margin.y + pixelsPerPart / 2
      );
   }

   private Vector2i getClosestPoint(Vector2i pixels) {
      int pixelsPerPart = display.getPixelsPerPart();
      Vector2i margin = display.getMargin();
      return new Vector2i(
              (int)Math.round(((pixels.x - margin.x - pixelsPerPart / 2.0) / pixelsPerPart - 1) / 3.0),
              (int)Math.round(((pixels.y - margin.y - pixelsPerPart / 2.0) / pixelsPerPart - 1) / 3.0)
      );
   }

   private void clickDown(int x, int y) {
      path.steps.clear();
      path.end = false;
      path.startPercent = 1;
      int pixelsPerPart = display.getPixelsPerPart();
      Vector2i point = new Vector2i(x, y);
      for (Vector2i start : puzzle.startPoints) {
         if (getPixelsPoint(start).dist(point) < pixelsPerPart * 1.5) {
            path.start = start;
            path.steps.add(start);
            onPathUpdate.run(path, false);
            soundPool.play(soundStart, 1, 1, 0, 0, 1);
            return;
         }
      }
      onPathUpdate.run(path, false);
   }

   private void updatePercent(Vector2i p1, Vector2i p2, Vector2i pix1, Vector2i pix2, int x, int y) {
      if (p1.x != p2.x) {
         path.lastStepPercent = (x - pix1.x) / (double)(pix2.x - pix1.x);
      } else {
         path.lastStepPercent = (y - pix1.y) / (double)(pix2.y - pix1.y);
      }
      path.lastStepPercent = Math.min(1, Math.max(0, path.lastStepPercent));
   }

   private boolean canAddNewPoint(Vector2i point) {
      if (point.x < 0 || point.y < 0 || point.x > puzzle.width || point.y > puzzle.height) return false;
      if (path.steps.contains(point)) return false;
      return true;
   }

   private void checkEnding() {
      path.end = false;
      if (path.steps.size() == 0) return;
      Vector2i last = path.steps.get(path.steps.size() - 1);
      if (!puzzle.endPoints.contains(last)) return;
      if (path.lastStepPercent < ENDING_TOLERATION) return;
      path.lastStepPercent = 1;
      path.end = true;
   }

   private void move(int x, int y) {
      if (this.path.steps.size() == 0) return;
      Vector2i closestPoint = getClosestPoint(new Vector2i(x, y));
      if (closestPoint.x < 0 || closestPoint.y < 0 || closestPoint.x > puzzle.width || closestPoint.y > puzzle.height) return;
      Vector2i last = path.steps.get(path.steps.size() - 1);
      Vector2i prev = path.steps.size() >= 2 ? path.steps.get(path.steps.size() - 2) : new Vector2i(-100, -100);
      if (closestPoint.equals(last)) {
         Vector2i lastPix = getPixelsPoint(last);
         Vector2i dif = new Vector2i(x, y).sub(lastPix);
         Vector2i nearest;
         if (Math.abs(dif.x) > Math.abs(dif.y)) {
            nearest = last.add(new Vector2i(dif.x < 0 ? -1 : 1, 0));
         } else {
            nearest = last.add(new Vector2i(0, dif.y < 0 ? -1 : 1));
         }
         if (nearest.equals(prev)) {
            updatePercent(prev, last, getPixelsPoint(prev), lastPix, x, y);
            return;
         }
         if (canAddNewPoint(nearest)) {
            path.steps.add(nearest);
            updatePercent(last, nearest, lastPix, getPixelsPoint(nearest), x, y);
         } else {
            if (path.steps.size() > 1)
               updatePercent(prev, last, getPixelsPoint(prev), lastPix, x, y);
            else path.lastStepPercent = 1;
         }
         return;
      }
      int dist = closestPoint.dist2(last);
      if (dist > 1) return;
      if (closestPoint.equals(prev)) {
         Vector2i prevPix = getPixelsPoint(prev);
         Vector2i dif = new Vector2i(x, y).sub(prevPix);
         Vector2i nearest;
         if (Math.abs(dif.x) > Math.abs(dif.y)) {
            nearest = prev.add(new Vector2i(dif.x < 0 ? -1 : 1, 0));
         } else {
            nearest = prev.add(new Vector2i(0, dif.y < 0 ? -1 : 1));
         }
         if (nearest.equals(last)) {
            updatePercent(prev, last, prevPix, getPixelsPoint(last), x, y);
            return;
         }
         path.steps.remove(path.steps.size() - 1);
         if (canAddNewPoint(nearest)) {
            path.steps.add(nearest);
            return;
         }
         if (path.steps.size() < 3) return;
         Vector2i prevPrev = path.steps.get(path.steps.size() - 2);
         if (!prevPrev.equals(nearest)) return;
         updatePercent(prevPrev, prev, getPixelsPoint(prevPrev), prevPix, x, y);
         return;
      }
      if (path.steps.contains(closestPoint)) return;
      path.steps.add(closestPoint);
      updatePercent(last, closestPoint, getPixelsPoint(last), getPixelsPoint(closestPoint), x, y);
   }

   private void up() {
      onPathUpdate.run(path, true);
   }
}
