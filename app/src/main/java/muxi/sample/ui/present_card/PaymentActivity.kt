package muxi.sample.ui.present_card

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_payment.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.MPSManager
import muxi.sample.Constants
import muxi.sample.FormatUtils
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.TransactionTask
import java.text.NumberFormat
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import muxi.sample.ui.BaseActivity


class PaymentActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = PaymentActivity::class.java.simpleName

    /**
     * TODO: change this variable to use MAC address from your pinpad
     */
    private val bluetothDevice = "B0:F1:EC:E2:EA:78"

    private var mpsManager: MPSManager? = null
    val dialogHelper = DialogHelper.getInstance()
    val transactionHelper = TransactionHelper.getInstance()

    var transactionType: MPSTransaction.TransactionMode = MPSTransaction.TransactionMode.CREDIT

    var installments = 0
    var list_of_items = arrayOf("A vista","2x ","3x","4x","5x","6x",
        "7x","8x","9x","10x","11x","12x")
    private var currentValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        title = getString(R.string.payment_toolbar_title)

        spinner!!.onItemSelectedListener = this


        mpsManager?.currentBluetoothDevice = bluetoothDevice

        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)

        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner!!.adapter = aa

        btn_pay!!.setOnClickListener {
            removeFocus()
            mpsManager?.currentBluetoothDevice = bluetoothDevice
            dialogHelper.showLoadingDialog(this, true)
            TransactionTask(mpsManager!!,transactionHelper.mountTransaction(
                currentValue, transactionType,"","",installments
            )!!, Constants.TransactionState.payment).execute()
        }

        btn_credit!!.setOnClickListener{
            ll_payment_info.visibility = View.VISIBLE
            buttonEffect(btn_credit, MPSTransaction.TransactionMode.CREDIT,btn_debit,btn_voucher)
        }

        btn_debit!!.setOnClickListener{
            ll_payment_info.visibility = View.INVISIBLE
            ll_rg_rate.visibility = View.GONE
            installments = 1
            spinner.setSelection(0)
            buttonEffect(btn_debit, MPSTransaction.TransactionMode.DEBIT,btn_credit,btn_voucher)
        }

        btn_voucher!!.setOnClickListener {
            ll_payment_info.visibility = View.INVISIBLE
            ll_rg_rate.visibility = View.GONE
            installments = 1
            spinner.setSelection(0)
            buttonEffect(btn_voucher, MPSTransaction.TransactionMode.VOUCHER,btn_credit,btn_debit)
        }

        et_value.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != currentValue){
                    et_value.removeTextChangedListener(this)

                    val cleanString: String = s.toString()
                        .replace(Regex("[R$,.]"), "")
                        .trim()
                    Log.d(TAG, "cleanString: $cleanString")

                    val parsed: Double = cleanString.toDouble()
                    Log.d(TAG, "parsed: $parsed")

                    val formatted: String = NumberFormat.getCurrencyInstance().format(parsed/100)
                    Log.d(TAG, "formatted: $formatted")

                    currentValue = cleanString
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
        removeFocus()
        if(position > 0)
            ll_rg_rate.visibility = View.VISIBLE
        else
            ll_rg_rate.visibility = View.GONE
        installments = (position + 1)
    }

    override fun onStart() {
        super.onStart()

        if(mpsManager == null)
            mpsManager = MPSManager(this.applicationContext)

        mpsManager!!.bindService(applicationContext)

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onTransactionAnswer(mpsResult: MPSResult?) {
                super.onTransactionAnswer(mpsResult)

                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var clientReceipt = ""
                    var establishmentReceipt = ""
                    var body = ""
                    var showReceipt = false

                    if (mpsResult!!.status == MPSResult.Status.SUCCESS) {
                        val date = FormatUtils.getCurrentDate()
                        val time = FormatUtils.getCurrentTime(false)
                        val dateTime = "$date $time"
                        transactionHelper.dateLast = dateTime
                        transactionHelper.amountLast = "R$ ${insertPointValue()}"
                        transactionHelper.typeLast = transactionType.name
                        title = resources.getString(R.string.transactionSuccess)
                        clientReceipt = mpsResult.clientReceipt
                        establishmentReceipt = mpsResult.establishmentReceipt
                        body = mountBodyMessage()
                        showReceipt = true

                    }
                    else if (mpsResult.status == MPSResult.Status.ERROR) {
                        title = resources.getString(R.string.transactionError)
                        body = mpsResult.descriptionError
                    }
                    dialogHelper.showTransactionDialog(
                        this@PaymentActivity,
                        title,
                        body,establishmentReceipt,clientReceipt, showReceipt
                    )
                }

            }
        })
    }

    private fun mountBodyMessage(): String {
        val value = insertPointValue()
        return "R$ $value\n$transactionType - $installments installments"

    }

    private fun insertPointValue(): String {
        var decimal = currentValue.takeLast(2)
        val value = currentValue.take(currentValue.length - decimal.length)
        decimal = ",$decimal"
        val reversedValue = value.reversed()
        val chunkedString = reversedValue.chunked(3)
        Log.d(TAG,"chunckedString: $chunkedString")
        var returnValue = ""
        for(part in chunkedString){

            returnValue += if(part.length == 3){
                "$part."
            } else {
                part
            }
        }

        return if(returnValue[returnValue.length - 1 ] == '.') {

            "${returnValue.take(returnValue.length - 1).reversed()}$decimal"

        } else {

            "${returnValue.reversed()}$decimal"

        }
    }

    private fun buttonEffect(buttonPressed: Button, type: MPSTransaction.TransactionMode, buttonUnpressed: Button, buttonUnpressedTwo: Button) {

        removeFocus()

        //TODO change to support API < 21
        buttonPressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_base)
        buttonPressed.setTextColor(ContextCompat.getColor(this,R.color.color_text_pressed))

        buttonUnpressed.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_text_pressed)
        buttonUnpressed.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        buttonUnpressedTwo.backgroundTintList = ContextCompat.getColorStateList(this,R.color.color_text_pressed)
        buttonUnpressedTwo.setTextColor(ContextCompat.getColor(this,R.color.color_base))

        Log.d(TAG, "Type Payment: ${type.name}")
        transactionType = type
    }

    private fun removeFocus() {
        window.decorView.clearFocus()
        et_value.clearFocus()
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


}
