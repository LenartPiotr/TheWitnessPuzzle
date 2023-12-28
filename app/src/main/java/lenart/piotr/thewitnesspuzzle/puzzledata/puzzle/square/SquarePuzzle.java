package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces.IExcludeEdgesComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Edge;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class SquarePuzzle implements IPuzzle, Parcelable {

    int width, height;
    List<IComponent> components;

    List<Vector2i> startPoints;
    List<Vector2i> endPoints;

    List<Edge> excluded;
    boolean[][] reservedFields;

    private SquarePuzzle(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
        this.startPoints = new ArrayList<>();
        this.endPoints = new ArrayList<>();
        excluded = new ArrayList<>();
        reservedFields = new boolean[width][height];
    }

    public static SquarePuzzle createEmpty(int width, int height) {
        return new SquarePuzzle(width, height);
    }

    public List<Vector2i> getStartPoints() { return startPoints; }
    public List<Vector2i> getEndPoints() { return endPoints; }

    public int getHeight() { return height; }
    public int getWidth() { return width; }

    public List<IComponent> getComponents() { return components; }
    public void registerComponents() {
        excluded.clear();
        for (IComponent c : components) {
            if (c instanceof IExcludeEdgesComponent) {
                excluded.addAll(((IExcludeEdgesComponent) c).getExcludedEdges());
            }
        }
    }

    public boolean isEdgeExcluded(Edge e) { return excluded.contains(e); }

    public boolean reserveField(Vector2i v) {
        if (reservedFields[v.x][v.y]) return false;
        reservedFields[v.x][v.y] = true;
        return true;
    }
    public void freeField(Vector2i v) { reservedFields[v.x][v.y] = false; }
    public boolean isFieldFree(Vector2i v) { return !reservedFields[v.x][v.y]; }

    // IPuzzle implementation

    @Override
    public IViewPuzzle createViewPuzzle(Context context, PuzzleCanvas canvas) {
        return new SquarePuzzleDisplay(context, this, canvas);
    }

    @Override
    public boolean isMatching(IPath iPath) throws WrongComponentException {
        if (!(iPath instanceof Path)) throw new WrongComponentException(this, Path.class, iPath);
        Path path = (Path) iPath;
        List<Sector> sectors = Sector.getSectors(width, height, path);
        for (IComponent component : components) {
            if (!component.isMatching(this, path, sectors)) return false;
        }
        return true;
    }

    // Parcelable

    protected SquarePuzzle(Parcel in) {
        width = in.readInt();
        height = in.readInt();
        int componentsCount = in.readInt();
        components = new ArrayList<>();
        for (int i = 0; i < componentsCount; i++) {
            String className = Objects.requireNonNull(in.readString());
            try {
                Class<?> anyClass = Class.forName(className);
                if (IComponent.class.isAssignableFrom(anyClass)) {
                    Class<? extends IComponent> componentClass = anyClass.asSubclass(IComponent.class);
                    Constructor<? extends IComponent> componentConstructor = componentClass.getConstructor();
                    IComponent component = componentConstructor.newInstance();
                    component.readFromParcel(in);
                    components.add(component);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Class " + className + " should contains empty constructor.", e);
            }
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
            parcel.writeString(component.getClass().getName());
            component.writeToParcel(parcel, flags);
        }
    }
}
