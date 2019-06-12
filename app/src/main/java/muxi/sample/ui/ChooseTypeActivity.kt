package muxi.sample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_choosetype.*
import muxi.sample.R

class ChooseTypeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosetype)

        val recyclerView = id_rv
        recyclerView.adapter = ItemAdapter(this)
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        Log.d("CHOSETYPEACTIVITY", "On ChooseTypeActivity ")
    }
}