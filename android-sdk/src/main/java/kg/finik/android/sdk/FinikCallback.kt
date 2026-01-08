package kg.finik.android.sdk

interface FinikCallback {
    fun onPaymentSuccess(data: Map<String, Any?>)

    fun onCreated(data: Map<String, Any?>) {}

    fun onSdkPopped() {}
}