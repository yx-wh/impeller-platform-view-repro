package org.example.impeller_repro

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * A single-top activity that shows whatever text was shared to it, with one
 * native platform view below it.
 */
class MainActivity : FlutterActivity() {
    private var channel: MethodChannel? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        flutterEngine.platformViewsController.registry.registerViewFactory(
            "repro/view",
            LabelViewFactory(),
        )

        val created = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "repro/text",
        )
        // Dart asks for the text once its handler is ready, so nothing is
        // dropped while the engine is still starting.
        created.setMethodCallHandler { call, result ->
            if (call.method == "initialText") {
                result.success(processText())
            } else {
                result.notImplemented()
            }
        }
        channel = created
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        if (text != null) {
            channel?.invokeMethod("text", text)
        }
    }

    private fun processText(): String? =
        intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
}
