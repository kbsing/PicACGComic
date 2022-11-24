package projekt.cloud.piece.pic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import com.google.android.material.imageview.ShapeableImageView
import projekt.cloud.piece.pic.R

class RatioImageView(context: Context, attributeSet: AttributeSet?): ShapeableImageView(context, attributeSet) {

    private val ratioX: Int
    private val ratioY: Int
    
    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.RatioImageView).use {
            ratioX = it.getInt(R.styleable.RatioImageView_ratioX, 1)
            ratioY = it.getInt(R.styleable.RatioImageView_ratioY, 1)
        }
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        
        if (widthSpecMode == UNSPECIFIED && heightSpecMode == UNSPECIFIED) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        
        val measureSpecWidth: Int
        val measureSpecHeight: Int
        
        when {
            widthSpecMode != UNSPECIFIED && heightSpecMode == UNSPECIFIED -> {
                measureSpecWidth = widthMeasureSpec
                measureSpecHeight = MeasureSpec.makeMeasureSpec(
                    getScaledSize(MeasureSpec.getSize(widthMeasureSpec), ratioY, ratioX),
                    EXACTLY
                )
            }
            else -> {
                measureSpecHeight = heightMeasureSpec
                measureSpecWidth = MeasureSpec.makeMeasureSpec(
                    getScaledSize(MeasureSpec.getSize(heightMeasureSpec), ratioX, ratioY),
                    EXACTLY
                )
            }
        }
        
        setMeasuredDimension(measureSpecWidth, measureSpecHeight)
    }
    
    private fun getScaledSize(size: Int, multiplicand: Int, dividend: Int) =
        size * multiplicand / dividend

}