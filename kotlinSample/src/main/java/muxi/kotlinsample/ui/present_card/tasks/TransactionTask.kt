package muxi.kotlinsample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.payservices.sdk.data.MPSTransaction
import muxi.kotlinsample.Constants
import muxi.payservices.sdk.service.IMPSManager

class TransactionTask(val mpsManager: IMPSManager,
                      val mpsTransaction: MPSTransaction,
                      val transactionState: Constants.TransactionState): AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        when(transactionState){
            Constants.TransactionState.payment->
                mpsManager.transaction(mpsTransaction)
            Constants.TransactionState.cancel->
                mpsManager.cancelTransaction(mpsTransaction)
        }
        return null
    }
}