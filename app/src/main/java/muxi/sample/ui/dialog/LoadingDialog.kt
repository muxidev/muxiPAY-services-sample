package muxi.sample.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import muxi.sample.R

class DialogHelper {

    var alertDialog: AlertDialog? = null
    companion object {
        fun newInstance():DialogHelper{
            return DialogHelper()
        }
    }

    fun showLoadingDialog(context: Context) {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading,
            null,false)
        alertDialog = AlertDialog.Builder(context)
            .setView(view)
            .show()
    }

    fun hideLoadingDialog() {
        alertDialog!!.hide()
    }
}