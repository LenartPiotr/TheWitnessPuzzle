package lenart.piotr.thewitnesspuzzle.utils.vectors;

public class Vector2i {
   public int x;
   public int y;
   public Vector2i() { x = 0; y = 0; }
   public Vector2i(int _x, int _y) { x = _x; y = _y; }
   public Vector2i(Vector2i v) { x = v.x; y = v.y; }

   public Vector2i add(Vector2i v) { return new Vector2i(x + v.x, y + v.y); }
   public Vector2i sub(Vector2i v) { return new Vector2i(x - v.x, y - v.y); }
   public Vector2i multi(int v) { return new Vector2i(x * v, y * v); }
   public double dist(Vector2i v) { return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2)); }
   public int dist2(Vector2i v) { return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y); }
}
