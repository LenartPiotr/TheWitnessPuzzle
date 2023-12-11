package lenart.piotr.thewitnesspuzzle.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleDisplay;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class PuzzleInteractiveFragment extends Fragment {

    private IPuzzle puzzle;
    private PuzzleCanvas puzzleCanvas;

    public PuzzleInteractiveFragment() { }

    public static PuzzleInteractiveFragment newInstance(IPuzzle puzzle) {
        PuzzleInteractiveFragment fragment = new PuzzleInteractiveFragment();
        Bundle args = new Bundle();
        args.putParcelable("puzzle", (Parcelable) puzzle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            puzzle = getArguments().getParcelable("puzzle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle_interactive, container, false);
        puzzleCanvas = view.findViewById(R.id.puzzleCanvas);
        IViewPuzzle viewPuzzle = puzzle.createViewPuzzle(puzzleCanvas);

        // TODO: TO REMOVE
        List<Vector2i> steps = new ArrayList<>();
        steps.add(new Vector2i(0, 5));
        steps.add(new Vector2i(1, 5));
        steps.add(new Vector2i(2, 5));
        steps.add(new Vector2i(2, 4));
        steps.add(new Vector2i(3, 4));
        steps.add(new Vector2i(3, 3));
        steps.add(new Vector2i(3, 2));
        steps.add(new Vector2i(4, 2));
        steps.add(new Vector2i(4, 1));
        steps.add(new Vector2i(4, 0));
        steps.add(new Vector2i(5, 0));
        ((SquarePuzzleDisplay)viewPuzzle).setPath(new Path(new Vector2i(0, 5), steps));

        puzzleCanvas.setViewPuzzle(viewPuzzle);
        return view;
    }
}