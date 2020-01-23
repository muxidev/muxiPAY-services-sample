package muxi.kotlinsample

object Constants {

    const val PRESENT_CARD_ID = 0
    const val ECOMMERCE_ID = 1

    const val TYPES_SIZE = 2

    const val DEFAULT_INSTALLMENTS = 1
    const val ESTABLISHMENT_RECEIPT = "ESTABLISHMENT_RECEIPT"
    const val CLIENT_RECEIPT = "CLIENT_RECEIPT"

    enum class TransactionState {
        payment,
        cancel
    }


    /**
     * TODO: change this variable to use your Merchant Id
     */
    const val MERCHANT_ID = "9876"
}