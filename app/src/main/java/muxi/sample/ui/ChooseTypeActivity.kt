package muxi.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choosetype.*
import muxi.sample.R
import muxi.sample.ui.ecommerce.EcommerceActivity
import muxi.sample.ui.present_card.MainActivity

class ChooseTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosetype)
        setSupportActionBar(my_toolbar)

        tv_item_payment_type_card.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        tv_item_payment_type_no_card.setOnClickListener {
            startActivity(Intent(this, EcommerceActivity::class.java))
        }
    }
}