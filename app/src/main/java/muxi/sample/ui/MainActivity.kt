package muxi.sample.ui

import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import muxi.sample.R
import muxi.sample.data.MPSTransaction
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
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
            mpsManager!!.initialize(showMessage, cnpj)
        }
        btnTransact.setOnClickListener {
            mpsManager!!.makePaymentWithPinpad(mpsTransaction)
        }
        btnCancel.setOnClickListener {
            mpsManager!!.cancelTransaction(mpsTransaction)
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
        mpsManager!!.setMpsManagerCallback(mpsManagerCallback)

    }

    override fun onDestroy() {
        mpsManager!!.unbindService(this)
        super.onDestroy()
    }



    private val mpsManagerCallback = object : MPSManager.MPSManagerCallback {
        override fun onCancelAnswer(mpsTransactionResult: MPSTransactionResult?) {
            toast(mpsTransactionResult!!.clientReceipt)
        }

        override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
            toast(mpsTransactionResult!!.transactionStatus.name)
        }

        override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
            toast(mpsTransactionResult!!.transactionStatus.name)
        }

        override fun onTransactionAnswer(mpsTransactionResult: MPSTransactionResult?) {
            toast(mpsTransactionResult!!.clientReceipt)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            toast(componentName.toString())
        }

    }

}
