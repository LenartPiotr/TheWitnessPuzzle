package lenart.piotr.thewitnesspuzzle.puzzledata.components;

import android.os.Parcel;

import androidx.annotation.NonNull;

public interface IComponent {
    void writeToParcel(@NonNull Parcel parcel, int flags);
}
