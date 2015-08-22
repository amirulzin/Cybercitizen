package com.opensource.common.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridItemDecoration extends RecyclerView.ItemDecoration
{
    private int mSpacing = 0;
    private int mSpanCount = 0;

    public GridItemDecoration(Context context, int spanCount, int marginDp)
    {
        this(spanCount, (int) (context.getResources().getDisplayMetrics().density * marginDp));
    }

    public GridItemDecoration(int spanCount, int spacing)
    {
        mSpanCount = spanCount;
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int mod;

        mod = position % mSpanCount;
        outRect.left = mod * mSpacing / mSpanCount;
        outRect.right = (mSpanCount - mod - 1) * mSpacing / mSpanCount;
        if (position >= mSpanCount)
        {
            outRect.top = mSpacing;
        }
    }
}