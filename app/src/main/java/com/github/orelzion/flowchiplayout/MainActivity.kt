package com.github.orelzion.flowchiplayout

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import com.github.orelzion.flowchiplayout.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

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
            val chip = createChip(withText = text.substringBeforeLast(","))
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