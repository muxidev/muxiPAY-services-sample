package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_receipt.*
import muxi.sample.Constants.RECEIPT_PARAM
import muxi.sample.R

class ReceiptActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        val receiptText: String = intent.getStringExtra(RECEIPT_PARAM)


        supportActionBar!!.title = getString(R.string.receipt_toolbar_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tv_receipt.text = receiptText

        btnDone.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}