package kg.finik.android.sdk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
    val description: String? = null,
    val callbackUrl: String? = null,
    val amount: Amount? = null,
    val maxAvailableQuantity: Int? = null,
    val maxAvailableAmount: Double? = null,
    val requiredFields: List<RequiredField>? = null,
    val visibilityType: VisibilityType? = null,
    val actionLabelType: ActionLabelType? = null,
    val startDate: Calendar? = null,
    val endDate: Calendar? = null,
    val mcc: String? = null,
) : FinikWidget() {
    override fun toMap(): Map<String, Any?> {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);

        val baseMap = mapOf(
            "type" to "createItem",
            "accountId" to accountId,
            "requestId" to requestId,
            "nameEn" to name,
            "description" to description,
            "callbackUrl" to callbackUrl,
            "amount" to amount?.toMap(),
            "maxAvailableQuantity" to maxAvailableQuantity,
            "maxAvailableAmount" to maxAvailableAmount,
            "startDate" to startDate?.let { isoFormatter.format(it.time) },
            "endDate" to endDate?.let { isoFormatter.format(it.time) },
            "visibilityType" to (visibilityType ?: VisibilityType.PRIVATE).rawValue,
            "actionLabelType" to actionLabelType?.rawValue,
            "mcc" to mcc,
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
    PAYMENT("PAYMENT"),
    REPLENISHMENT("REPLENISHMENT");

    open fun toMap(): Map<String, Any?> {
        return mapOf("textScenario" to this.rawValue)
    }

    companion object {
        fun fromRawValue(raw: String): TextScenario? = TextScenario.entries.find { it.rawValue == raw }
    }
}

@Parcelize
enum class FinikSdkLocale(val rawValue: String): Parcelable {
    KG("ky"),
    EN("en"),
    RU("ru");

    open fun toMap(): Map<String, Any?> {
        return mapOf("locale" to this.rawValue)
    }

    companion object {
        fun fromRawValue(raw: String): FinikSdkLocale? = FinikSdkLocale.entries.find { it.rawValue == raw }
    }
}

@Parcelize
enum class PaymentMethod(val rawValue: String): Parcelable {
    APP("APP"),
    QR("QR");

    open fun toMap(): Map<String, Any?> {
        return mapOf("paymentMethod" to this.rawValue)
    }

    companion object {
        fun fromRawValue(raw: String): PaymentMethod? = PaymentMethod.entries.find { it.rawValue == raw }
    }
}

@Parcelize
enum class VisibilityType(val rawValue: String) : Parcelable {
    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    companion object {
        fun fromRawValue(raw: String): VisibilityType? =
            entries.find { it.rawValue == raw }
    }
}

@Parcelize
enum class ActionLabelType(val rawValue: String) : Parcelable {
    PAY("PAY"),
    BUY("BUY"),
    TRANSFER("TRANSFER"),
    REGISTER("REGISTER"),
    JOIN("JOIN"),
    ENROLL("ENROLL"),
    BOOK("BOOK"),
    DONATE("DONATE"),
    TOP_UP("TOP_UP"),
    SEND_KOSHUMCHA("SEND_KOSHUMCHA");

    companion object {
        fun fromRawValue(raw: String): ActionLabelType? =
            entries.find { it.rawValue == raw }
    }
}

@Parcelize
sealed class Amount : Parcelable {
    abstract fun toMap(): Map<String, Any?>

    companion object {
        fun fromMap(map: Map<String, Any?>): Amount? {
            return when (map["type"] as? String) {
                "fixedAmount" -> {
                    val value = (map["fixedAmount"] as? Number)?.toDouble()
                    value?.let { FixedAmount(it) }
                }
                "minMaxAmount" -> {
                    val min = (map["min"] as? Number)?.toDouble()
                    val max = (map["max"] as? Number)?.toDouble()
                    MinMaxAmount(min, max)
                }
                else -> FreeAmount
            }
        }
    }
}

@Parcelize
data class FixedAmount(val value: Double) : Amount(), Parcelable {
    override fun toMap(): Map<String, Any?> = mapOf(
        "type" to "fixedAmount",
        "fixedAmount" to value
    )
}

@Parcelize
data class MinMaxAmount(val min: Double? = null, val max: Double? = null) : Amount(), Parcelable {
    override fun toMap(): Map<String, Any?> = mapOf(
        "type" to "minMaxAmount",
        "min" to min,
        "max" to max
    )
}

@Parcelize
object FreeAmount : Amount(), Parcelable {
    override fun toMap(): Map<String, Any?> = mapOf(
        "type" to "freeAmount"
    )
}
