package muxi.kotlinsample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.payservices.sdk.service.IMPSManager

class DeconfigureTask (val mpsManager: IMPSManager,
                       val ignore: Boolean): AsyncTask<Void, Void, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        mpsManager.deconfigure(ignore)
        return null
    }

}