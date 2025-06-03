package kg.finik.android.sdk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class FinikWidget: Parcelable {
    abstract fun toMap(): Map<String, Any?>
}

data class GetItemHandlerWidget(
    val itemId: String,
) : FinikWidget() {
     override fun toMap(): Map<String, Any?> {
        return mapOf(
            "type" to "getItem",
            "itemId" to itemId
        )
    }
}

data class CreateItemHandlerWidget(
    val accountId: String,
    val requestId: String? = null,
    val name: String,
    val callbackUrl: String? = null,
    val fixedAmount: Double? = null,
    val maxAvailableQuantity: Int? = null,
    val requiredFields: List<RequiredField>? = null
) : FinikWidget() {
    override fun toMap(): Map<String, Any?> {
        val baseMap = mapOf(
            "type" to "createItem",
            "accountId" to accountId,
            "requestId" to requestId,
            "nameEn" to name,
            "callbackUrl" to callbackUrl,
            "fixedAmount" to fixedAmount,
            "maxAvailableQuantity" to maxAvailableQuantity
        )
        return if (requiredFields != null) {
            baseMap + ("requiredFields" to requiredFields.map { it.toMap() })
        } else {
            baseMap
        }
    }
}

@Parcelize
data class RequiredField(
    val fieldId: String,
    val value: String
): Parcelable {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "fieldId" to fieldId,
            "value" to value
        )
    }
}

@Parcelize
enum class TextScenario(val rawValue: String): Parcelable {
    PAYMENT("payment"),
    REPLENISHMENT("replenishment");

    open fun toMap(): Map<String, Any?> {
        return mapOf("textScenario" to this.rawValue)
    }

    companion object {
        fun fromRawValue(raw: String): TextScenario? = TextScenario.entries.find { it.rawValue == raw }
    }
}

@Parcelize
enum class FinikSdkLocale(val rawValue: String): Parcelable {
    KG("kg"),
    EN("en"),
    RU("ru");

    open fun toMap(): Map<String, Any?> {
        return mapOf("locale" to this.rawValue)
    }

    companion object {
        fun fromRawValue(raw: String): FinikSdkLocale? = FinikSdkLocale.entries.find { it.rawValue == raw }
    }
}
