package com.ebolo.krichtexteditor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ebolo.krichtexteditor.R
import com.ebolo.krichtexteditor.RichEditor
import com.ebolo.krichtexteditor.ui.layouts.KRichEditorFragmentLayout
import com.ebolo.krichtexteditor.ui.layouts.KRichEditorViewLayout
import com.ebolo.krichtexteditor.ui.widgets.EditorButton
import org.jetbrains.anko.AnkoContext

class KRichEditorView: Fragment() {
    private val layout by lazy { KRichEditorViewLayout() }
    val editor = RichEditor()
    var settings: ((KRichEditorViewLayout).() -> Unit)? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        layout.apply { settings?.invoke(this) }
        return layout.createView(AnkoContext.create(requireContext(), this))
    }

    companion object {
        @JvmStatic fun getInstance(options: Options) = KRichEditorView().apply {
            this.layout.apply {
                placeHolder = options.placeHolder
                imageButtonAction = options.imageButtonAction
                buttonsLayout = options.buttonsLayout
                buttonActivatedColorId = options.buttonActivatedColorId
                buttonDeactivatedColorId = options.buttonDeactivatedColorId
                showToolbar = options.showToolbar
                readOnly = options.readOnly
                onInitialized = options.onInitialized
            }
        }
    }
}

fun kRichEditorView(
        settings: ((KRichEditorViewLayout).() -> Unit)? = null
): KRichEditorView = KRichEditorView().apply {
    this.settings = settings
}