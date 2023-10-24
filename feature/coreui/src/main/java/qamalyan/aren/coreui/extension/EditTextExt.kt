package qamalyan.aren.coreui.extension

import android.widget.EditText

val EditText?.textOrEmpty get() = this?.text.toStringOrEmpty()

fun EditText.updateText(newText: CharSequence) {
    val focused = hasFocus()
    if (focused) clearFocus()
    setText(newText)
    if (focused) requestFocus()
}

fun EditText.updateTextSaveSelection(newText: CharSequence, forceSelection: Int? = null) {
    val currentSelection = selectionEnd
    val isSelectionLast = currentSelection == this.text.toStringOrEmpty().length
    updateText(newText)
    val selection = forceSelection ?: if (isSelectionLast) newText.length else currentSelection
    setSelection(selection)
}