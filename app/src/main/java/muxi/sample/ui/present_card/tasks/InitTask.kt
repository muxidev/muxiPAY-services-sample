package muxi.sample.ui.present_card.tasks

import android.os.AsyncTask
import muxi.sample.service.MPSManager

class InitTask(val mpsManager: MPSManager,
               val showMessage: Boolean,
               val cnpj: String): AsyncTask<Void, Void, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        mpsManager.initialize(showMessage,cnpj)
        return null
    }

}