// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:finik_sdk/finik_sdk.dart';
import 'package:flutter_module/finik_provider_screen.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Widget test loading state', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const FinikProviderScreen());

    // Verify that our FinikProvider is painted.
    expect(find.byType(CircularProgressIndicator), findsOneWidget);
  });
}
