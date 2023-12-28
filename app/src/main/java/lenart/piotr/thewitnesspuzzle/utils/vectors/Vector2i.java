package lenart.piotr.thewitnesspuzzle.utils.vectors;

import java.util.Objects;

public class Vector2i {
   public int x;
   public int y;
   public Vector2i() { x = 0; y = 0; }
   public Vector2i(int _x, int _y) { x = _x; y = _y; }
   public Vector2i(Vector2i v) { x = v.x; y = v.y; }

   public Vector2i add(Vector2i v) { return new Vector2i(x + v.x, y + v.y); }
   public Vector2i sub(Vector2i v) { return new Vector2i(x - v.x, y - v.y); }
   public Vector2i multi(int v) { return new Vector2i(x * v, y * v); }
   public Vector2i multi(double v) { return new Vector2i((int)(x * v), (int)(y * v)); }
   public double dist(Vector2i v) { return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2)); }
   public int dist2(Vector2i v) { return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y); }
   public Vector2i clone() { return new Vector2i(x, y); }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Vector2i vector2i = (Vector2i) o;
      return x == vector2i.x && y == vector2i.y;
   }

   @Override
   public int hashCode() {
      return Objects.hash(x, y);
   }

   @Override
   public String toString() {
      return "(" + x + ", " + y + ')';
   }
}
