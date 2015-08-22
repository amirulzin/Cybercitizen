package com.opensource.cybercitizen.activity.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.opensource.cybercitizen.R;

public class EatItem implements Parcelable
{
    public static final Parcelable.Creator<EatItem> CREATOR = new Parcelable.Creator<EatItem>()
    {
        public EatItem createFromParcel(Parcel source) {return new EatItem(source);}

        public EatItem[] newArray(int size) {return new EatItem[size];}
    };
    LatLng mLatLng;
    String imageUri;
    String header;
    String wifistatus;
    String congestion;
    String promo;
    int timedPromo = -1;
    boolean expanded = false;
    private boolean mWifiAvailable = false;

    public EatItem(final String imageUri, final String header)
    {
        this.imageUri = imageUri;
        this.header = header;
    }

    public EatItem(final String imageUri, final String header, final String wifistatus, final String congestion, final String promo)
    {
        this.imageUri = imageUri;
        this.header = header;
        this.wifistatus = wifistatus;
        this.congestion = congestion;
        this.promo = promo;
    }

    public EatItem()
    {
    }

    protected EatItem(Parcel in)
    {
        this.mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        this.imageUri = in.readString();
        this.header = in.readString();
        this.wifistatus = in.readString();
        this.congestion = in.readString();
        this.promo = in.readString();
        this.timedPromo = in.readInt();
        this.expanded = in.readByte() != 0;
        this.mWifiAvailable = in.readByte() != 0;
    }

    public LatLng getLatLng()
    {
        return mLatLng;
    }

    public void setLatLng(final LatLng latLng)
    {
        mLatLng = latLng;
    }

    public boolean isExpanded()
    {
        return this.expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    public int getTimedPromo()
    {
        return timedPromo;
    }

    public EatItem setDailyTimedPromo(int endsInMinutes)
    {
        timedPromo = endsInMinutes;
        return this;
    }

    public boolean isWifiAvailable()
    {
        return mWifiAvailable;
    }

    public EatItem setWifiAvailable(boolean available)
    {
        if (available)
        {
            wifistatus = "WiFi Available";
            mWifiAvailable = true;
        }

        else
        {
            wifistatus = "WiFi Not Available";
            mWifiAvailable = false;
        }
        return this;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public String getHeader()
    {
        return header;
    }

    public String getWifiStatus()
    {
        return wifistatus;
    }

    public String getCongestion()
    {
        return congestion;
    }

    public EatItem setCongestion(final int congestion)
    {
        this.congestion = String.valueOf(congestion) + "% Congestion";
        return this;
    }

    public String getPromo()
    {
        return promo;
    }

    public EatItem setPromo(final String promo)
    {
        this.promo = promo;
        return this;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(this.mLatLng, 0);
        dest.writeString(this.imageUri);
        dest.writeString(this.header);
        dest.writeString(this.wifistatus);
        dest.writeString(this.congestion);
        dest.writeString(this.promo);
        dest.writeInt(this.timedPromo);
        dest.writeByte(expanded ? (byte) 1 : (byte) 0);
        dest.writeByte(mWifiAvailable ? (byte) 1 : (byte) 0);
    }

    public static class EatItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private static final int LAYOUT_RES = R.layout.listitem_eathome;
        private ImageView mImageView;
        private TextView mHeader, mWifistatus, mPromo, mCongestion;
        private EatItem mEatItem;
        private View.OnClickListener onClickCallback;

        public EatItemViewHolder(final View itemView)
        {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.li_eh_image);
            mHeader = (TextView) itemView.findViewById(R.id.li_eh_header);
            mWifistatus = (TextView) itemView.findViewById(R.id.li_eh_wifistatus);
            mPromo = (TextView) itemView.findViewById(R.id.li_eh_promo);
            mCongestion = (TextView) itemView.findViewById(R.id.li_eh_congestion);

            itemView.setOnClickListener(this);
        }

        public static int getLayoutRes()
        {
            return LAYOUT_RES;
        }

        public void setOnClickCallback(final View.OnClickListener onClickCallback)
        {
            this.onClickCallback = onClickCallback;
        }

        public void bindData(EatItem eatItem, Context context)
        {
            mEatItem = eatItem;
            Glide.with(context).load(eatItem.getImageUri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView).onLoadFailed(new IllegalStateException("FAIL LOAD"), context.getResources().getDrawable(R.drawable.abc_btn_rating_star_on_mtrl_alpha));

            mHeader.setText(eatItem.getHeader());
            mWifistatus.setText(eatItem.getWifiStatus());
            if (eatItem.isWifiAvailable())
            {
                mWifistatus.setTextColor(context.getResources().getColor(R.color.textColorPositive));
            }
            else
            {
                mWifistatus.setTextColor(context.getResources().getColor(R.color.textColorNegative));
            }

            mPromo.setText(eatItem.getPromo());
            mCongestion.setText(eatItem.getCongestion());

        }

        public void startTimer()
        {
            AsyncTask<Integer, Integer, Void> asyncTask = new AsyncTask<Integer, Integer, Void>()
            {
                private String basePromo = "{init}";

                @Override
                protected Void doInBackground(final Integer... params)
                {
                    int time = params[0];

                    try
                    {
                        if (time > 0)
                        {
                            while (time > 0)
                            {
                                time--;
                                publishProgress(time);
                                Thread.sleep(1000);
                            }
                        }

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(final Integer... values)
                {
                    super.onProgressUpdate(values);
                    if (mEatItem != null)
                    {

                        if (basePromo.equals("{init}"))
                        {
                            basePromo = mEatItem.getPromo();
                        }

                        String outPromo = basePromo + "Ending in " + values[0] + " minutes";

                        mEatItem.setPromo(outPromo);
                    }
                }
            };
            asyncTask.execute(mEatItem.getTimedPromo());
        }

        @Override
        public void onClick(final View v)
        {
            if (onClickCallback != null)
                onClickCallback.onClick(v);
        }
    }
}
