package finik.android.sdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // private val paymentReceiver = object : BroadcastReceiver() {
    //     override fun onReceive(context: Context?, intent: Intent?) {
    //         val action = intent?.action ?: return
    //         when (action) {
    //             "finik_paymentSuccess" -> {
    //                 val data = intent.getStringExtra("data") ?: ""
    //                 Log.d("MainActivity", "Payment success: $data")
    //             }
    //             "finik_paymentFailure" -> {
    //                 val error = intent.getStringExtra("error") ?: ""
    //                 Log.e("MainActivity", "Payment failed: $error")
    //             }
    //             "finik_onBackPressed" -> {
    //                 Log.d("MainActivity", "FinikActivity закрыта")
    //             }
    //         }
    //     }
    // }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Регистрируем приёмник
        // registerReceiver(paymentReceiver, IntentFilter().apply {
        //     addAction("finik_paymentSuccess")
        //     addAction("finik_paymentFailure")
        //     addAction("finik_onBackPressed")
        // })

        // Запуск FinikActivity из твоей SDK
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "da2-qtfmf4xkzjeypiexb75aqxtn6u")
            putExtra("locale", "ru")
            putExtra("useHive", true)
        }

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregisterReceiver(paymentReceiver)
    }
}
