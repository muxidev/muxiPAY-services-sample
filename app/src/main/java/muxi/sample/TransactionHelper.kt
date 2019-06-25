package muxi.sample

import muxi.sample.data.MPSTransaction

class TransactionHelper {

    companion object {
        fun newInstance():TransactionHelper{
            return TransactionHelper()
        }
    }


    fun mountTransaction(value: String, type: MPSTransaction.TransactionType,
                         cv: String, authCode: String, installments: Int): MPSTransaction? {
        val transaction = MPSTransaction()
        transaction.amount = value
        transaction.currency = MPSTransaction.CurrencyType.BRL
        transaction.cv = cv
        transaction.auth = authCode
        transaction.type = type
        transaction.installments = installments

        return transaction

    }
}