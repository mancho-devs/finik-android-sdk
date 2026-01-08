package kg.finik.android.sdk

import android.content.Context
import android.content.Intent

object FinikSdk {

    internal var callback: FinikCallback? = null

    fun openPaymentScreen(
        context: Context,
        params: FinikParams,
        callback: FinikCallback,
    ) {
        this.callback = callback

        val intent = Intent(context, FinikActivity::class.java).apply {
            putExtra("extra_params", params)

        }
        context.startActivity(intent)
    }

    internal fun clear() {
        callback = null
    }
}