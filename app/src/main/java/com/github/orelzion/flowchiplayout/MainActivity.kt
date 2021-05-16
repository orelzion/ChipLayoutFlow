package com.github.orelzion.flowchiplayout

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import com.github.orelzion.flowchiplayout.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EMAIL_REGEX = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        configureView(viewBinding)
    }

    private fun configureView(viewBinding: ActivityMainBinding) {
        viewBinding.emailInput.doOnTextChanged { text, start, before, count ->
            text?.let { afterTextChanged(viewBinding, text.toString()) }
        }
    }

    private fun afterTextChanged(viewBinding: ActivityMainBinding, text: String) {
        if (text.length > 1 && text.endsWith(",")) {
            val emailInput = text.substringBeforeLast(",")

            if(!emailInput.matches(EMAIL_REGEX.toRegex())) {
                Toast.makeText(this, R.string.email_input_error, Toast.LENGTH_SHORT).show()
                viewBinding.emailInput.text = null
                return
            }

            val chip = createChip(withText = emailInput)
            viewBinding.root.addView(chip)

            val updatedArray = viewBinding.mailsLayout.referencedIds.run {
                 this
                    .toMutableList()
                    .apply {
                        add(this.size - 1, chip.id)
                    }.toIntArray()
            }
            viewBinding.mailsLayout.referencedIds = updatedArray
            viewBinding.emailInput.text = null
        }
    }

    private fun createChip(withText: String): Chip {
        return Chip(this).apply {
            text = withText
            id = ViewCompat.generateViewId()
        }
    }
}