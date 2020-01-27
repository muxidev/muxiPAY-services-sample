package muxi.kotlinsample.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_choosetype.*
import muxi.kotlinsample.R
import muxi.kotlinsample.ui.ecommerce.EcommerceActivity
import muxi.kotlinsample.ui.present_card.MainActivity

class ChooseTypeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosetype)
        setSupportActionBar(my_toolbar)

        cv_item_payment_type_card.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))

        }

        tv_item_payment_type_no_card.setOnClickListener {
            startActivity(Intent(this, EcommerceActivity::class.java))

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

}