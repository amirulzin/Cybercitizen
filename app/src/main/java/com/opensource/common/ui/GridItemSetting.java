package com.opensource.common.ui;

public class GridItemSetting
{
    private int mExtraSideMarginPx;
    private int columnCount;
    private int itemTargetWidthPx;
    private int convertedHeightPx;
    private int marginPx;
    private float displayMetricsDensity;
    private int containerWidthPx;
    private float widthToHeightRatio;

    public GridItemSetting(final float displayMetricsDensity, final int parentContainerWidthPx, final int targetItemWidthDp, final float widthToHeightRatio, final int itemMarginDp)
    {
        this.displayMetricsDensity = displayMetricsDensity;
        this.setMargin(itemMarginDp);
        this.columnCount = (int) (parentContainerWidthPx / (targetItemWidthDp * displayMetricsDensity)); //floored. We throw out the few non-absolute pixels for now.
        this.containerWidthPx = parentContainerWidthPx;

        float yLeftovers = 0;

        float grossTotalMarginPixel = this.columnCount * itemMarginDp * displayMetricsDensity; //Also floored with the same reason as above.
        int totalMarginPixel = (int) grossTotalMarginPixel;
        yLeftovers += grossTotalMarginPixel % 1;

        float grossItemTargetWidth = (parentContainerWidthPx - totalMarginPixel) / (float) this.columnCount;
        this.itemTargetWidthPx = (int) grossItemTargetWidth;

        yLeftovers += grossItemTargetWidth % 1;

        if (yLeftovers >= 2)
            mExtraSideMarginPx = (int) (yLeftovers / 2);
        else mExtraSideMarginPx = 0;

        this.widthToHeightRatio = widthToHeightRatio;
        this.applyWidthToHeightRatio(this.widthToHeightRatio);
    }

    public void setMargin(int marginDP)
    {
        this.marginPx = (int) (marginDP * displayMetricsDensity);
    }

    public void applyWidthToHeightRatio(float ratio)
    {
        this.convertedHeightPx = (int) (this.itemTargetWidthPx / ratio);
    }

    public int getExtraSideMarginPx()
    {
        return mExtraSideMarginPx;
    }

    public int getItemHeightPixels()
    {
        return convertedHeightPx;
    }

    public int getItemWidthPixels()
    {
        return itemTargetWidthPx;
    }

    public int getMarginPixels()
    {
        return marginPx;
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    /**
     * Only call for setting item size based on column count.
     * Setting this will destroy the class values intended for fitting a minimum item width (hence you'll need to construct another {@link GridItemSetting}).
     * Once set, the only usable values onwards (for the grid item) are marginPixels, widthPixels and heightPixels.
     *
     * @param columnCount Preferred column count.
     */
    public void setColumn(int columnCount)
    {
        this.columnCount = columnCount;
        final int itemGrossWidth = containerWidthPx / columnCount;
        this.itemTargetWidthPx = itemGrossWidth - marginPx;
        this.applyWidthToHeightRatio(widthToHeightRatio);
    }
}
