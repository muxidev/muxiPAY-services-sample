package muxi.sample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.payservices.sdk.service.MPSManager

class DeconfigureTask (val mpsManager: MPSManager,
                       val ignore: Boolean): AsyncTask<Void, Void, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        mpsManager.deconfigure(ignore)
        return null
    }

}