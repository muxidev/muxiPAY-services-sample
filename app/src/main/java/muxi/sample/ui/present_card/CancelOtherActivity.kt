package muxi.sample.ui.present_card

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel_other.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.MPSManager
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask

class CancelOtherActivity:AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    var transactionMode: MPSTransaction.TransactionMode = MPSTransaction.TransactionMode.CREDIT

    val dialogHelper = DialogHelper.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_other)

        title = getString(R.string.cancel_toolbar_title)

        btn_creditOther!!.setOnClickListener{
            buttonEffect(btn_creditOther, MPSTransaction.TransactionMode.CREDIT,btn_debitOther,btn_voucherOther)
        }

        btn_debitOther!!.setOnClickListener{
            buttonEffect(btn_debitOther, MPSTransaction.TransactionMode.DEBIT,btn_creditOther,btn_voucherOther)
        }

        btn_voucherOther!!.setOnClickListener {
            buttonEffect(btn_voucherOther, MPSTransaction.TransactionMode.VOUCHER,btn_creditOther,btn_debitOther)
        }

        btnCancelOther!!.setOnClickListener {
            dialogHelper.showLoadingDialog(this, true)
            //TODO change to get from activity
            TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                "", transactionMode,et_doc.text.toString(),
                et_autCode.text.toString(),0
            )!!, Constants.TransactionState.cancel).execute()
        }
    }


    //TODO trocar esta logica
    private fun buttonEffect(buttonPressed: Button, mode: MPSTransaction.TransactionMode, buttonUnpressed: Button, buttonUnpressedTwo: Button) {

        buttonPressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_base)
        buttonPressed.setTextColor(ContextCompat.getColor(this,R.color.color_text_pressed))

        buttonUnpressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressed.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        buttonUnpressedTwo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressedTwo.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        transactionMode = mode
    }


    override fun onStart() {
        super.onStart()
        if(mpsManager == null){
            mpsManager = MPSManager(this.applicationContext)
        }

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onCancelAnswer(mpsResult: MPSResult?) {
                super.onCancelAnswer(mpsResult)
                dialogHelper.handleCancelAnswer(this@CancelOtherActivity,mpsResult)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}


/*
GIN000100
PAX
D150

1.06               @
1.08
001.16 180619
7D334404
,D
 */