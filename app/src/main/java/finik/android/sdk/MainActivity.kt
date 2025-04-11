package finik.android.sdk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск FinikActivity из твоей библиотеки
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "da2-qtfmf4xkzjeypiexb75aqxtn6u")
            putExtra("locale", "ru")
            putExtra("useHive", true)
        }

        startActivity(intent)
    }
}
