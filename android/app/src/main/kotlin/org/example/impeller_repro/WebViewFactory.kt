package org.example.impeller_repro

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

/**
 * A plain native view. Deliberately not a WebView: the platform view itself is
 * all that matters here.
 */
class LabelHost(context: Context) : PlatformView {
    private val view = TextView(context).apply {
        text = "native platform view"
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        setTextColor(Color.DKGRAY)
        setBackgroundColor(0xFFEEEEEE.toInt())
        setPadding(32, 64, 32, 64)
    }

    override fun getView(): View = view

    override fun dispose() {}
}

class LabelViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView = LabelHost(context)
}
