package lenart.piotr.thewitnesspuzzle.puzzledata.components;

import android.os.Parcel;

import lenart.piotr.thewitnesspuzzle.puzzledata.components.square.MissingEdgesComponent;

public class ComponentsIdManager {
   public static int getComponentId(IComponent component) {
      if (component instanceof MissingEdgesComponent) return 1;
      return -1;
   }
   public static IComponent createComponent(int id, Parcel in) {
      if (id == 1) return new MissingEdgesComponent(in);
      return null;
   }
}
