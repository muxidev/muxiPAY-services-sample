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


        title = getString(R.string.receipt_toolbar_title)

        tv_receipt.text = receiptText

        btnShare.setOnClickListener {

            val i = Intent(Intent.ACTION_SEND)
            i.type = getResources().getString(R.string.email_type)
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.client_receipt))
            i.putExtra(Intent.EXTRA_TEXT, receiptText)
        }
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