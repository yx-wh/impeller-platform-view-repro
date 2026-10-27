import "package:flutter/material.dart";
import "package:flutter/services.dart";
import "package:flutter_inappwebview/flutter_inappwebview.dart";

void main() => runApp(const ReproApp());

class ReproApp extends StatelessWidget {
  const ReproApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(colorSchemeSeed: Colors.indigo),
      home: const TextReceiver(),
    );
  }
}

/// Shows the text other apps shared to this one, above a WebView configured the
/// way the real app configures it: hybrid composition (`useHybridComposition`)
/// through flutter_inappwebview.
///
/// The counter is the only state that matters: when the window goes black the
/// app is still alive, and a black screen cannot tell that apart from a crash.
class TextReceiver extends StatefulWidget {
  const TextReceiver({super.key});

  @override
  State<TextReceiver> createState() => _TextReceiverState();
}

class _TextReceiverState extends State<TextReceiver> {
  static const _channel = MethodChannel("repro/text");

  String? _text;
  var _received = 0;

  @override
  void initState() {
    super.initState();

    _channel.setMethodCallHandler((call) async {
      if (call.method == "text") _show(call.arguments as String);
    });
    _loadInitialText();
  }

  Future<void> _loadInitialText() async {
    final text = await _channel.invokeMethod<String>("initialText");
    if (text != null) _show(text);
  }

  void _show(String text) {
    if (!mounted) return;
    setState(() {
      _text = text;
      _received++;
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      appBar: AppBar(title: const Text("Impeller repro")),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 24),
            child: Column(
              children: [
                Text("$_received", style: theme.textTheme.displayMedium),
                Text("text received", style: theme.textTheme.labelMedium),
                const SizedBox(height: 12),
                Text(
                  _text ?? "(no text yet — share some text to this app)",
                  textAlign: TextAlign.center,
                  maxLines: 3,
                  overflow: TextOverflow.ellipsis,
                  style: theme.textTheme.titleMedium,
                ),
                const SizedBox(height: 16),
                // Keeps a frame in flight at all times.
                const LinearProgressIndicator(),
              ],
            ),
          ),
          const Divider(height: 1),
          Expanded(
            child: InAppWebView(
              initialData: InAppWebViewInitialData(
                data: """
<html><head><meta name="viewport" content="width=device-width"></head>
<body style="font-family:sans-serif;padding:16px;color:#222;background:#fff">
  <h1>entry</h1>
  <p>${"A dictionary entry rendered by a WebView. " * 40}</p>
</body></html>""",
                baseUrl: WebUri("http://ciyue.internal/"),
              ),
              initialSettings: InAppWebViewSettings(
                useWideViewPort: false,
                transparentBackground: true,
                useHybridComposition: true,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
