# Finik Android SDK

[kg.finik:android-sdk](https://central.sonatype.com/artifact/kg.finik/android-sdk) — это Android-библиотека, которая позволяет легко интегрировать финтех-интерфейс Finik в ваше приложение. Вся реализация UI и логики выполнена на Flutter, но вам не нужно ничего настраивать — просто вызывайте `FinikActivity`.

## 🔧 Установка

Добавьте зависимость в ваш `build.gradle`:

```groovy
dependencies {
    implementation 'kg.finik:android-sdk:1.1.1'
}
```

## ⚙️ Настройка

И добавьте нужные репозитории в `settings.gradle`:
`maven("https://storage.googleapis.com/download.flutter.io")` обязательно, так как наш SDK зависеть от Flutter [библиотеки](https://pub.dev/packages/finik_sdk)

```dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")
    }
}
```

## 🚀 Использование

Всё, что вам нужно — это запустить `FinikActivity` через `registerForActivityResult` и передать параметры через Intent.

```
import android.content.Intent
import kg.finik.android.sdk.CreateItemHandlerWidget
import kg.finik.android.sdk.FinikActivity
import kg.finik.android.sdk.FinikSdkLocale
import kg.finik.android.sdk.PaymentMethod
import kg.finik.android.sdk.RequiredField
import kg.finik.android.sdk.TextScenario

private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск FinikActivity из твоей SDK
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "YOUR_API_KEY")
            putExtra(
                "widget",
//                GetItemHandlerWidget(itemId = "YOUR_ITEM_ID")
                CreateItemHandlerWidget(
                    accountId = "72145c2f-b987-46b9-b718-5d8313854f69",
                    name = "Кроссовки", // YOUR_ITEM_NAME
//                    fixedAmount = 2300.0, - not required
//                    callbackUrl = TODO(), - not required
//                    textScenario = TextScenario.REPLENISHMENT, - not required
//                    requiredFields = listOf(RequiredField(fieldId = "orderId", value = "123")),  - not required
                )
            )
            putExtra("locale", FinikSdkLocale.RU as Parcelable)
            putExtra("isBeta", true)
        }

        finikLauncher.launch(intent)
    }
```

## 📡 Обратная связь от Finik

`FinikActivity` возвращает `Activity.RESULT_OK` или `Activity.RESULT_CANCELED`:

Name Описание
RESULT_OK Оплата прошла успешно либо завершилась с ошибкой. Аргумент data содержит параметр `paymentResultJson`
RESULT_CANCELED Пользователь нажал назад в интерфейсе Finik, Аргумент data содержит параметр `isBackPressed`

Пример приёма:

```
private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val paymentResultJson = data?.getStringExtra("paymentResultJson")
                Log.d("MainActivity", "Payment result: $paymentResultJson")
                // Обработка success или failure
            } else {
                Log.d("MainActivity", "Пользователь вышел из Finik по кнопке назад")
                // Обработка кнопки назад
            }
        }
```

Пример `Activity` можно найти [здесь](app/src/main/java/finik/android/sdk/MainActivity.kt)

© 2025 — Finik Team
Все права защищены.
