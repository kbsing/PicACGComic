package projekt.cloud.piece.pic.ui.read.content

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

class RecyclerGesture: OnItemTouchListener, OnScaleGestureListener {
    
    private var recyclerView: RecyclerView? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (recyclerView == null) {
            recyclerView = rv
        }
        if (scaleGestureDetector == null) {
            scaleGestureDetector = ScaleGestureDetector(rv.context, this)
        }
        return e.pointerCount > 1
    }
    
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        scaleGestureDetector?.onTouchEvent(e)
    }
    
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit
    
    private var avg = 1
    private var scaleLevel = 1F
    private var scaleBase = 1F
    private var lastScale = 0
    
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        avg = ((avg + detector.currentSpan) / 2).toInt()
        scaleLevel = avg / scaleBase
        if (lastScale != 0) {
            scaleLevel *= lastScale
        }
        
        recyclerView?.let {
            it.scaleX = scaleLevel
            it.scaleY = scaleLevel
            it.invalidate()
        }
        
        return true
    }
    
    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        recyclerView?.let {
            it.pivotX = detector.focusX
            it.pivotY = detector.focusY
        }
        scaleBase = detector.currentSpan
        avg = scaleBase.toInt()
        return true
    }
    
    override fun onScaleEnd(detector: ScaleGestureDetector) {
        lastScale = scaleLevel.toInt()
    }
    
}