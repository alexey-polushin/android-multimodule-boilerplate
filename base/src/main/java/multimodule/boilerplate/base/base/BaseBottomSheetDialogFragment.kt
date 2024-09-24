package multimodule.boilerplate.base.base

import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import multimodule.boilerplate.base.di.basicFragmentModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.closestDI

abstract class BaseBottomSheetDialogFragment: BottomSheetDialogFragment(), DIAware {
    final override val di: DI by subDI(closestDI()) {
        importOnce(basicFragmentModule(this@BaseBottomSheetDialogFragment), allowOverride = true)
    }

    fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}