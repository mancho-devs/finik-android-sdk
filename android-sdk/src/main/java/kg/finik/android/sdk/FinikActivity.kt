package kg.finik.android.sdk

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class FinikActivity : FlutterActivity() {

    private val channelName = "finik_sdk_channel"

    private lateinit var apiKey: String
    private lateinit var widget: FinikWidget
    private var isBeta: Boolean? = null
    private var enableShare: Boolean? = null
    private var tapableSupportButtons: Boolean? = null
    private var enableAnimation: Boolean? = null
    private var locale: FinikSdkLocale? = null
    private var textScenario: TextScenario? = null
    private var paymentMethods: Array<PaymentMethod>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем параметры из Intent
        intent?.let {
            apiKey = it.getStringExtra("apiKey") ?: ""
            isBeta = it.getBooleanExtra("isBeta", false)
            enableShare = it.getBooleanExtra("enableShare", true)
            tapableSupportButtons = it.getBooleanExtra("tapableSupportButtons", true)
            enableAnimation = it.getBooleanExtra("enableAnimation", true)
            widget = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("widget", FinikWidget::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("widget")
            } ?: error("widget is required")

            locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("locale", FinikSdkLocale::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("locale")
            }

            textScenario = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("textScenario", TextScenario::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("textScenario")
            }

            paymentMethods = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(
                    "paymentMethods",
                    PaymentMethod::class.java
                )?.filterIsInstance<PaymentMethod>()?.toTypedArray()
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayExtra("paymentMethods")?.filterIsInstance<PaymentMethod>()?.toTypedArray()
            }
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        val channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channelName
        )

        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getFinikSdkParams" -> {
                    val params = mutableMapOf<String, Any?>()

                    textScenario?.toMap()?.let { params.putAll(it) }
                    paymentMethods?.let { params["paymentMethods"] = it.map { it.name } }
                    locale?.toMap()?.let { params.putAll(it) }

                    params += mapOf(
                        "apiKey" to apiKey,
                        "isBeta" to isBeta,
                        "enableShare" to enableShare,
                        "tapableSupportButtons" to tapableSupportButtons,
                        "enableAnimation" to enableAnimation,
                        "widget" to widget.toMap()
                    )

                    result.success(params)
                }

                "onBackPressed" -> {
                    Log.d("FinikSDK", "Back button pressed in FinikProvider")
                    setResult(RESULT_CANCELED)
                    finish()
                }

                "onPayment" -> {
                    Log.d("FinikSDK", "Payment result: ${call.arguments}")
                    val jsonString = JSONObject(call.arguments as Map<*, *>).toString()

                    val resultIntent = Intent().apply {
                        putExtra("paymentResultJson", jsonString)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                else -> {
                    result.notImplemented()
                }
            }
        }

        super.configureFlutterEngine(flutterEngine)
    }
}
