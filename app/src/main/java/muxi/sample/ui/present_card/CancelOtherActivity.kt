package muxi.sample.ui.present_card

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_cancel_other.*
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

class CancelOtherActivity:BaseActivity(), AdapterView.OnItemSelectedListener {


    private var mpsManager: MPSManager? = null

    var transactionMode: MPSTransaction.TransactionMode? = null

    val dialogHelper = DialogHelper.getInstance()

    var typePaymentToCancel = ""
    var list_of_items = arrayOf("Payment Type","CREDIT","DEBIT","VOUCHER")

    val TAG = CancelOtherActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_other)

        title = getString(R.string.cancel_toolbar_title)

        spinner_cancel_other!!.onItemSelectedListener = this

        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)

        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner_cancel_other!!.adapter = arrayAdapter

        btnCancelOther!!.setOnClickListener {
            when (typePaymentToCancel) {
                "CREDIT" -> transactionMode = MPSTransaction.TransactionMode.CREDIT
                "DEBIT" -> transactionMode = MPSTransaction.TransactionMode.DEBIT
                "VOUCHER" -> transactionMode = MPSTransaction.TransactionMode.VOUCHER
            }
            if(et_doc.text.isEmpty() || et_autCode.text.isEmpty() || transactionMode == null) {

                Toast.makeText(this,"Complete all fields to cancel any",Toast.LENGTH_SHORT).show()

            } else {

                dialogHelper.showLoadingDialog(this, true)
                Log.d(TAG,"Type: $transactionMode")
                Log.d(TAG,"Doc: ${et_doc.text}")
                Log.d(TAG,"Auth: ${et_autCode.text}")
                //TODO change to get from activity
                TransactionTask(mpsManager!!, TransactionHelper.getInstance().mountTransaction(
                    "", transactionMode!!,et_doc.text.toString(),
                    et_autCode.text.toString(),0
                )!!, Constants.TransactionState.cancel).execute()
            }
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


    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position>0){
            typePaymentToCancel = list_of_items[position]
        }
    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null){
            mpsManager = MPSManager(this.applicationContext)
        }

        mpsManager!!.bindService(applicationContext)

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onCancelAnswer(mpsResult: MPSResult?) {
                super.onCancelAnswer(mpsResult)
                runOnUiThread {
                    dialogHelper.handleCancelAnswer(this@CancelOtherActivity,mpsResult)
                }
            }
        })
    }

}
