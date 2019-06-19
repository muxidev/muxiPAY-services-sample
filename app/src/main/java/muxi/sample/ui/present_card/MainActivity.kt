package muxi.sample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import muxi.sample.R
import muxi.sample.service.MPSManager
import muxi.sample.ui.CallbackManager
import muxi.sample.ui.dialog.DialogHelper
import muxi.sample.ui.present_card.tasks.DeconfigureTask
import muxi.sample.ui.present_card.tasks.InitTask
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

    val dialogHelper = DialogHelper.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInit.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.GONE)
            InitTask(mpsManager!!,showMessage,cnpj).execute()
        }
        btnTransact.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
        btnCancel.setOnClickListener {
          //  dialogHelper.showLoadingDialog(this, View.VISIBLE)
           // TransactionTask(mpsManager!!,mpsTransaction!!,Constants.TransactionState.cancel).execute()
        }
        btnDeconfigure.setOnClickListener {
            dialogHelper.showLoadingDialog(this, View.GONE)
            DeconfigureTask(mpsManager!!,ignorePendingTransaction).execute()
        }
        btnHistory.setOnClickListener {
            toast(getString(R.string.built_screen))
        }


    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null)
            mpsManager = MPSManager.getInstance(this.applicationContext)

        mpsManager!!.bindService(applicationContext)
        mpsManager!!.setMpsManagerCallback(CallbackManager.newInstance(this, dialogHelper).mpsManagerCallback)
    }

    override fun onDestroy() {
        mpsManager!!.unbindService(this)
        super.onDestroy()
    }

}
