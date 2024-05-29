package multimodule.boilerplate.core.resources

import android.content.res.Configuration
import android.content.res.Resources

fun Resources.isNightMode(): Boolean =
    this.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES