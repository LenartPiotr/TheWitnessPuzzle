package lenart.piotr.thewitnesspuzzle.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;

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
        puzzleCanvas.setViewPuzzle(puzzle.createViewPuzzle());
        return view;
    }
}