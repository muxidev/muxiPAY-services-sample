package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.MPSManager
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.ui.BaseActivity
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask

class CancelActivity : BaseActivity() {

    private var mpsManager: MPSManager? = null

    val dialogHelper = DialogHelper.getInstance()

    private val transactionHelper = TransactionHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel)

        dialogHelper.textColor = ContextCompat.getColor(this,R.color.color_base)

        title = getString(R.string.cancel_toolbar_title)

        dateLast.text = transactionHelper.dateLast
        typeLast.text = transactionHelper.typeLast
        amountLast.text = transactionHelper.amountLast
        var transactionType: MPSTransaction.TransactionMode = MPSTransaction.TransactionMode.CREDIT
        when (transactionHelper.typeLast) {
            "DEBIT" -> transactionType = MPSTransaction.TransactionMode.DEBIT
            "VOUCHER" -> transactionType = MPSTransaction.TransactionMode.VOUCHER
        }

        btn_cancelLast.setOnClickListener {
            dialogHelper.showLoadingDialog(this, true)

            //TODO change to get from activity
            var cleanValue = transactionHelper.amountLast.replace("R$ ","")
            cleanValue =  cleanValue.replace(",","")
            Log.d("CancelActivity","Clean value: $cleanValue")
            TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                cleanValue, transactionType,"","",1
            )!!, Constants.TransactionState.cancel).execute()
        }
        btnOther.setOnClickListener {
            startActivity(Intent(this,CancelOtherActivity::class.java))

        }
    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null)
            mpsManager = MPSManager(this.applicationContext)

        mpsManager!!.bindService(applicationContext)

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onCancelAnswer(mpsResult: MPSResult?) {
                super.onCancelAnswer(mpsResult)
                runOnUiThread {
                    dialogHelper.handleCancelAnswer(this@CancelActivity, mpsResult)
                }
            }
        })
    }

}