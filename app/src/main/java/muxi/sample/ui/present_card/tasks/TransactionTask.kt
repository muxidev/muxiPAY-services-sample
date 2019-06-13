package muxi.sample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.sample.Constants
import muxi.sample.data.MPSTransaction
import muxi.sample.service.MPSManager

class TransactionTask(val mpsManager: MPSManager,
                      val mpsTransaction: MPSTransaction,
                      val transactionState: Constants.TransactionState): AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        when(transactionState){
            Constants.TransactionState.payment->
                mpsManager.makePaymentWithPinpad(mpsTransaction)
            Constants.TransactionState.cancel->
                mpsManager.cancelTransaction(mpsTransaction)
        }
        return null
    }
}