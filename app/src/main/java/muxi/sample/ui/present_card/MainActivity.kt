package muxi.sample.ui.present_card

import android.content.ComponentName
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import muxi.sample.Constants
import muxi.sample.R
import muxi.sample.data.MPSTransaction
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.DeconfigureTask
import muxi.sample.ui.present_card.tasks.InitTask
import muxi.sample.ui.present_card.tasks.TransactionTask
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    private val TAG = MainActivity::class.java.simpleName

    /**
     * TODO: change this variable to use MAC address from your pinpad
     */
    private val bluetothDevice = "10:D0:7A:31:27:7C"
    /**
     * TODO: change this variable to decide if messages will be shown in pinpad or not
     * For example: "Remove card or insert card" messages
     */

    private val showMessage = true
    /**
     * TODO: change this variable to use your CNPJ
     */

    private val ignorePendingTransaction = true
    private val cnpj = "1234"

    val value = "2200"
    val type = MPSTransaction.TransactionType.CREDIT
    val installments = 1
    val mpsTransaction = mountTransaction(value,type,installments)
    val dialogHelper = DialogHelper.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInit.setOnClickListener {
            dialogHelper.showLoadingDialog(this)
            InitTask(mpsManager!!,showMessage,cnpj).execute()
        }
        btnTransact.setOnClickListener {
            dialogHelper.showLoadingDialog(this)
            TransactionTask(mpsManager!!,mpsTransaction!!,Constants.TransactionState.payment).execute()
        }
        btnCancel.setOnClickListener {
            dialogHelper.showLoadingDialog(this)
            TransactionTask(mpsManager!!,mpsTransaction!!,Constants.TransactionState.cancel).execute()
        }
        btnDeconfigure.setOnClickListener {
            dialogHelper.showLoadingDialog(this)
            DeconfigureTask(mpsManager!!,ignorePendingTransaction).execute()
        }
        btnHistory.setOnClickListener {
            toast(getString(R.string.built_screen))
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
            this@MainActivity.runOnUiThread{
                dialogHelper.hideLoadingDialog()
                //TODO add treatment to !! , when cannot have null branch
                when(mpsTransactionResult!!.transactionStatus){
                    MPSTransactionResult.TransactionStatus.SUCCESS ->
                        toast(mpsTransactionResult.clientReceipt)
                    MPSTransactionResult.TransactionStatus.ERROR->
                        toast(mpsTransactionResult.descriptionError)
                }
            }
        }

        override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
            this@MainActivity.runOnUiThread{
                dialogHelper.hideLoadingDialog()
                toast(mpsTransactionResult!!.transactionStatus.name)
            }
        }

        override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
            this@MainActivity.runOnUiThread {
                dialogHelper.hideLoadingDialog()
                toast(mpsTransactionResult!!.transactionStatus.name)

            }
        }

        override fun onTransactionAnswer(mpsTransactionResult: MPSTransactionResult?) {
            this@MainActivity.runOnUiThread{
                dialogHelper.hideLoadingDialog()
                when(mpsTransactionResult!!.transactionStatus){
                    MPSTransactionResult.TransactionStatus.SUCCESS ->
                        toast(mpsTransactionResult.clientReceipt)
                    MPSTransactionResult.TransactionStatus.ERROR->
                        toast(mpsTransactionResult.descriptionError)
                }
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            toast(componentName.toString())
        }

    }

}
