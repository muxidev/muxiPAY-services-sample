package muxi.sample.ui.present_card

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel_other.*
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.data.MPSTransaction
import muxi.sample.service.MPSManager
import muxi.sample.ui.CallbackManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask

class CancelOtherActivity:AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    var transactionType: MPSTransaction.TransactionType = MPSTransaction.TransactionType.CREDIT

    val dialogHelper = DialogHelper.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_other)


        supportActionBar!!.title = getString(R.string.cancel_toolbar_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_creditOther!!.setOnClickListener{
            buttonEffect(btn_creditOther, MPSTransaction.TransactionType.CREDIT,btn_debitOther,btn_voucherOther)
        }

        btn_debitOther!!.setOnClickListener{
            buttonEffect(btn_debitOther, MPSTransaction.TransactionType.DEBIT,btn_creditOther,btn_voucherOther)
        }

        btn_voucherOther!!.setOnClickListener {
            buttonEffect(btn_voucherOther, MPSTransaction.TransactionType.VOUCHER,btn_creditOther,btn_debitOther)
        }

        btnCancelOther!!.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.VISIBLE)
            //TODO change to get from activity
            TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                "", transactionType,et_doc.text.toString(),
                et_autCode.text.toString(),0
            )!!, Constants.TransactionState.cancel).execute()
        }
    }


    //TODO trocar esta logica
    private fun buttonEffect(buttonPressed: Button, type: MPSTransaction.TransactionType, buttonUnpressed: Button, buttonUnpressedTwo: Button) {

        buttonPressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_base)
        buttonPressed.setTextColor(ContextCompat.getColor(this,R.color.color_text_pressed))

        buttonUnpressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressed.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        buttonUnpressedTwo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressedTwo.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        transactionType = type
    }


    override fun onStart() {
        super.onStart()
        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this.applicationContext)

        val callbackManager = CallbackManager.getInstance(this, dialogHelper)
        mpsManager!!.setMpsManagerCallback(callbackManager.mpsManagerCallback)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}