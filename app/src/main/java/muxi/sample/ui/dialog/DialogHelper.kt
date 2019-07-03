package muxi.sample.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.dialog_answer.view.*
import kotlinx.android.synthetic.main.dialog_loading.view.*
import muxi.sample.Constants.RECEIPT_PARAM
import muxi.sample.R
import muxi.sample.TransactionHelper
import muxi.sample.data.MPSTransactionResult
import muxi.sample.ui.present_card.MainActivity
import muxi.sample.ui.present_card.ReceiptActivity

class DialogHelper {

    var alertDialog: AlertDialog? = null
    var textColor : Int = Color.BLACK


    companion object {
        private var instance: DialogHelper? = null
        fun getInstance():DialogHelper{
            if(instance == null)
                instance = DialogHelper()
            return instance!!
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

    fun showTransactionDialog(context: Context, title:String, body:String, receipt:String, showReceipt: Boolean){

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_answer,null,false)
        view.tv_init.setTextColor(textColor)
        view.tv_init.text = body
        alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(context.getString(R.string.ok),object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    alertDialog!!.cancel()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(context, intent, null)
                }})
            .setNegativeButton(context.getString(R.string.receipt),object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    alertDialog!!.cancel()
                    val intent = Intent(context, ReceiptActivity::class.java).apply {
                        putExtra(RECEIPT_PARAM, receipt)
                    }
                    startActivity(context, intent, null)
                }})
            .create()

        alertDialog!!.show()
//            .setPositiveButton(context.getString(R.string.ok)) { alertDialog, _ ->
//                alertDialog.cancel()
//                val intent = Intent(context, MainActivity::class.java)
//                startActivity(context, intent, null)
//            }
//            .setNegativeButton(context.getString(R.string.receipt)) { alertDialog, _ ->
//                alertDialog.cancel()
//                val intent = Intent(context, ReceiptActivity::class.java).apply {
//                    putExtra(RECEIPT_PARAM, receipt)
//                }
//                startActivity(context, intent, null)
//            }

        if(!showReceipt)
            alertDialog!!.getButton(AlertDialog.BUTTON_NEGATIVE).visibility = View.GONE
    }


    fun handleCancelAnswer(context:Context, mpsTransactionResult: MPSTransactionResult?) {
        hideLoadingDialog()
        val transactionHelper = TransactionHelper.getInstance()
        var body = ""
        var result = ""
        var showReceipt = false
        //TODO add treatment to !! , when cannot have null branch
        when (mpsTransactionResult!!.transactionStatus) {
            MPSTransactionResult.TransactionStatus.SUCCESS -> {
                body = context.getString(R.string.cancelSuccess)
                result = mpsTransactionResult.clientReceipt
                showReceipt = true
                transactionHelper.dateLast = ""
                transactionHelper.amountLast = ""
                transactionHelper.typeLast = ""
            }
            MPSTransactionResult.TransactionStatus.ERROR -> {
                body = context.getString(R.string.cancelError)
                result = mpsTransactionResult.descriptionError
            }
        }

        showTransactionDialog(context,
            mpsTransactionResult.transactionStatus.name,
            body, result, showReceipt
        )
    }

}