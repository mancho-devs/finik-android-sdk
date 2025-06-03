import 'package:flutter/services.dart';
import 'package:finik_sdk/finik_sdk.dart';

class FinikProviderScreen extends StatefulWidget {
  const FinikProviderScreen({super.key});

  @override
  State<FinikProviderScreen> createState() => _FinikProviderScreenState();
}

class _FinikProviderScreenState extends State<FinikProviderScreen> {
  static const _channel = MethodChannel('finik_sdk_channel');

  String? apiKey;
  String? itemId;
  String? locale;
  bool? useHive;

  @override
  void initState() {
    super.initState();
    _loadParamsFromAndroid();
  }

  Future<void> _loadParamsFromAndroid() async {
    final result = await _channel.invokeMethod<Map>('getFinikParams');
    setState(() {
      apiKey = result?['apiKey'];
      itemId = result?['itemId'];
      locale = result?['locale'];
      useHive = result?['useHive'];

    });
  }

  void _onBackPressed() {
    _channel.invokeMethod('onBackPressed');
  }

  void _onPaymentSuccess(Map? data) {
    _channel.invokeMethod('onPaymentSuccess', data);
  }

  void _onPaymentFailure(String message) {
    _channel.invokeMethod('onPaymentFailure', message);
  }

  @override
  Widget build(BuildContext context) {
    if (apiKey == null || locale == null) {
      return const Center(child: CircularProgressIndicator());
    }

    return Scaffold(
      body: FinikProvider(
        apiKey: apiKey!,
        locale: locale!,
        useHiveForGraphQLCache: useHive ?? false,
        onBackPressed: _onBackPressed,
        onPaymentSuccess: _onPaymentSuccess,
        onPaymentFailure: _onPaymentFailure,
        widget: GetItemHandlerWidget(
          itemId: itemId!,
          textScenario: TextScenario.payment,
        ), // или любой другой кастом
      ),
    );
  }
}
