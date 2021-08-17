package com.ebolo.krichtexteditor.ui.widgets

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bitbucket.eventbus.EventBus
import com.ebolo.krichtexteditor.R
import com.ebolo.krichtexteditor.RichEditor
import org.jetbrains.anko.runOnUiThread

class EditorToolbar(private val editor: RichEditor, private val buttonsLayout: List<Int>) {
    var linkButtonAction: (() -> Unit)? = null
    var imageButtonAction: (() -> Unit)? = null

    private lateinit var buttons: Map<Int, ImageView>

    var buttonActivatedColorId: Int = R.color.colorAccent
    var buttonDeactivatedColorId: Int = R.color.tintColor

    fun createToolbar(container: LinearLayout) {
        fun createButton(@EditorButton.Companion.ActionType actionType: Int): ImageView {
            val buttonLayout = LayoutInflater.from(container.context).inflate(
                R.layout.editor_toolbar_button,
                container,
                false
            )

            val imageView = buttonLayout.findViewById<AppCompatImageView>(R.id.image_view)
            imageView.apply {
                setImageResource(EditorButton.actionButtonDrawables[actionType]!!)
                setOnClickListener {
                    when (actionType) {
                        EditorButton.IMAGE -> imageButtonAction?.invoke()

                        EditorButton.LINK -> linkButtonAction?.invoke()

                        else -> editor.command(actionType)
                    }
                }
            }

            container.addView(buttonLayout)

            return imageView
        }
        buttons = buttonsLayout.associateWith { createButton(it) }
    }

    fun setupListeners(context: Context) {
        val eventBus = EventBus.getInstance()
        buttonsLayout.forEach { buttonId ->
            eventBus.on("style", "style_$buttonId") {
                val state = it as Boolean
                context.runOnUiThread {
                    buttons[buttonId]?.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            when {
                                state -> buttonActivatedColorId
                                else -> buttonDeactivatedColorId
                            }
                        )
                    )
                }
            }
        }
    }
}