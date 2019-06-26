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
import muxi.sample.TransactionHelper


class CallbackManager(context: Context, dialogHelper: DialogHelper) {

    val transactionHelper = TransactionHelper.getInstance()

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
                var result = ""
                var showReceipt = false
                //TODO add treatment to !! , when cannot have null branch
                when(mpsTransactionResult!!.transactionStatus) {
                    MPSTransactionResult.TransactionStatus.SUCCESS -> {
                        body = context.resources.getString(R.string.cancelSuccess)
                        result = mpsTransactionResult.clientReceipt
                        showReceipt = true
                        transactionHelper.dateLast = ""
                        transactionHelper.amountLast = ""
                        transactionHelper.typeLast = ""
                    }
                    MPSTransactionResult.TransactionStatus.ERROR -> {
                        body = context.resources.getString(muxi.sample.R.string.cancelError)
                        result = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showTransactionDialog(context,
                    mpsTransactionResult.transactionStatus.name,
                    body,result,showReceipt
                )
            }

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
            Handler(Looper.getMainLooper()).post {
                dialogHelper.hideLoadingDialog()
                var title = ""
                var receipt = ""
                var body = ""
                var showReceipt = false
                when (mpsTransactionResult!!.transactionStatus) {
                    MPSTransactionResult.TransactionStatus.SUCCESS -> {
                        title = context.resources.getString(R.string.transactionSuccess)
                        receipt = mpsTransactionResult.clientReceipt
                        showReceipt = true

                    }
                    MPSTransactionResult.TransactionStatus.ERROR -> {
                        title = context.resources.getString(R.string.transactionError)
                        body = mpsTransactionResult.descriptionError
                    }
                }
                dialogHelper.showTransactionDialog(
                    context,
                    title,
                    body,receipt, showReceipt
                )
            }
        }


        override fun onServiceDisconnected(componentName: ComponentName?) {
            Log.d(TAG,componentName.toString())
        }

    }
}