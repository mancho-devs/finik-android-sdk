package kg.finik.android.sdk

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GetItemHandlerWidget(
    val parameter: GetItemParameter,
) : FinikWidget() {
    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to "getItem",
            "parameter" to parameter.toMap()
        )
    }
}

@Parcelize
sealed class GetItemParameter : Parcelable {
    abstract val value: String
    abstract val type: String

    fun toMap(): Map<String, String> {
        return mapOf(
            "type" to type,
            "value" to value
        )
    }
}

data class ItemId(override val value: String) : GetItemParameter() {
    override val type: String get() = "itemId"
}

data class ItemShortUrl(override val value: String) : GetItemParameter() {
    override val type: String get() = "itemShortUrl"
}

data class ItemTransactionId(override val value: String) : GetItemParameter() {
    override val type: String get() = "itemTransactionId"

}