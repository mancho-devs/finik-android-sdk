# Finik Android SDK

[kg.finik:android-sdk](https://central.sonatype.com/artifact/kg.finik/android-sdk) — это Android-библиотека, которая позволяет легко интегрировать финтех-интерфейс Finik в ваше приложение посредством `FinikActivity`.

## 🔧 Установка

Добавьте зависимость в ваш `build.gradle`:

```groovy
dependencies {
    implementation 'kg.finik:android-sdk:1.1.1'
}
```

## ⚙️ Настройка

Добавьте нужные репозитории в `settings.gradle`.

```dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")
    }
}
```

Flutter зависимость необходима, так как Finik Android SDK основан на библиотеке [Finik Flutter SDK](https://pub.dev/packages/finik_sdk).

## 🚀 Использование

Запустите `FinikActivity` через `registerForActivityResult` и передайте параметры через Intent.

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

        // Запуск FinikActivity
        val intent = Intent(this, FinikActivity::class.java).apply {
            // Обязательное поле.
            putExtra("apiKey", "YOUR_API_KEY")
            putExtra(
                "widget",
//                GetItemHandlerWidget(itemId = "YOUR_ITEM_ID")
                CreateItemHandlerWidget(
                    // Обязательное поле. ID счёт бенефициара.
                    accountId = "YOUR_ACCOUNT_ID",
                    // Обязательное поле. Название товара или услуги.
                    name = "Кроссовки", 
                    // Необязательное поле. По умолчанию сумма становится динамичной, не фиксированной,
                    // и обязательной для ввода покупателем вручную при оплате.
                    fixedAmount = 2300.0,
                    // Необязательное поле. Webhook URL, на который вы хотели бы получить сообщение
                    // от Finik в ответ на успешный платёж.
                    callbackUrl = TODO(),
                    // Необязательное поле. Список ключей и их значений, которые вы хотите получить
                    // обратно на указанный `callbackUrl`.
                    requiredFields = listOf(RequiredField(fieldId = "orderId", value = "123")),
                )
            )
//            Необязательное поле. По умолчанию KG
            putExtra("locale", FinikSdkLocale.RU as Parcelable)
//            Необязательное поле. Параметр для теста с beta ключами в beta среде
//            putExtra("isBeta", true)
//            Необязательное поле. Возможность отключать кнопку поделиться.
//            putExtra("enableShare", false)
//            Необязательное поле. Возможность делать не кликабельным кнопки контакты.
//            putExtra("tapableSupportButtons", false)
//            Необязательное поле. Анимация(шиммер) до загрузки данных
//            putExtra("enableShimmer", false)
//            Необязательное поле. Тип оплаты: пополнение или оплата. По умолчанию, оплата.
//            putExtra("textScenario", TextScenario.REPLENISHMENT as Parcelable)
//            Необязательное поле. Список методов пополнение или оплаты
//            val methods = arrayOf(PaymentMethod.QR)
//            putExtra("paymentMethods", methods);
        }

        finikLauncher.launch(intent)
    }
```

## 📡 Обратная связь от Finik

`FinikActivity` возвращает `Activity.RESULT_OK` или `Activity.RESULT_CANCELED`:

|Name | Описание |
|-----|----------|
|RESULT_OK | Оплата прошла успешно, либо завершилась с ошибкой. Аргумент `data` содержит параметр `paymentResultJson` |
|RESULT_CANCELED | Пользователь нажал назад в интерфейсе Finik, Аргумент `data` содержит параметр `isBackPressed` |

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

Пример `Activity` можно найти в [MainActivity.kt](app/src/main/java/finik/android/sdk/MainActivity.kt)

© 2025 Finik
Все права защищены.
