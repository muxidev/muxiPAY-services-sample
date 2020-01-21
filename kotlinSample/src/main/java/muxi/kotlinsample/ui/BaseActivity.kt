package muxi.kotlinsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import muxi.kotlinsample.R


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(this, ChooseTypeActivity::class.java))

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}