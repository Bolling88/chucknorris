package app.bolling.chucknorris.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import app.bolling.chucknorris.R;
import app.bolling.chucknorris.databinding.ActivityJokeBinding;

public class JokeActivity extends AppCompatActivity {

    private ActivityJokeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_joke);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragMan.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putInt(JokeFragment.KEY_JOKE_ID, 11);
            Fragment myFrag = new JokeFragment();
            myFrag.setArguments(bundle);
            fragTransaction.replace(binding.frameContent.getId(), myFrag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.joke, menu);
        return true;
    }

}
