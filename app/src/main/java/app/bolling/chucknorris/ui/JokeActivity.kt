package app.bolling.chucknorris.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import app.bolling.chucknorris.R
import app.bolling.chucknorris.databinding.ActivityJokeBinding
import app.bolling.chucknorris.ui.fragment.favourite.FavouritesFragment
import app.bolling.chucknorris.ui.fragment.joke.JokeFragment

class JokeActivity : AppCompatActivity() {

    private var binding: ActivityJokeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_joke)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragMan = supportFragmentManager
            val fragTransaction = fragMan.beginTransaction()
            fragTransaction.replace(binding!!.frameContent.id, JokeFragment()).commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                //show back arrow
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_joke, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourites -> {
                val fragMan = supportFragmentManager
                val fragTransaction = fragMan.beginTransaction()
                fragTransaction.replace(binding!!.frameContent.id, FavouritesFragment()).addToBackStack(null).commit()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return false
        }
    }
}
