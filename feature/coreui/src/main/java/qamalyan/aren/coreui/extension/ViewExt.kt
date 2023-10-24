package qamalyan.aren.coreui.extension


import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import qamalyan.aren.coreui.utils.KeyboardEventHandler


fun View.getColor(@ColorRes resId: Int) = context.getColorCompat(resId)

fun View.getDrawable(@DrawableRes resId: Int) = context.getDrawableCompat(resId)


fun View.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = this }
        top?.run { topMargin = this }
        right?.run { rightMargin = this }
        bottom?.run { bottomMargin = this }
    }
}

fun View.margin(all: Int) {
    margin(left = all, top = all, right = all, bottom = all)
}

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun View.addSystemPadding(
    targetView: View = this,
    isTop: Boolean = true,
    isBottom: Boolean = true,
    isIncludeIme: Boolean = true,
) {
    doOnApplyWindowInsets { _, windowInsets, initialPadding ->
        val mask =
            if (isIncludeIme) WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            else
                WindowInsetsCompat.Type.systemBars()

        val isKeyboardVisible = ViewCompat.getRootWindowInsets(targetView)
            ?.isVisible(WindowInsetsCompat.Type.ime()) == true

        KeyboardEventHandler.keyboardCallback(isKeyboardVisible)
        val insets = windowInsets.getInsets(mask)
        targetView.updatePadding(
            top = initialPadding.top + if (isTop) insets.top else 0,
            bottom = initialPadding.bottom + if (isBottom || isKeyboardVisible) insets.bottom else 0,
        )
    }
}

fun View.doOnApplyWindowInsets(block: (View, insets: WindowInsetsCompat, initialPadding: Rect) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
        WindowInsetsCompat.CONSUMED
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }

    }

    requestFocus()
    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}