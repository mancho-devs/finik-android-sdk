package kg.finik.android.sdk

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class FinikActivity : FlutterActivity() {

    private val channelName = "finik_sdk_channel"

    private lateinit var apiKey: String
    private lateinit var widget: FinikWidget
    private var isBeta: Boolean? = null
    private var locale: FinikSdkLocale? = null
    private var textScenario: TextScenario? = null
    private var paymentMethod: PaymentMethod? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем параметры из Intent
        intent?.let {
            apiKey = it.getStringExtra("apiKey") ?: ""
            widget = it.getParcelableExtra("widget", FinikWidget::class.java)!!
            isBeta = it.getBooleanExtra("isBeta", false)
            locale = it.getParcelableExtra("locale", FinikSdkLocale::class.java)
            textScenario = it.getParcelableExtra("textScenario", TextScenario::class.java)
            paymentMethod = it.getParcelableExtra("paymentMethod", PaymentMethod::class.java)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channelName
        )

        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getFinikSdkParams" -> {
                    val params = mutableMapOf<String, Any?>()

                    textScenario?.toMap()?.let { params.putAll(it) }
                    paymentMethod?.toMap()?.let { params.putAll(it) }
                    locale?.toMap()?.let { params.putAll(it) }

                    params += mapOf(
                        "apiKey" to apiKey,
                        "isBeta" to isBeta,
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
    }
}
