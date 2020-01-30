package muxi.kotlinsample.ui.present_card

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import muxi.payservices.sdk.data.MPSResult
import muxi.payservices.sdk.service.CallbackAnswer
import muxi.payservices.sdk.service.IMPSManager
import muxi.kotlinsample.BuildConfig
import muxi.kotlinsample.Constants
import muxi.kotlinsample.R
import muxi.kotlinsample.ui.dialog.DialogHelper
import muxi.kotlinsample.ui.present_card.tasks.DeconfigureTask
import muxi.kotlinsample.ui.present_card.tasks.InitTask
import org.jetbrains.anko.toast
import muxi.kotlinsample.ui.BaseActivity
import muxi.payservices.sdk.service.MPSManager


class MainActivity : BaseActivity() {

    private var mpsManager: IMPSManager? = null

    private val TAG = MainActivity::class.java.simpleName

    /**
     * TODO: change this variable to decide if messages will be shown in pinpad or not
     * For example: "Remove card or insert card" messages
     */
    private val showMessage = true

    private val ignorePendingTransaction = true

    private val adapter by lazy { ItemAdapter() }

    val dialogHelper = DialogHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.present_card_toolbar_title)

        rv_button_list.adapter = adapter
        rv_button_list.layoutManager =LinearLayoutManager(this)

        adapter.addItem(ItemAdapter.Item("Initialize", View.OnClickListener {
            dialogHelper.showLoadingDialog(this, false)
            InitTask(
                mpsManager!!,
                showMessage,
                Constants.MERCHANT_ID,
                BuildConfig.API_KEY
            ).execute()
        }))

        adapter.addItem(ItemAdapter.Item("Transact", View.OnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }))

        adapter.addItem(ItemAdapter.Item("Cancel", View.OnClickListener {
            startActivity(Intent(this, CancelActivity::class.java))
        }))

        adapter.addItem(ItemAdapter.Item("Deconfigure", View.OnClickListener {
            dialogHelper.showLoadingDialog(this, false)
            DeconfigureTask(
                mpsManager!!,
                ignorePendingTransaction
            ).execute()
        }))

        adapter.addItem(ItemAdapter.Item("History", View.OnClickListener {
            toast(getString(R.string.built_screen))
        }))
    }

    override fun onStart() {
        super.onStart()
        if(mpsManager == null){
            mpsManager = MPSManager(this)
        }

        mpsManager!!.bindService(this)

        mpsManager!!.setMpsManagerCallback(object : CallbackAnswer(){
            override fun onDeconfigureAnswer(mpsResult: MPSResult?) {
                super.onDeconfigureAnswer(mpsResult)

                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var body = ""
                    when(mpsResult?.status){
                        MPSResult.Status.SUCCESS->{
                            title = getString(R.string.deconfigureSuccess)
                        }
                        MPSResult.Status.ERROR->{
                            title = getString(R.string.deconfigureError)
                            body = mpsResult.descriptionError
                        }
                    }

                    dialogHelper.showInitDialog(this@MainActivity,title,body)
                    Log.d(TAG,mpsResult?.status?.name)
                }

            }

            override fun onInitAnswer(mpsResult: MPSResult?) {
                super.onInitAnswer(mpsResult)

                runOnUiThread {
                    dialogHelper.hideLoadingDialog()
                    var title = ""
                    var body = ""
                    when(mpsResult?.status){
                        MPSResult.Status.SUCCESS -> {
                            title = getString(R.string.initSuccess)
                        }
                        MPSResult.Status.ERROR->{
                            title = getString(R.string.initError)
                            body = mpsResult.descriptionError
                        }
                    }
                    dialogHelper.showInitDialog(this@MainActivity,title,body)
                    Log.d(TAG,mpsResult?.status?.name)
                }

            }
        })
    }

    override fun onDestroy() {
        mpsManager!!.unbindService(this)
        super.onDestroy()
    }

}
