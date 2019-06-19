package muxi.sample.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_answer.view.*
import kotlinx.android.synthetic.main.dialog_loading.view.*
import muxi.sample.R

class DialogHelper {

    var alertDialog: AlertDialog? = null
    companion object {
        fun newInstance():DialogHelper{
            return DialogHelper()
        }
    }

    fun showLoadingDialog(context: Context, isWaitingPinpadIsVisible: Int ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading,
            null,false)
        view.tv_waiting_pp.visibility = isWaitingPinpadIsVisible
        alertDialog = AlertDialog.Builder(context)
            .setView(view)
            .show()
    }

    fun hideLoadingDialog() {
        alertDialog!!.hide()
    }

    fun showInitDialog(context: Context, title:String, body:String){

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_answer,
            null,false)
        view.tv_init.setText(body)
        alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(context.getString(R.string.ok)) { alertDialog, _ ->alertDialog.cancel()
            }
            .show()
    }

    fun showTransactionDialog(context: Context, title:String, body:String){

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_answer,
            null,false)
        view.tv_init.setText(body)
        alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(context.getString(R.string.ok)) { alertDialog, _ ->alertDialog.cancel()
            }
            .setNegativeButton(context.getString(R.string.receipt)) { alertDialog, _ ->alertDialog.cancel()

            }
            .show()
    }


}