package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_receipt.*
import muxi.sample.Constants.CLIENT_RECEIPT
import muxi.sample.Constants.ESTABLISHMENT_RECEIPT
import muxi.sample.R
import muxi.sample.ui.BaseActivity

class ReceiptActivity: BaseActivity() {


    private val TAG = ReceiptActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        val establishmentReceipt: String = intent.getStringExtra(ESTABLISHMENT_RECEIPT)
        val clientReceipt: String = intent.getStringExtra(CLIENT_RECEIPT)

        var receipt = establishmentReceipt
        title = getString(R.string.receipt_toolbar_title)

        tv_receipt.text = receipt

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG,"onTabReselected")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG,"onTabUnselected")

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG,"onTabSelected")
                receipt = if(tab!!.position == 0){
                    establishmentReceipt
                }else{
                    clientReceipt

                }
                tv_receipt.text = receipt

            }


        })


        btnShare.setOnClickListener {

            val i = Intent(Intent.ACTION_SEND)
            i.type = resources.getString(R.string.email_type)
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.client_receipt))
            i.putExtra(Intent.EXTRA_TEXT, clientReceipt)
            try {
                startActivity(Intent.createChooser(i, getResources().getString(R.string.send_email)))
            } catch (ex: android.content.ActivityNotFoundException) {
                Log.e(TAG, "There are no email clients installed.")
            }

        }
        btnDone.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))

        }
    }

}