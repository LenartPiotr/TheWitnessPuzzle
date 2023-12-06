package lenart.piotr.thewitnesspuzzle.utils.vectors;

public class Vector2f {
   public float x;
   public float y;
   public Vector2f() { x = 0; y = 0; }
   public Vector2f(float _x, float _y) { x = _x; y = _y; }
   public Vector2f(Vector2f v) { x = v.x; y = v.y; }

   public Vector2f add(Vector2f v) { return new Vector2f(x + v.x, y + v.y); }
   public Vector2f sub(Vector2f v) { return new Vector2f(x - v.x, y - v.y); }
   public Vector2f multi(int v) { return new Vector2f(x * v, y * v); }
   public double dist(Vector2f v) { return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2)); }
   public float dist2(Vector2f v) { return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y); }
}
