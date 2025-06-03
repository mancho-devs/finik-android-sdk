# Finik Android SDK

`kg.finik:android-sdk` — это Android-библиотека, которая позволяет легко интегрировать финтех-интерфейс Finik в ваше приложение. Вся реализация UI и логики выполнена на Flutter, но вам не нужно ничего настраивать — просто вызывайте `FinikActivity`.

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

```import android.content.Intent
import finik.android.sdk.FinikActivity

private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запуск FinikActivity из твоей SDK
        val intent = Intent(this, FinikActivity::class.java).apply {
            putExtra("apiKey", "YOUR_API_KEY")
            putExtra("itemId", "YOUR_ITEM_ID")
            putExtra("locale", "ru") - not required
            putExtra("useHive", false) - not required
        }

        finikLauncher.launch(intent)
    }
```

## 📡 Обратная связь от Finik

`FinikActivity` возвращает `Activity.RESULT_OK` или `Activity.RESULT_CANCELED`:

Name Описание
RESULT_OK Оплата прошла успешно. Аргумент data содержит параметры `paymentResult` и `details`
RESULT_CANCELED Пользователь нажал назад в интерфейсе Finik, Аргумент data содержит параметр `isBackPressed`
RESULT_CANCELED Оплата завершилась с ошибкой. Аргумент data содержит параметры `paymentResult` и `details`

Пример приёма:

```
private val finikLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Обработка success paymentResult
                val data = result.data
                val resultValue = data?.getStringExtra("paymentResult")
                val details = data?.getStringExtra("details")
            } else {
                val isBackPressed = result.data?.getStringExtra("isBackPressed") == "true"

                if (isBackPressed) {
                    // Обработка кнопки назад
                    Log.d("MainActivity", "Пользователь вышел из Finik по кнопке назад")
                } else {
                    // Обработка failure paymentResult
                    val data = result.data
                    val resultValue = data?.getStringExtra("paymentResult")
                    val details = data?.getStringExtra("details")
                }
            }
        }
```

Пример `Activity` можно найти [здесь](app/src/main/java/finik/android/sdk/MainActivity.kt)

© 2025 — Finik Team
Все права защищены.
