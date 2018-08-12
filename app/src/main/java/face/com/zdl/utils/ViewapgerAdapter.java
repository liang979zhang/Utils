package face.com.zdl.utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewapgerAdapter extends PagerAdapter {


    private List<View> mImageViews;


    public ViewapgerAdapter(List<View> mImageViews) {
        this.mImageViews = mImageViews;
    }

    @Override
    public int getCount() {
        return mImageViews.size() <=1 ? mImageViews.size() : Short.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mImageView = mImageViews.get(position % mImageViews.size());

        if (mImageView.getParent() == container) {
            container.removeView(mImageView);
        }
        container.addView(mImageViews.get(position % mImageViews.size()));
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
