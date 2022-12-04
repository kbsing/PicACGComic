package projekt.cloud.piece.pic.ui.read.content

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView

class ComicContentImageView(context: Context, attributeSet: AttributeSet?): ShapeableImageView(context, attributeSet) {
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bitmap = (drawable as? BitmapDrawable)?.bitmap
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED || bitmap == null) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        setMeasuredDimension(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                bitmap.height * MeasureSpec.getSize(widthMeasureSpec) / bitmap.width,
                MeasureSpec.EXACTLY
            )
        )
    }
    
}