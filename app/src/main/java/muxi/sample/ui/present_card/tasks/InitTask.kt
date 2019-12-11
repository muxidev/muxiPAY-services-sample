package muxi.sample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.payservices.sdk.service.MPSManager

class InitTask(val mpsManager: MPSManager,
               val showMessage: Boolean,
               val merchantId: String,
               val token: String): AsyncTask<Void, Void, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        mpsManager.initialize(showMessage, merchantId, token)
        return null
    }

}