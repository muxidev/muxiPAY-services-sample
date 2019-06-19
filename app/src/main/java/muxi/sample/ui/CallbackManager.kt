package muxi.sample.ui

import android.content.ComponentName
import android.content.Context
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager
import muxi.sample.ui.dialog.DialogHelper
import android.os.Looper
import android.os.Handler
import android.util.Log
import muxi.sample.R


class CallbackManager(context: Context, dialogHelper: DialogHelper) {

    private val TAG = CallbackManager::class.java.simpleName

    companion object {
        fun newInstance(context: Context, dialogHelper: DialogHelper): CallbackManager {
            return CallbackManager(context, dialogHelper)
        }
    }

    val mpsManagerCallback = object : MPSManager.MPSManagerCallback {
        override fun onCancelAnswer(mpsTransactionResult: MPSTransactionResult?) {

            Handler(Looper.getMainLooper()).post {
                dialogHelper.hideLoadingDialog()
                var body = ""
                var toastAnswer = ""
                //TODO add treatment to !! , when cannot have null branch
                when(mpsTransactionResult!!.transactionStatus) {
                    MPSTransactionResult.TransactionStatus.SUCCESS -> {
                        body = context.resources.getString(R.string.cancelSuccess)
                        toastAnswer = mpsTransactionResult.clientReceipt
                    }
                    MPSTransactionResult.TransactionStatus.ERROR -> {
                        body = context.resources.getString(muxi.sample.R.string.cancelError)
                        toastAnswer = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showTransactionDialog(context,
                    mpsTransactionResult.transactionStatus.name,
                    body
                )
                Log.d("CallbackManager", toastAnswer)
            }

        }

        override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
            Handler(Looper.getMainLooper()).post{

                dialogHelper.hideLoadingDialog()
                var body = ""
                when(mpsTransactionResult!!.transactionStatus.name){
                    MPSTransactionResult.TransactionStatus.SUCCESS.name -> body = context.resources.getString(R.string.deconfigureSuccess)
                    MPSTransactionResult.TransactionStatus.ERROR.name->body = context.resources.getString(R.string.deconfigureError)
                }
                dialogHelper.showInitDialog(context,
                    mpsTransactionResult.transactionStatus.name,
                    body
                )
                Log.d(TAG,mpsTransactionResult.transactionStatus.name)

            }
        }

        override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
            Handler(Looper.getMainLooper()).post {
                dialogHelper.hideLoadingDialog()
                var body = ""
                when(mpsTransactionResult!!.transactionStatus.name){
                    MPSTransactionResult.TransactionStatus.SUCCESS.name -> body = context.resources.getString(R.string.initSuccess)
                    MPSTransactionResult.TransactionStatus.ERROR.name->body = context.resources.getString(R.string.initError)
                }
                dialogHelper.showInitDialog(context,
                    mpsTransactionResult.transactionStatus.name,
                    body
                )
                Log.d(TAG,mpsTransactionResult.transactionStatus.name)


            }
        }

        override fun onTransactionAnswer(mpsTransactionResult: MPSTransactionResult?) {
            Handler(Looper.getMainLooper()).post {
                dialogHelper.hideLoadingDialog()
                var body = ""
                var toastAnswer = ""
                when (mpsTransactionResult!!.transactionStatus) {
                    MPSTransactionResult.TransactionStatus.SUCCESS -> {
                        body = context.resources.getString(R.string.transactionSuccess)
                        toastAnswer = mpsTransactionResult.clientReceipt
                    }
                    MPSTransactionResult.TransactionStatus.ERROR -> {
                        body = context.resources.getString(R.string.transactionError)
                        toastAnswer = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showTransactionDialog(
                    context,
                    mpsTransactionResult.transactionStatus.name,
                    body
                )
                Log.d(TAG, toastAnswer)
            }
        }


        override fun onServiceDisconnected(componentName: ComponentName?) {
            Log.d(TAG,componentName.toString())
        }

    }
}