package org.example.impeller_repro

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * The same hop the real app makes: the system opens this invisible activity,
 * which immediately hands the text to [MainActivity] in the existing task.
 */
class ProcessTextActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        if (text.isNullOrEmpty()) {
            finish()
            return
        }

        startActivity(
            Intent(this, MainActivity::class.java).apply {
                action = Intent.ACTION_PROCESS_TEXT
                putExtra(Intent.EXTRA_PROCESS_TEXT, text)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            },
        )
        finish()
    }
}
