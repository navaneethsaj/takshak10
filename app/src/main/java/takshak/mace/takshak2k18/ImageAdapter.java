package takshak.mace.takshak2k18;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mThumbIds;
    LinearLayout expandlayout;
    ImageView expandedimage;

    public ImageAdapter(Context c, String[] urlArray, ImageView imgview, LinearLayout expandlayout) {
        mContext = c;
        mThumbIds = urlArray;
        expandedimage=imgview;
        this.expandlayout = expandlayout;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, final View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setBackgroundResource(R.drawable.imagebg);
            imageView.setPadding(1,1,1,1);
            imageView.setElevation(6);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.getDrawable().getConstantState() == mContext.getDrawable(R.drawable.placeholder).getConstantState()){
                    return;
                }
                expandedimage.setImageDrawable(imageView.getDrawable());
                expandlayout.setVisibility(View.VISIBLE);
            }
        });

        Picasso.get().load(mThumbIds[position]).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
        return imageView;
    }

}
