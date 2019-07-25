package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.MPSManager
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask

class CancelActivity : AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    val dialogHelper = DialogHelper.getInstance()

    val transactionHelper = TransactionHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel)

        dialogHelper.textColor = ContextCompat.getColor(this,R.color.color_base)

        title = getString(R.string.cancel_toolbar_title)

        dateLast.text = transactionHelper.dateLast
        typeLast.text = transactionHelper.typeLast
        amountLast.text = transactionHelper.amountLast

        btn_cancelLast.setOnClickListener {
            dialogHelper.showLoadingDialog(this, true)

            //TODO change to get from activity
            TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                "", MPSTransaction.TransactionMode.CREDIT,"","",1
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

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onCancelAnswer(mpsResult: MPSResult?) {
                super.onCancelAnswer(mpsResult)
                runOnUiThread {
                    dialogHelper.handleCancelAnswer(this@CancelActivity, mpsResult)
                }
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