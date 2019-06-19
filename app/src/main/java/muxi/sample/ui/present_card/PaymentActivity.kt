package muxi.sample.ui.present_card

import android.content.ComponentName
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_payment.*
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.data.MPSTransaction
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
import muxi.sample.ui.CallbackManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask
import org.jetbrains.anko.toast
import java.lang.Double.parseDouble
import java.text.NumberFormat

class PaymentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = PaymentActivity::class.java.simpleName

    private var mpsManager: MPSManager? = null
    val dialogHelper = DialogHelper.newInstance()

    var transactionType: MPSTransaction.TransactionType = MPSTransaction.TransactionType.CREDIT

    val type = MPSTransaction.TransactionType.CREDIT
    var installments = 1
    var list_of_items = arrayOf("installments","1","2","3","4","5","6",
        "7","8","9","10","11","12")
    private var paymentType = ""
    private var currentValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        spinner!!.onItemSelectedListener = this

        val mpsTransaction =  mountTransaction("1000",type,installments)

        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)

        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner!!.adapter = aa

        btn_pay!!.setOnClickListener {
            DialogHelper.newInstance().showLoadingDialog(this,View.VISIBLE)
            dialogHelper.showLoadingDialog(this, View.VISIBLE)
            TransactionTask(mpsManager!!,mpsTransaction!!, Constants.TransactionState.payment).execute()
        }

        btn_credit!!.setOnClickListener{
            buttonEffect(btn_credit, MPSTransaction.TransactionType.CREDIT,btn_debit,btn_voucher)
        }

        btn_debit!!.setOnClickListener{
            buttonEffect(btn_debit, MPSTransaction.TransactionType.DEBIT,btn_credit,btn_voucher)
        }

        btn_voucher!!.setOnClickListener {
            buttonEffect(btn_voucher, MPSTransaction.TransactionType.VOUCHER,btn_credit,btn_debit)
        }

        et_value.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != currentValue){
                    et_value.removeTextChangedListener(this)

                    var cleanString: String = s.toString().replace("[$,.]", "")
                    Log.d(TAG, "cleasnString:$cleanString")

                    var parsed: Double = parseDouble(cleanString)
                    Log.d(TAG, "parsed:$parsed")

                    var formatted: String = NumberFormat.getCurrencyInstance().format((parsed/100))
                    Log.d(TAG, "formatted:$formatted")

                    currentValue = formatted
                    et_value.setText(formatted)
                    et_value.setSelection(formatted.length)
                    et_value.addTextChangedListener(this)
                }
            }

        })

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position>0){
            installments = position
        }
    }

    override fun onStart() {
        super.onStart()

        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this.applicationContext)

        val bindService = mpsManager!!.bindService(applicationContext)

        val callbackManager = CallbackManager.newInstance(this, dialogHelper)
        mpsManager!!.setMpsManagerCallback(callbackManager.mpsManagerCallback)

    }

    private fun mountTransaction(value: String, type: MPSTransaction.TransactionType,
                                 installments: Int): MPSTransaction? {
        val transaction = MPSTransaction()
        transaction.amount = value
        transaction.currency = MPSTransaction.CurrencyType.BRL
        transaction.type = type
        transaction.installments = installments

        return transaction

    }
    private fun buttonEffect(buttonPressed: Button, type: MPSTransaction.TransactionType, buttonUnpressed: Button, buttonUnpressedTwo: Button) {

        buttonPressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_base)
        buttonPressed.setTextColor(ContextCompat.getColor(this,R.color.color_text_pressed))

        buttonUnpressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressed.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        buttonUnpressedTwo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_btn_unpressed)
        buttonUnpressedTwo.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        Log.d(TAG, "Type Payment: ${type.name}")
        transactionType = type
    }


}
