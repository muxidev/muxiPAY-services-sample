package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import muxi.sample.R
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.callbacks.DefaultCallback
import muxi.sample.ui.present_card.tasks.DeconfigureTask
import muxi.sample.ui.present_card.tasks.InitTask
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var mpsManager: MPSManager? = null

    private val TAG = MainActivity::class.java.simpleName


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

    val dialogHelper = DialogHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.title = getString(R.string.present_card_toolbar_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnInit.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.GONE)
            InitTask(mpsManager!!,showMessage,cnpj).execute()
        }
        btnTransact.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
        btnCancel.setOnClickListener {
            startActivity(Intent(this, CancelActivity::class.java))
        }
        btnDeconfigure.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.GONE)
            DeconfigureTask(mpsManager!!,ignorePendingTransaction).execute()
        }
        btnHistory.setOnClickListener {
            toast(getString(R.string.built_screen))
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this)

        mpsManager!!.bindService(this)
        mpsManager!!.setMpsManagerCallback(object : DefaultCallback() {
            override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
                super.onDeconfigureAnswer(mpsTransactionResult)

                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var body = ""
                    when(mpsTransactionResult!!.transactionStatus.name){
                        MPSTransactionResult.TransactionStatus.SUCCESS.name ->{
                            title = getString(R.string.deconfigureSuccess)
                        }
                        MPSTransactionResult.TransactionStatus.ERROR.name->{
                            title = getString(R.string.deconfigureError)
                            body = mpsTransactionResult.descriptionError
                        }
                    }

                    dialogHelper.showInitDialog(this@MainActivity,title,body)
                    Log.d(TAG,mpsTransactionResult.transactionStatus.name)
                }
            }

            override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
                super.onInitAnswer(mpsTransactionResult)
                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var body = ""
                    when(mpsTransactionResult!!.transactionStatus.name){
                        MPSTransactionResult.TransactionStatus.SUCCESS.name -> {
                            title = getString(R.string.initSuccess)
                        }
                        MPSTransactionResult.TransactionStatus.ERROR.name->{
                            title = getString(R.string.initError)
                            body = mpsTransactionResult.descriptionError
                        }
                    }
                    dialogHelper.showInitDialog(this@MainActivity,title,body)
                    Log.d(TAG,mpsTransactionResult.transactionStatus.name)
                }
            }
        })
    }

    override fun onDestroy() {
        mpsManager!!.unbindService(this)
        super.onDestroy()
    }

}
