package kg.finik.android.sdk

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject

class FinikActivity : FlutterActivity() {

    private val channelName = "finik_sdk_channel"
    private lateinit var params: FinikParams

    override fun onBackPressed() {
        Log.d("FinikSDK", "Back button pressed in FinikSdk")

        super.onBackPressed()
        FinikSdk.callback?.onSdkPopped()
        FinikSdk.clear()
    }

    inline fun <reified T : Parcelable> Intent.getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(key) as? T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем параметры из Intent
        params = intent.getParcelableCompat<FinikParams>("extra_params")
            ?: error("FinikParams is required")
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        val channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            channelName
        )

        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getFinikSdkParams" -> {
                    result.success(params.toMap())
                }

                "onBackPressed" -> {
                    onBackPressed()
                }

                "onPayment" -> {
                    Log.d("FinikSDK", "Payment result: ${call.arguments}")

                    finish()

                    @Suppress("UNCHECKED_CAST")
                    FinikSdk.callback?.onPaymentSuccess(call.arguments as Map<String, Any?>)
                    FinikSdk.clear()
                }

                "onCreated" -> {
                    Log.d("FinikSDK", "Item created: ${call.arguments}")

                    @Suppress("UNCHECKED_CAST")
                    FinikSdk.callback?.onCreated(call.arguments as Map<String, Any?>)
                }

                else -> {
                    result.notImplemented()
                }
            }
        }

        super.configureFlutterEngine(flutterEngine)
    }
}
