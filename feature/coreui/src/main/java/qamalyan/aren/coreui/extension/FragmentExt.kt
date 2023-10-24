package qamalyan.aren.coreui.extension

import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun <T> Fragment.collectWhenStarted(flow: Flow<T>, block: suspend (value: T) -> Unit) =
    flow.flowWithLifecycle(lifecycle)
        .onEach(block)
        .launchIn(viewLifecycleScope)

fun <T> Fragment.collectLatestWhenStarted(flow: Flow<T>, block: suspend (value: T) -> Unit) {
    viewLifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle).collectLatest { block(it) }
    }
}

val Fragment.viewLifecycleScope: CoroutineScope get() = viewLifecycleOwner.lifecycleScope

fun Fragment.getColor(@ColorRes resId: Int) = requireContext().getColorCompat(resId)

fun Fragment.getDrawable(@DrawableRes resId: Int) = requireContext().getDrawableCompat(resId)


fun Fragment.showToast(message: CharSequence) = requireContext().showToast(message)

fun Fragment.openLink(link: String) = requireContext().openLink(link)


fun Fragment.hideKeyboard() {
    //TODO: check is keyboard visible, so hide
    view?.let {
        ContextCompat.getSystemService(it.context, InputMethodManager::class.java)
            ?.hideSoftInputFromWindow(it.applicationWindowToken, 0)
    }
}