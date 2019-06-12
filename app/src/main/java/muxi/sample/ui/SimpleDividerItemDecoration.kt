package muxi.sample.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import muxi.sample.R

class SimpleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDivider = context.resources.getDrawable(R.drawable.line_divider, null)

        }else{
            mDivider = context.resources.getDrawable(R.drawable.line_divider)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.getPaddingLeft()
        val right = parent.getWidth() - parent.getPaddingRight()

        val childCount = parent.getChildCount()
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.getLayoutParams() as RecyclerView.LayoutParams

            val top = child.getBottom() + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}