package lenart.piotr.thewitnesspuzzle.puzzledata;

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

public class SquarePuzzle implements IPuzzle, IViewPuzzle, Parcelable {

    private int width, height;
    private List<IComponent> components;

    private SquarePuzzle(int width, int height) {
        this.width = width;
        this.height = height;
        this.components = new ArrayList<>();
    }

    public static SquarePuzzle createEmpty(int width, int height) {
        SquarePuzzle empty = new SquarePuzzle(width, height);
        return empty;
    }

    // IPuzzle implementation

    // IViewPuzzle implementation

    @Override
    public void draw(Canvas canvas) {
        // lines background #3D2DC7 61/45/199
        // lines front #B6DAF7 182/218/247
        // background #5653FF 86/83/255

        Paint paintLinesBg = new Paint();
        paintLinesBg.setColor(Color.rgb(61, 45, 199));

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int requiredPartsHorizontal = width * 3 + 3;
        int requiredPartsVertical = height * 3 + 3;

        int pixelsPerPartHorizontal = canvasWidth / requiredPartsHorizontal;
        int pixelsPerPartVertical = canvasHeight / requiredPartsVertical;
        int pixelsPerPart = Math.min(pixelsPerPartVertical, pixelsPerPartHorizontal);

        int marginTop = 0;
        int marginLeft = 0;
        if (pixelsPerPartVertical < pixelsPerPartHorizontal) {
            marginLeft = (canvasWidth - (pixelsPerPart * requiredPartsHorizontal)) / 2;
        } else {
            marginTop = (canvasHeight - (pixelsPerPart * requiredPartsVertical)) / 2;
        }

        for (int x = 0; x <= width; x++) {
            canvas.drawRoundRect(
                    new RectF(
                            (x * 3 + 1) * pixelsPerPart + marginLeft,
                            pixelsPerPart + marginTop,
                            (x * 3 + 2) * pixelsPerPart + marginLeft,
                            pixelsPerPart * (height * 3 + 2) + marginTop),
                    pixelsPerPart / 2, pixelsPerPart / 2, paintLinesBg);
        }
        for (int y = 0; y <= height; y++) {
            canvas.drawRoundRect(
                    new RectF(
                            pixelsPerPart + marginLeft,
                            (y * 3 + 1) * pixelsPerPart + marginTop,
                            pixelsPerPart * (width * 3 + 2) + marginLeft,
                            (y * 3 + 2) * pixelsPerPart + marginTop),
                    pixelsPerPart / 2, pixelsPerPart / 2, paintLinesBg);
        }
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
