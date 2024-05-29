package multimodule.boilerplate.core.bitmap

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.convertBitmapToBase64(): String {
    val outputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

fun Bitmap.scaleAndRotate(scale: Float, rotate: Int): Bitmap {
    val m = Matrix()
    m.postScale(scale, scale)
    if (rotate != 0) {
        m.postRotate(rotate.toFloat())
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, m, true)
}