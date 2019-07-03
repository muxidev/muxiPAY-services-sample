package muxi.sample.ui.present_card.callbacks

import android.content.ComponentName
import android.util.Log
import muxi.sample.data.MPSTransactionResult
import muxi.sample.service.MPSManager

open class DefaultCallback : MPSManager.MPSManagerCallback {

    companion object{
        val TAG = DefaultCallback::class.java.simpleName
    }
    override fun onCancelAnswer(mpsTransactionResult: MPSTransactionResult?) {
        Log.d(TAG,"onCancelAnswer"+mpsTransactionResult.toString())
    }

    override fun onDeconfigureAnswer(mpsTransactionResult: MPSTransactionResult?) {
        Log.d(TAG,"onDeconfigureAnswer"+mpsTransactionResult.toString())
    }

    override fun onInitAnswer(mpsTransactionResult: MPSTransactionResult?) {
        Log.d(TAG,"onInitAnswer"+mpsTransactionResult.toString())
    }

    override fun onTransactionAnswer(mpsTransactionResult: MPSTransactionResult?) {
        Log.d(TAG,"onTransactionAnswer"+mpsTransactionResult.toString())
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.d(TAG,"onServiceDisconnected"+p0.toString())
    }

}