package lenart.piotr.thewitnesspuzzle.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentContainerView;
import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.puzzle.square.SquarePuzzle;
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

        SquarePuzzle puzzle = SquarePuzzle.createEmpty(5, 5);
        puzzle.getStartPoints().add(new Vector2i(0, 0));
        puzzle.getEndPoints().add(new Vector2i(2, 0));
        puzzle.getEndPoints().add(new Vector2i(2, 5));
        puzzle.getEndPoints().add(new Vector2i(0, 2));
        puzzle.getEndPoints().add(new Vector2i(5, 2));
        puzzle.getEndPoints().add(new Vector2i(0, 0));
        puzzle.getEndPoints().add(new Vector2i(5, 0));
        puzzle.getEndPoints().add(new Vector2i(0, 5));
        puzzle.getEndPoints().add(new Vector2i(5, 5));
        puzzle.getEndPoints().add(new Vector2i(2, 2));
        changeFragment(PuzzleInteractiveFragment.newInstance(puzzle));
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.fragmentsContainer, fragment);
        tr.commit();
    }
}