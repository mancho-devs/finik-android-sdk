package finik.android.sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class FinikActivity : FlutterActivity() {

    private val channelName = "finik_channel"

    private lateinit var apiKey: String
    private var locale: String = "ru"
    private var useHive: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем параметры из Intent
        intent?.let {
            apiKey = it.getStringExtra("apiKey") ?: ""
            locale = it.getStringExtra("locale") ?: "ru"
            useHive = it.getBooleanExtra("useHive", true)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName).setMethodCallHandler { call, result ->
            when (call.method) {
                "getFinikParams" -> {
                    val params = mapOf(
                        "apiKey" to apiKey,
                        "locale" to locale,
                        "useHiveForGraphQLCache" to useHive
                    )
                    result.success(params)
                }

                "onBackPressed" -> {
                    Log.d("FinikSDK", "Back button pressed in FinikProvider")
                    sendBroadcast("finik_onBackPressed")
                }

                "onPaymentSuccess" -> {
                    Log.d("FinikSDK", "Payment success: ${call.arguments}")
                    sendBroadcast("finik_paymentSuccess", "data" to (call.arguments as? String ?: ""))
                }

                "onPaymentFailure" -> {
                    Log.e("FinikSDK", "Payment failed: ${call.arguments}")
                    sendBroadcast("finik_paymentFailure", "error" to (call.arguments as? String ?: ""))
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun sendBroadcast(action: String, vararg extras: Pair<String, String>) {
        val intent = Intent(action)
        extras.forEach { intent.putExtra(it.first, it.second) }
        sendBroadcast(intent) // Используем стандартный sendBroadcast
    }
}
