package finik.android.sdk

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kg.finik.android.sdk.ActionLabelType
import kg.finik.android.sdk.CreateItemHandlerWidget
import kg.finik.android.sdk.FinikCallback
import kg.finik.android.sdk.FinikParams
import kg.finik.android.sdk.FinikSdk
import kg.finik.android.sdk.FixedAmount
import kg.finik.android.sdk.GetItemHandlerWidget
import kg.finik.android.sdk.ItemId
import kg.finik.android.sdk.RequiredField
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fun start() {
            FinikSdk.openPaymentScreen(
                context = this,
                params = FinikParams(
                    apiKey = "YOUR_API_KEY",
                    isBeta = true,
                    widget =
//                        GetItemHandlerWidget(parameter = ItemId("YOUR_ITEM_ID")),
                        CreateItemHandlerWidget(
                            // Обязательное поле. ID счёт бенефициара.
                            accountId = "YOUR_ACCOUNT_ID",

                            // Обязательное поле. Название товара или услуги.
                            name = "Кроссовки",

                            // (необязательно): Краткое описание товара. Отображается в интерфейсе оплаты.
                            description = "Интернет-магазин спортивной одежды предлагает широкий ассортимент мужской и женской одежды и аксессуаров для спорта. Футболки, толстовки, рубашки, спортивные костюмы и кроссовки можно купить с доставкой в любой город – курьерской службой.",

                            // Необязательное поле. По умолчанию сумма становится динамичной, не фиксированной,
                            // и обязательной для ввода покупателем вручную при оплате.
                            amount = FixedAmount(value = 51.3),

                            // (необязательно): Дата и время, с которых товар становится доступен для оплаты.
                            startDate = Calendar.getInstance(),

                            // (необязательно): Дата и время, после которых товар больше недоступен для оплаты.
                            endDate = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_MONTH, 1) // tomorrow
                            },

                            // (необязательно): Максимальное количество раз, которое этот товар может быть приобретён. Предотвращает перепродажу.
                            maxAvailableQuantity = 1,

                            // (необязательно): Максимальная общая сумма, разрешённая для оплаты по всем покупкам этого товара.
                            maxAvailableAmount = 100000.0,

                            // Необязательное поле. Список ключей и их значений, которые вы хотите получить
                            // обратно на указанный `callbackUrl`.
                            requiredFields = listOf(
                                RequiredField(
                                    fieldId = "orderId", value = "123"
                                )
                            ),

                            // Необязательное поле. Webhook URL, на который вы хотели бы получить сообщение
                            // от Finik в ответ на успешный платёж.
                            callbackUrl = "https:/my/callback/url.kg",
                            actionLabelType = ActionLabelType.REGISTER,
                            mcc = "1234",
                        ),
                ),
                callback = object : FinikCallback {
                    override fun onPaymentSuccess(data: Map<String, Any?>) {
                        Log.d("MainActivity", "Payment result: $data")

                        // Payment result example:
                        // {
                        //     "amount": 51.3,                                 // сумма платежа
                        //     "status": "SUCCEEDED"                           // статус платежа
                        //     "fields": {                                     // дополнительные поля которые вы передали в requiredFields
                        //         "transactionType": "20",
                        //         "amount": 51.3,
                        //         "orderId": "123",
                        //         "qrComment": "",
                        //         "name": "Кроссовки",
                        //         "url": "https://qr.finik.kg/#00020101021232810011qr.finik.kg0114averspay-items1032989e4c5623344b6b9869ef8c64e67e33120212130212520448295303417540451305908Finik-QR6304f11d",
                        //         "transactionId": "197cd2cd-ddd0-4e10-a9ed-39ab0f092d5c"
                        //     },
                        //     "accountId": "YOUR_ACCOUNT_ID",
                        //     "transactionDate": 1756391111658,              // дата транзакции в миллисекундах
                        //     "transactionType": "DEBIT",                    // тип транзакции
                        //     "receiptNumber": "213933032616",               // номер чека
                        //     "item": {
                        //         "id": "1743008977_ac74f101-6e79-4b5a-b654-637cca0e1a39"
                        //     },
                        //     "clientId": "79ba7a09-3365-4c5a-b0b4-10905e368698",
                        //     "deviceId": "12b7c530-b408-445e-bb50-b4c317dfc057",
                        //     "transactionId": "197cd2cd-ddd0-4e10-a9ed-39ab0f092d5c",
                        //     "requestDate": 1756391110897,
                        //     "id": "3601266827_197cd2cd-ddd0-4e10-a9ed-39ab0f092d5c_DEBIT",
                        // }
                    }

                    override fun onCreated(data: Map<String, Any?>) {
                        Log.d("MainActivity", "QR created with id: ${data["id"]}")
                    }

                    override fun onSdkPopped() {
                        Log.d("MainActivity", "Payment screen popped")
                    }
                }

            )
        }


        val button = Button(this).apply {
            text = "Открыть Finik SDK"
            setOnClickListener {
                // Запуск FinikActivity с SDK
                start()
            }
        }

        setContentView(button)
    }
}
