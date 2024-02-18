package com.bikulwon.manseryeok.adapter.decorator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecorator(
    //그릴 divider의 높이와 색상을 받는다
    private val dividerHeight: Int,
    private val dividerColor: Int = Color.TRANSPARENT
) : RecyclerView.ItemDecoration() {
    //c.drawRect 에서 사용될 변수 선언
    private val paint = Paint()

    // recyclerView 보다 먼저 그려지는 함수
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        myDivider(c,parent,color = dividerColor)
    }

    private fun myDivider(c: Canvas, parent: RecyclerView, color: Int) {
        paint.color = color

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val param = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + param.bottomMargin
            val dividerBottom = dividerTop + dividerHeight

            c.drawRect(
                child.left.toFloat(),
                dividerTop.toFloat(),
                child.right.toFloat(),
                dividerBottom.toFloat(),
                paint
            )
        }
    }

    //recyclerView의 측정된 자식 성격의 메소드 들을 통해 호출되고 커스텀하지 않는경우 크기가 없는  rect를 반환한다.
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = dividerHeight
    }
}