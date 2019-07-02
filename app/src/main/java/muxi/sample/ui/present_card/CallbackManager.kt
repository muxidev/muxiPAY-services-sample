package muxi.sample.ui.present_card

import android.content.ComponentName
import android.content.Context
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
import muxi.sample.ui.dialog.DialogHelper
import android.os.Looper
import android.os.Handler
import android.util.Log
import muxi.sample.R
import muxi.sample.TransactionHelper


class CallbackManager(context: Context, dialogHelper: DialogHelper) {


    interface ViewI {
        fun onShowProgress()
        fun onHideProgress()
        fun onTransactionSucess(clientReceipt: String)
        fun onTransactionError(descriptionError :String)
        fun onShowDialog(message:String)
    }

    interface cancelAnser
    val transactionHelper = TransactionHelper.getInstance()

    private val TAG = CallbackManager::class.java.simpleName

    companion object {
        private var instance: CallbackManager? = null
        fun getInstance(context: Context, dialogHelper: DialogHelper): CallbackManager {
            if(instance == null)
                instance =
                    CallbackManager(context, dialogHelper)
            return instance!!
        }
    }

    val mpsManagerCallback = object : MPSManager.MPSManagerCallback {
        override fun onCancelAnswer(mpsTransactionResult: MPSTransactionResult?) {

        }

        override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
            Handler(Looper.getMainLooper()).post{

                dialogHelper.hideLoadingDialog()
                var title = ""
                var body = ""
                when(mpsTransactionResult!!.transactionStatus.name){
                    MPSTransactionResult.TransactionStatus.SUCCESS.name ->{
                        title = context.resources.getString(R.string.deconfigureSuccess)
                    }
                    MPSTransactionResult.TransactionStatus.ERROR.name->{
                        title = context.resources.getString(R.string.deconfigureError)
                        body = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showInitDialog(context,
                    title,
                    body
                )
                Log.d(TAG,mpsTransactionResult.transactionStatus.name)

            }
        }

        override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
            Handler(Looper.getMainLooper()).post {
                dialogHelper.hideLoadingDialog()
                var title = ""
                var body = ""
                when(mpsTransactionResult!!.transactionStatus.name){
                    MPSTransactionResult.TransactionStatus.SUCCESS.name -> {
                        title = context.resources.getString(R.string.initSuccess)
                    }
                    MPSTransactionResult.TransactionStatus.ERROR.name->{
                        title = context.resources.getString(R.string.initError)
                        body = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showInitDialog(context,
                    title,
                    body
                )
                Log.d(TAG,mpsTransactionResult.transactionStatus.name)


            }
        }

        override fun onTransactionAnswer(mpsTransactionResult: MPSTransactionResult?) {

        }


        override fun onServiceDisconnected(componentName: ComponentName?) {
            Log.d(TAG,componentName.toString())
        }

    }
}