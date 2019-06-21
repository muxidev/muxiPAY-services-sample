package muxi.sample.ui.present_card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_receipt.*
import muxi.sample.Constants.RECEIPT_PARAM
import muxi.sample.R

class ReceiptActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        val receiptText: String = intent.getStringExtra(RECEIPT_PARAM)

        tv_receipt.text = receiptText
    }
}