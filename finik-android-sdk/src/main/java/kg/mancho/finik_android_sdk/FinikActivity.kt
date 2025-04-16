package kg.mancho.finik_android_sdk

import android.app.Activity
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
            useHive = it.getBooleanExtra("useHive", false)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channelName
        ).setMethodCallHandler { call, result ->
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
                    val resultIntent = Intent().apply {
                        putExtra("isBackPressed", "true")
                    }
                    setResult(Activity.RESULT_CANCELED, resultIntent)
                    finish()
                }

                "onPaymentSuccess" -> {
                    Log.d("FinikSDK", "Payment success: ${call.arguments}")
                    val resultIntent = Intent().apply {
                        putExtra("paymentResult", "success")
                        putExtra("details", call.arguments.toString())
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }

                "onPaymentFailure" -> {
                    Log.e("FinikSDK", "Payment failed: ${call.arguments}")
                    val resultIntent = Intent().apply {
                        putExtra("paymentResult", "failure")
                        putExtra("details", call.arguments.toString())
                    }
                    setResult(Activity.RESULT_CANCELED, resultIntent)
                    finish()
                }

                else -> result.notImplemented()
            }
        }
    }
}
