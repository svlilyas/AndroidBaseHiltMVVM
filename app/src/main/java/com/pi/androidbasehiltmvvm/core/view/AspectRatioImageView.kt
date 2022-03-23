package com.pi.androidbasehiltmvvm.core.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.pi.androidbasehiltmvvm.R

class AspectRatioImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var ratio: Float = DEFAULT_RATIO

    private val path: Path = Path()
    private var cornerRadius: Int = 0
    private var roundedCorner: Int = 0

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.AspectRatioImageView).apply {
                ratio = getFloat(R.styleable.AspectRatioImageView_ari_ratio, DEFAULT_RATIO)
                cornerRadius = getDimensionPixelSize(R.styleable.AspectRatioImageView_radius, 0)
                roundedCorner = getInt(R.styleable.AspectRatioImageView_corner, 0)
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!path.isEmpty) {
            canvas!!.clipPath(path)
        }
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setPath()
    }

    private fun setPath() {
        path.rewind()

        val radii = FloatArray(8)

        when (roundedCorner) {
            0 -> {
                // all side
                for (i in 0 until 8)
                    radii[i] = cornerRadius.toFloat()
            }

            1 -> {
                // top left
                radii[0] = cornerRadius.toFloat()
                radii[1] = cornerRadius.toFloat()
            }

            2 -> {
                // top right
                radii[2] = cornerRadius.toFloat()
                radii[3] = cornerRadius.toFloat()
            }

            3 -> {
                // bottom right
                radii[4] = cornerRadius.toFloat()
                radii[5] = cornerRadius.toFloat()
            }

            4 -> {
                // bottom left
                radii[6] = cornerRadius.toFloat()
                radii[7] = cornerRadius.toFloat()
            }

            5 -> {
                // left
                radii[0] = cornerRadius.toFloat()
                radii[1] = cornerRadius.toFloat()
                radii[6] = cornerRadius.toFloat()
                radii[7] = cornerRadius.toFloat()
            }

            6 -> {
                // right
                radii[2] = cornerRadius.toFloat()
                radii[3] = cornerRadius.toFloat()
                radii[4] = cornerRadius.toFloat()
                radii[5] = cornerRadius.toFloat()
            }

            7 -> {
                // top
                radii[0] = cornerRadius.toFloat()
                radii[1] = cornerRadius.toFloat()
                radii[2] = cornerRadius.toFloat()
                radii[3] = cornerRadius.toFloat()
            }

            8 -> {
                // bottom
                radii[4] = cornerRadius.toFloat()
                radii[5] = cornerRadius.toFloat()
                radii[6] = cornerRadius.toFloat()
                radii[7] = cornerRadius.toFloat()
            }
        }
        path.addRoundRect(
            RectF(
                0F, 0F, width.toFloat(),
                height.toFloat()
            ),
            radii, Path.Direction.CW
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight

        when {
            width > 0 -> height = (width * ratio).toInt()
            height > 0 -> width = (height / ratio).toInt()
            else -> return
        }

        setMeasuredDimension(width, height)
    }

    companion object {
        const val DEFAULT_RATIO = 1F
    }
}
