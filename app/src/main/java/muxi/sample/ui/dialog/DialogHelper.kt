package muxi.sample.ui.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.dialog_init_answer.view.*
import kotlinx.android.synthetic.main.dialog_transaction_answer.view.*
import muxi.payservices.sdk.data.MPSResult
import muxi.sample.Constants.CLIENT_RECEIPT
import muxi.sample.Constants.ESTABLISHMENT_RECEIPT
import muxi.sample.R
import muxi.sample.TransactionHelper
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

    fun showLoadingDialog(context: Context, isToShowWaitingPinpad: Boolean) {
        var layoutId: Int = R.layout.dialog_loading
        if(isToShowWaitingPinpad)
            layoutId = R.layout.dialog_waiting_pinpad
        val view = LayoutInflater.from(context).inflate(layoutId,
            null,false)

        alertDialog = AlertDialog.Builder(context)
            .setView(view)
            .create()

        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.show()
    }

    fun hideLoadingDialog() {
        alertDialog!!.hide()
    }

    fun showInitDialog(context: Context, title:String, body:String){

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_init_answer,null,false)
        view.tv_init.text = body
        alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(view)
            .setPositiveButton(context.getString(R.string.ok)) { alertDialog, _ -> alertDialog.cancel()}
            .create()

        alertDialog?.let {
            alertDialog?.show()
        }
    }

    fun showTransactionDialog(context: Context, title:String, body:String, establishmentReceipt:String,
                              clientReceipt: String, isTransactionApproved: Boolean){

        var pathToImage = "img_approved.json"

        var negativeButton = context.getString(R.string.receipt)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_transaction_answer,null,false)
        view.tv_body.setTextColor(Color.BLACK)
        view.tv_body.text = body
        view.tv_title.text = title
        val lottieAnimationView: LottieAnimationView = view.findViewById(R.id.animation)
        if(!isTransactionApproved){
            pathToImage = "img_denied.json"
            negativeButton = context.getString(R.string.try_again)
        }
        lottieAnimationView.setAnimation(pathToImage)
        lottieAnimationView.playAnimation()
        alertDialog = AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                alertDialog!!.cancel()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context, intent, null)
            }
            .setNegativeButton(negativeButton) { _, _ ->
                alertDialog!!.cancel()
                if(isTransactionApproved){
                    val intent = Intent(context, ReceiptActivity::class.java).apply {
                        putExtra(ESTABLISHMENT_RECEIPT, establishmentReceipt)
                        putExtra(CLIENT_RECEIPT, clientReceipt)
                    }
                startActivity(context, intent, null)
                }
            }
            .create()
        alertDialog!!.show()

    }


    fun handleCancelAnswer(context:Context, mpsTransactionResult: MPSResult?) {
        hideLoadingDialog()
        val transactionHelper = TransactionHelper.getInstance()
        var body = ""
        var establishmentReceipt = ""
        var clientReceipt = ""
        var showReceipt = false
        //TODO add treatment to !! , when cannot have null branch
        when (mpsTransactionResult!!.status) {
            MPSResult.Status.SUCCESS -> {
                body = context.getString(R.string.cancelSuccess)
                establishmentReceipt = mpsTransactionResult.establishmentReceipt
                clientReceipt = mpsTransactionResult.clientReceipt
                showReceipt = true
                transactionHelper.dateLast = ""
                transactionHelper.amountLast = ""
                transactionHelper.typeLast = ""
            }
            MPSResult.Status.ERROR -> {
                body = mpsTransactionResult.descriptionError
            }
        }

        showTransactionDialog(context,
            mpsTransactionResult.status.name,
            body, establishmentReceipt, clientReceipt, showReceipt
        )
    }

}