import 'package:flutter/material.dart';
import 'package:flutter_library/finik_provider_screen.dart';


void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    const MaterialApp(
      home: FinikProviderScreen(),
    ),
  );
}

