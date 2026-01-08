package kg.finik.android.sdk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinikParams(
    val apiKey: String,
    val widget: FinikWidget,
    val isBeta: Boolean = false,
    val enableShare: Boolean? = null,
    val tapableSupportButtons: Boolean? = null,
    val enableSupportButtons: Boolean? = null,
    val enableShimmer: Boolean? = null,
    val locale: FinikSdkLocale? = null,
    val textScenario: TextScenario? = null,
    val paymentMethods: List<PaymentMethod>? = null
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        val params = mutableMapOf<String, Any?>()
        textScenario?.toMap()?.let { params.putAll(it) }
        paymentMethods?.let { params["paymentMethods"] = it.map { m -> m.name } }
        locale?.toMap()?.let { params.putAll(it) }

        params += mapOf(
            "apiKey" to apiKey,
            "isBeta" to isBeta,
            "enableShare" to enableShare,
            "tapableSupportButtons" to tapableSupportButtons,
            "enableSupportButtons" to enableSupportButtons,
            "enableShimmer" to enableShimmer,
            "widget" to widget.toMap()
        )
        return params
    }
}


@Parcelize
sealed class FinikWidget : Parcelable {
    abstract fun toMap(): Map<String, Any?>
}
