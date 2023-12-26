package lenart.piotr.thewitnesspuzzle.ui.fragments;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.Solution;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.square.BouncingPathGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.square.NaiveGenerator;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.IViewPuzzle;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.Path;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzleDisplay;
import lenart.piotr.thewitnesspuzzle.ui.views.PuzzleCanvas;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;

public class PuzzleInteractiveFragment extends Fragment {

    private IPuzzle puzzle;
    private PuzzleCanvas puzzleCanvas;
    private IViewPuzzle viewPuzzle;

    private SoundPool soundPool;
    private int soundGoodAnswer;
    private int soundBadAnswer;
    private int soundGiveUp;

    public PuzzleInteractiveFragment() { }

    public static PuzzleInteractiveFragment newInstance(IPuzzle puzzle) {
        PuzzleInteractiveFragment fragment = new PuzzleInteractiveFragment();
        Bundle args = new Bundle();
        args.putParcelable("puzzle", (Parcelable) puzzle);
        fragment.setArguments(args);
        return fragment;
    }

    private void prepareSongs() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        soundGoodAnswer = soundPool.load(getContext(), R.raw.good_answer, 1);
        soundBadAnswer = soundPool.load(getContext(), R.raw.bad_answer, 1);
        soundGiveUp = soundPool.load(getContext(), R.raw.give_up, 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            puzzle = getArguments().getParcelable("puzzle");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle_interactive, container, false);
        puzzleCanvas = view.findViewById(R.id.puzzleCanvas);
        viewPuzzle = puzzle.createViewPuzzle(getContext(), puzzleCanvas);

        prepareSongs();

        viewPuzzle.enableDrawing(path -> {
            if (path.end) {
                soundPool.play(soundGoodAnswer, 1, 1, 0, 0, 1);
            } else {
                soundPool.play(soundGiveUp, 1, 1, 0, 0, 1);
                viewPuzzle.clearPath();
            }
        });

        view.findViewById(R.id.btTest).setOnClickListener(v -> {
            NaiveGenerator generator = new NaiveGenerator.Builder().setSize(10, 10).build();
            Solution solution = null;
            try {
                solution = generator.generate();
            } catch (WrongComponentException e) {
                throw new RuntimeException(e);
            }
            puzzle = solution.getPuzzle();
            viewPuzzle = puzzle.createViewPuzzle(getContext(), puzzleCanvas);
            puzzleCanvas.setViewPuzzle(viewPuzzle);
            ((SquarePuzzleDisplay)viewPuzzle).setPath((Path) solution.getPath());
            puzzleCanvas.redraw();
        });

        puzzleCanvas.setViewPuzzle(viewPuzzle);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        if (viewPuzzle != null) viewPuzzle.destroy();
    }
}