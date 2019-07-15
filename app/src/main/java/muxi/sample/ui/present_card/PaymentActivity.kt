package muxi.sample.ui.present_card

//import muxi.payservices.sdk.data.MPSTransaction.TransactionMode.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
import java.lang.Double.parseDouble
import java.text.NumberFormat

class PaymentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = PaymentActivity::class.java.simpleName

    /**
     * TODO: change this variable to use MAC address from your pinpad
     */
    private val bluetothDevice = "00:07:80:62:3D:37"

    private var mpsManager: MPSManager? = null
    val dialogHelper = DialogHelper.getInstance()
    val transactionHelper = TransactionHelper.getInstance()

    var transactionType: MPSTransaction.TransactionMode = MPSTransaction.TransactionMode.CREDIT

    val type = MPSTransaction.TransactionMode.CREDIT
    var installments = 0
    var list_of_items = arrayOf("installments","1","2","3","4","5","6",
        "7","8","9","10","11","12")
    private var currentValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        title = getString(R.string.payment_toolbar_title)

        spinner!!.onItemSelectedListener = this

        et_value.setSelection(et_value.text.length)

        mpsManager?.currentBluetoothDevice = bluetothDevice

        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)

        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner!!.adapter = aa

        btn_pay!!.setOnClickListener {
            mpsManager?.currentBluetoothDevice = bluetothDevice
            dialogHelper.showLoadingDialog(this, View.VISIBLE)
            val date = FormatUtils.getCurrentDate()
            val time = FormatUtils.getCurrentTime(false)
            val dateTime = "$date $time"
            transactionHelper.dateLast = dateTime
            transactionHelper.amountLast = currentValue
            transactionHelper.typeLast = transactionType.name
            TransactionTask(mpsManager!!,transactionHelper.mountTransaction(
                currentValue, transactionType,"","",installments
            )!!, Constants.TransactionState.payment).execute()
        }

        btn_credit!!.setOnClickListener{
            buttonEffect(btn_credit, MPSTransaction.TransactionMode.CREDIT,btn_debit,btn_voucher)
        }

        btn_debit!!.setOnClickListener{
            buttonEffect(btn_debit, MPSTransaction.TransactionMode.DEBIT,btn_credit,btn_voucher)
        }

        btn_voucher!!.setOnClickListener {
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

                    var cleanString: String = s.toString().replace(Regex("[R$,.]"), "")
                    Log.d(TAG, "cleasnString:$cleanString")

                    var parsed: Double = parseDouble(cleanString)
                    Log.d(TAG, "parsed:$parsed")

                    var formatted: String = NumberFormat.getCurrencyInstance().format((parsed/100))
                    Log.d(TAG, "formatted:$formatted")

                    currentValue = cleanString
                    et_value.setText(formatted)
                    et_value.setSelection(formatted.length)
                    et_value.addTextChangedListener(this)
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
            mpsManager = MPSManager(this.applicationContext)

        mpsManager!!.bindService(applicationContext)

//        val callbackManager = CallbackManager.getInstance(applicationContext, dialogHelper)

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onTransactionAnswer(mpsResult: MPSResult?) {
                super.onTransactionAnswer(mpsResult)

                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var receipt = ""
                    var body = ""
                    var showReceipt = false

                    if (mpsResult!!.status == MPSResult.Status.SUCCESS) {
                        title = resources.getString(R.string.transactionSuccess)
                        receipt = mpsResult.clientReceipt
                        showReceipt = true

                    }
                    else if (mpsResult.status == MPSResult.Status.SUCCESS) {
                        title = resources.getString(R.string.transactionError)
                        body = mpsResult.descriptionError
                    }
                    dialogHelper.showTransactionDialog(
                        this@PaymentActivity,
                        title,
                        body,receipt, showReceipt
                    )
                }

            }
        })
    }

    private fun buttonEffect(buttonPressed: Button, type: MPSTransaction.TransactionMode, buttonUnpressed: Button, buttonUnpressedTwo: Button) {

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
