package lenart.piotr.thewitnesspuzzle.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentContainerView;
import lenart.piotr.thewitnesspuzzle.R;
import lenart.piotr.thewitnesspuzzle.puzzledata.SquarePuzzle;
import lenart.piotr.thewitnesspuzzle.ui.fragments.PuzzleInteractiveFragment;


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

        changeFragment(PuzzleInteractiveFragment.newInstance(SquarePuzzle.createEmpty(5, 5)));
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.fragmentsContainer, fragment);
        tr.commit();
    }
}