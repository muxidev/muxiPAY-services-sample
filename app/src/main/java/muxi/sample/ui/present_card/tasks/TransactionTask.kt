package muxi.sample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.payservices.sdk.data.MPSTransaction
import muxi.payservices.sdk.service.MPSManager
import muxi.sample.Constants

class TransactionTask(val mpsManager: MPSManager,
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