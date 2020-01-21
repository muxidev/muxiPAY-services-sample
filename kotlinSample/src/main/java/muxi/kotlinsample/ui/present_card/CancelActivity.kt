package muxi.kotlinsample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.IMPSManager
import muxi.kotlinsample.Constants
import muxi.kotlinsample.R
import muxi.kotlinsample.TransactionHelper
import muxi.kotlinsample.ui.BaseActivity
import muxi.kotlinsample.ui.dialog.DialogHelper
import muxi.kotlinsample.ui.present_card.tasks.TransactionTask
import muxi.payservices.sdk.service.MPSManager

class CancelActivity : BaseActivity() {

    private var mpsManager: IMPSManager? = null

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
            TransactionTask(
                mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                    cleanValue, transactionType, "", "", 1
                )!!, Constants.TransactionState.cancel
            ).execute()
        }
        btnOther.setOnClickListener {
            startActivity(Intent(this, CancelOtherActivity::class.java))

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