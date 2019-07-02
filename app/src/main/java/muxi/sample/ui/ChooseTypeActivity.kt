package muxi.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_choosetype.*
import muxi.sample.Constants.ECOMMERCE_ID
import muxi.sample.Constants.PRESENT_CARD_ID
import muxi.sample.Constants.TYPES_SIZE
import muxi.sample.R
import muxi.sample.ui.ecommerce.EcommerceActivity
import muxi.sample.ui.present_card.ItemAdapter
import muxi.sample.ui.present_card.MainActivity
import org.jetbrains.anko.toast

class ChooseTypeActivity : AppCompatActivity(), ItemAdapter.ItemAdapterOnClickHandler {

    override fun onClick(type: Int) {
        when (type) {
            PRESENT_CARD_ID -> startActivity(Intent(this, MainActivity::class.java))
            ECOMMERCE_ID -> startActivity(Intent(this, EcommerceActivity::class.java))
            else -> toast(getString(R.string.something_wrong))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosetype)

        val recyclerView = id_rv
        recyclerView.adapter = ItemAdapter(this, this)
        val layoutManager = StaggeredGridLayoutManager(TYPES_SIZE,StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = layoutManager
    }
}