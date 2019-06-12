package muxi.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import muxi.payservices.data.MPSTransaction
import muxi.payservices.data.MPSTransactionResult
import muxi.payservices.service.MPSManager
import muxi.sample.R
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    private val TAG = MainActivity::class.java.simpleName

    /**
     * TODO: change this variable to use MAC address from your pinpad
     */
    private val bluetothDevice = "B2:34:RE:43:RE"
    /**
     * TODO: change this variable to decide if messages will be shown in pinpad or not
     * For example: "Remove card or insert card" messages
     */

    private val showMessage = true
    /**
     * TODO: change this variable to use your CNPJ
     */

    private val cnpj = "1234"

    val value = "2200"
    val type = MPSTransaction.TransactionType.CREDIT
    val installments = 1
    val mpsTransaction = mountTransaction(value,type,installments)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInit.setOnClickListener {
            val answer = mpsManager!!.initialize(bluetothDevice,showMessage, cnpj)
            toast(answer)
        }
        btnTransact.setOnClickListener {
            mpsManager!!.makePaymentWithPinpad(mpsTransaction)
        }
        btnCancel.setOnClickListener {
            mpsManager!!.cancelTransaction(mpsTransaction,cancelPaymentListener)
        }
        btnDeconfigure.setOnClickListener {
            mpsManager!!.deconfigure(true)
        }


    }


    private fun mountTransaction(value: String, type: MPSTransaction.TransactionType,
                                 installments: Int): MPSTransaction? {
        val transaction = MPSTransaction()
        transaction.amount = value
        transaction.currency = MPSTransaction.CurrencyType.BRL
        transaction.type = type

        return transaction

    }

    override fun onStart() {
        super.onStart()

        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this.applicationContext)


        val bindService = mpsManager!!.bindService(applicationContext)
        mpsManager!!.setMakePaymentListener(paymentListener)

    }

    override fun onDestroy() {
        mpsManager!!.unbindService(this)
        super.onDestroy()
    }



    private val paymentListener = object : MPSManager.MakePaymentListener {
        override fun onSuccess(mpsTransactionResult: MPSTransactionResult?) {
            runOnUiThread {

                toast(mpsTransactionResult!!.clientReceipt)
            }
        }
        override fun onError(onError: String) {
            runOnUiThread {

                toast(onError)
            }
        }
    }

    private val cancelPaymentListener = object : MPSManager.CancelPaymentListener {
        override fun onGenericCancelSuccess(mpsTransactionResult: MPSTransactionResult?) {
            runOnUiThread {

                toast(mpsTransactionResult!!.clientReceipt)
            }
        }

        override fun onGenericCancelError(error: String?) {
            runOnUiThread {
                toast(error!!)
            }
        }

    }

}
