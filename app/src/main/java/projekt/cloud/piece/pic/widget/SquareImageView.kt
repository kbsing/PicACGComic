package projekt.cloud.piece.pic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import com.google.android.material.imageview.ShapeableImageView

class SquareImageView(context: Context, attributeSet: AttributeSet?): ShapeableImageView(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)

        if (widthSpecMode == UNSPECIFIED && MeasureSpec.getMode(heightMeasureSpec) == UNSPECIFIED) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

        val measureSpec = MeasureSpec.makeMeasureSpec(
            when (widthSpecMode) {
                UNSPECIFIED -> MeasureSpec.getSize(heightMeasureSpec)
                else -> MeasureSpec.getSize(widthMeasureSpec)
            },
            EXACTLY
        )

        setMeasuredDimension(measureSpec, measureSpec)
    }

}