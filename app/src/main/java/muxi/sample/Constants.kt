package muxi.sample

object Constants {

    const val PRESENT_CARD_ID = 0
    const val ECOMMERCE_ID = 1

    const val TYPES_SIZE = 2

    const val RECEIPT_PARAM = "RECEIPT_PARAM"

    enum class TransactionState {
        payment,
        cancel
    }
}