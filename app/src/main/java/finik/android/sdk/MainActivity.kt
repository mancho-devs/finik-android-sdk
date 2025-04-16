package finik.android.sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kg.mancho.finik_android_sdk.FinikActivity

class MainActivity : AppCompatActivity() {

    private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val resultValue = data?.getStringExtra("paymentResult")
                val details = data?.getStringExtra("details")
                // Обработка success
            } else {
                val isBackPressed = result.data?.getStringExtra("isBackPressed") == "true"

                if (isBackPressed) {
                    Log.d("MainActivity", "Пользователь вышел из Finik по кнопке назад")
                    // Обработка кнопки назад
                } else {
                    val data = result.data
                    val resultValue = data?.getStringExtra("paymentResult")
                    val details = data?.getStringExtra("details")
                    // Обработка failure
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск FinikActivity из твоей SDK
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "da2-qtfmf4xkzjeypiexb75aqxtn6u")
            putExtra("locale", "ru")
            putExtra("useHive", false)
        }

        finikLauncher.launch(intent)
    }
}
