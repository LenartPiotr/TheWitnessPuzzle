package lenart.piotr.thewitnesspuzzle.puzzledata.components;

import android.os.Parcel;

import androidx.annotation.NonNull;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.paths.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;

public interface IComponent {
    void writeToParcel(@NonNull Parcel parcel, int flags);
    boolean isMatching(IPuzzle puzzle, IPath path) throws WrongComponentException;
    void reset();
    void addRandomElement(IPuzzle puzzle, IPath path, int percent) throws WrongComponentException;
}
