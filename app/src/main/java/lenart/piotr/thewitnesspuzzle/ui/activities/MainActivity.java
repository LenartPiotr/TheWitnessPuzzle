package lenart.piotr.thewitnesspuzzle.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentContainerView;

import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.components.MissingEdgesComponent;
import lenart.piotr.thewitnesspuzzle.puzzledata.exceptions.WrongComponentException;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.Solution;
import lenart.piotr.thewitnesspuzzle.puzzledata.generators.square.NaiveGenerator;
import lenart.piotr.thewitnesspuzzle.ui.fragments.PuzzleInteractiveFragment;
import lenart.piotr.thewitnesspuzzle.utils.vectors.Vector2i;


public class MainActivity extends AppCompatActivity {

    FragmentContainerView fragmentContainer;
    boolean showMenu;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMenu = true;

        ImageView arrow = findViewById(R.id.ivArrow);
        LinearLayout topMenu = findViewById(R.id.llTopMenu);
        fragmentContainer = findViewById(R.id.fragmentsContainer);

        arrow.setOnClickListener(view -> {
            if (showMenu) {
                arrow.animate().rotation(0);
                topMenu.animate().translationY(-topMenu.getHeight());
            }
            else {
                arrow.animate().rotation(180);
                topMenu.animate().translationY(0);
            }
            showMenu = !showMenu;
        });

        nextPuzzle();
    }

    Vector2i getWindowMazeMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int maxV = Math.max(width, height);
        int minV = Math.min(width, height);

        int maxSize = 8;
        int minSize = maxSize * minV / maxV;

        int wSize = maxSize;
        int hSize = minSize;
        if (height > width) {
            wSize = minSize;
            hSize = maxSize;
        }
        hSize = Math.max(hSize - 1, 3);
        return new Vector2i(wSize, hSize);
    }

    public void nextPuzzle() {
        Vector2i windowMazeMetrics = getWindowMazeMetrics();
        NaiveGenerator generator = new NaiveGenerator.Builder()
                .setSize(4, 4)
                .addComponent(new MissingEdgesComponent())
                .build();
        Solution solution = null;
        try {
            solution = generator.generate();
        } catch (WrongComponentException e) {
            throw new RuntimeException(e);
        }
        changeFragment(PuzzleInteractiveFragment.newInstance(solution.getPuzzle()));
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.fragmentsContainer, fragment);
        tr.commit();
    }
}