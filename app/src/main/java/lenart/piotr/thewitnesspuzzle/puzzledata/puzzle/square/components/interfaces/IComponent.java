package lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.interfaces;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.util.List;

import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPath;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.utils.Sector;

public interface IComponent {
    void writeToParcel(@NonNull Parcel parcel, int flags);
    void readFromParcel(@NonNull Parcel in);
    boolean isMatching(SquarePuzzle puzzle, Path path, List<Sector> sectors);
    void reset();
    void addRandomElement(SquarePuzzle puzzle, Path path, List<Sector> sectors, int percent);
}
