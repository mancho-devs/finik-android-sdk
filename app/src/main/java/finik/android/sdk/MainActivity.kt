package finik.android.sdk

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kg.finik.android.sdk.CreateItemHandlerWidget
import kg.finik.android.sdk.FinikActivity
import kg.finik.android.sdk.FinikSdkLocale
import kg.finik.android.sdk.RequiredField

class MainActivity : AppCompatActivity() {

    private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val paymentResultJson = data?.getStringExtra("paymentResultJson")
                // Обработка success или failure
            } else {
                Log.d("MainActivity", "Пользователь вышел из Finik по кнопке назад")
                // Обработка кнопки назад
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск FinikActivity с SDK
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "YOUR_API_KEY")
            putExtra(
                "widget",
//                GetItemHandlerWidget(itemId = "YOUR_ITEM_ID")
                CreateItemHandlerWidget(
                    accountId = "YOUR_ACCOUNT_ID",
//                    YOUR_ITEM_NAME
                    name = "Кроссовки",
                    fixedAmount = 1200.0,
//                    callbackUrl = TODO(),
//                    textScenario = TextScenario.REPLENISHMENT,
                    requiredFields = listOf(RequiredField(fieldId = "orderId", value = "123"))
                )
            )
            putExtra("locale", FinikSdkLocale.RU as Parcelable)
            putExtra("isBeta", true)
//            putExtra("textScenario", TextScenario.REPLENISHMENT as Parcelable)
//            putExtra("useCacheForData", true)
        }

        finikLauncher.launch(intent)
    }
}
