package muxi.sample.ui.ecommerce

import android.os.Bundle
import muxi.sample.R
import muxi.sample.ui.BaseActivity

class EcommerceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecommerce)
        title = getString(R.string.ecommerce)

    }

}