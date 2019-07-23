package muxi.sample

import muxi.payservices.sdk.data.MPSTransaction


class TransactionHelper {
    var dateLast: String = ""
    var amountLast: String = ""
    var typeLast: String = ""

    companion object {
        private var instance: TransactionHelper? = null

        fun getInstance():TransactionHelper{
            if(instance == null)
                instance = TransactionHelper()
            return instance!!
        }
    }


    fun mountTransaction(value: String, mode: MPSTransaction.TransactionMode,
                         cv: String, authCode: String, installments: Int): MPSTransaction? {
        val transaction = MPSTransaction()
        transaction.amount = value
        transaction.currency = MPSTransaction.CurrencyType.BRL
        transaction.cv = cv
        transaction.auth = authCode
        transaction.transactionMode = mode
        transaction.installments = installments

        return transaction

    }
}