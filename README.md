# Finik Android SDK

`finik-android-sdk` — это Android-библиотека, которая позволяет легко интегрировать финтех-интерфейс Finik в ваше приложение. Вся реализация UI и логики выполнена на Flutter, но вам не нужно ничего настраивать — просто вызывайте `FinikActivity`.

## 🔧 Установка

Добавьте зависимость в ваш `build.gradle`:

```groovy
dependencies {
    implementation 'com.yourcompany:finik-android-sdk:1.0.0'
}
```

## ⚙️ Настройка
И добавьте нужные репозитории в `settings.gradle`:

```dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")
    }
}
```

## 🚀 Использование
Всё, что вам нужно — это запустить `FinikActivity` и передать параметры через Intent.

```import android.content.Intent
import finik.android.sdk.FinikActivity

val intent = Intent(this, FinikActivity::class.java).apply {
    putExtra("apiKey", "YOUR_API_KEY")
    putExtra("locale", "ru")              // или "en", "ky"
    putExtra("useHive", false)             // включить/выключить кеширование
}
startActivity(intent)
```

## 📡 Обратная связь от Finik
`FinikActivity` отправляет Broadcast-события:

Broadcast	              Описание
finik_onBackPressed	  Пользователь нажал назад в интерфейсе Finik
finik_paymentSuccess	Оплата прошла успешно. Аргумент data содержит доп. информацию
finik_paymentFailure	Оплата завершилась с ошибкой. Аргумент error содержит сообщение

Пример приёма:
```val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "finik_paymentSuccess" -> {
                val data = intent.getStringExtra("data")
                // Обработка успеха
            }
            "finik_paymentFailure" -> {
                val error = intent.getStringExtra("error")
                // Обработка ошибки
            }
        }
    }
}

// Зарегистрировать при необходимости
registerReceiver(receiver, IntentFilter().apply {
    addAction("finik_paymentSuccess")
    addAction("finik_paymentFailure")
})
```

© 2025 — Finik Team
Все права защищены.

