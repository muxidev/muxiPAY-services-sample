package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cancel.*
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.data.MPSTransaction
import muxi.sample.service.MPSManager
import muxi.sample.ui.CallbackManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask

class CancelActivity : AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    val dialogHelper = DialogHelper.newInstance()

    val transactionHelper = TransactionHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel)


        supportActionBar!!.title = getString(R.string.cancel_toolbar_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dateLast.text = transactionHelper.dateLast
        typeLast.text = transactionHelper.typeLast
        amountLast.text = transactionHelper.amountLast

        btn_cancelLast.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.VISIBLE)

            //TODO change to get from activity
            TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                "", MPSTransaction.TransactionType.CREDIT,"","",1
            )!!, Constants.TransactionState.cancel).execute()
        }
        btnOther.setOnClickListener {
            startActivity(Intent(this,CancelOtherActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this.applicationContext)

        val callbackManager = CallbackManager.newInstance(this, dialogHelper)
        mpsManager!!.setMpsManagerCallback(callbackManager.mpsManagerCallback)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}