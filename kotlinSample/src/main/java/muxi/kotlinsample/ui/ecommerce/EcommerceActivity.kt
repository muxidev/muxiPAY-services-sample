package muxi.kotlinsample.ui.ecommerce

import android.os.Bundle
import muxi.kotlinsample.R
import muxi.kotlinsample.ui.BaseActivity

class EcommerceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecommerce)
        title = getString(R.string.ecommerce)

    }

}