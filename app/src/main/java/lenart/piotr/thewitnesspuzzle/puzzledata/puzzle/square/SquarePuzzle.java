package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lenart.piotr.thewitnesspuzzle.puzzledata.components.ComponentsIdManager;
import lenart.piotr.thewitnesspuzzle.puzzledata.components.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SquarePuzzle implements IPuzzle, Parcelable {

    protected int width, height;
    protected List<IComponent> components;

    protected List<Vector2i> startPoints;
    protected List<Vector2i> endPoints;

    private SquarePuzzle(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
        this.startPoints = new ArrayList<>();
        this.endPoints = new ArrayList<>();
    }

    public static SquarePuzzle createEmpty(int width, int height) {
        SquarePuzzle empty = new SquarePuzzle(width, height);
        return empty;
    }

    public List<Vector2i> getStartPoints() { return startPoints; }
    public List<Vector2i> getEndPoints() { return endPoints; }

    // IPuzzle implementation

    @Override
    public IViewPuzzle createViewPuzzle() {
        return new SquarePuzzleDisplay(this);
    }

    // Parcelable

    protected SquarePuzzle(Parcel in) {
        width = in.readInt();
        height = in.readInt();
        int componentsCount = in.readInt();
        components = new ArrayList<>();
        for (int i = 0; i < componentsCount; i++) {
            components.add(ComponentsIdManager.createComponent(in.readInt(), in));
        }
    }

    public static final Creator<SquarePuzzle> CREATOR = new Creator<SquarePuzzle>() {
        @Override
        public SquarePuzzle createFromParcel(Parcel in) {
            return new SquarePuzzle(in);
        }

        @Override
        public SquarePuzzle[] newArray(int size) {
            return new SquarePuzzle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeInt(components.size());
        for (IComponent component : components) {
            parcel.writeInt(ComponentsIdManager.getComponentId(component));
            component.writeToParcel(parcel, flags);
        }
    }
}
