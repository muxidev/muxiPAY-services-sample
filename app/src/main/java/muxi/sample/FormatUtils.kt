package muxi.sample

import java.text.SimpleDateFormat
import java.util.*

object FormatUtils {


    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return sdf.format(Calendar.getInstance(Locale.getDefault()).time)
    }

    fun getCurrentTime(format24: Boolean): String {

        var dateFor = "HH:mm"
        if (!format24) {
            dateFor += " a"
        }

        val sdf = SimpleDateFormat(dateFor, Locale.getDefault())
        val now = Calendar.getInstance(Locale.getDefault())
        return sdf.format(now.time)
    }

    fun getValueReplaced(value: String): String {
        return value.replace("R", "")
                .replace("$", "")
                .replace(",", "")
        // Para valores maiores que R$ 999,99
                .replace(".", "")
    }
}