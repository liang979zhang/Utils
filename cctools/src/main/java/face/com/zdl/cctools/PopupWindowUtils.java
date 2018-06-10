package face.com.zdl.cctools;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by 89667 on 2018/3/5.
 */

public class PopupWindowUtils {


    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;// default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;// default is ture
    private View.OnTouchListener mOnTouchListener;

    private PopupWindowUtils(Context context) {
        mContext = context;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    /**
     * @param anchor
     * @param xOff
     * @param yOff
     * @return
     */
    public PopupWindowUtils showAsDropDown(View anchor, int xOff, int yOff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    public PopupWindowUtils showAsDropDown(View anchor) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public PopupWindowUtils showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     *
     * @param parent  父控件
     * @param gravity
     * @param x       the popup's x location offset
     * @param y       the popup's y location offset
     * @return
     */
    public PopupWindowUtils showAtLocation(View parent, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    /**
     * 添加一些属性设置
     *
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(mClippEnable);
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }
        if (mInputMode != -1) {
            popupWindow.setInputMethodMode(mInputMode);
        }
        if (mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);

    }

    private PopupWindow build() {

        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
        }

        if (mWidth != 0 && mHeight != 0) {
            mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
        } else {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mAnimationStyle != -1) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        apply(mPopupWindow);// 设置一些属性

        mPopupWindow.setFocusable(mIsFocusable);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(mIsOutside);

        if (mWidth == 0 || mHeight == 0) {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 如果外面没有设置宽高的情况下，计算宽高并赋值
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }

        mPopupWindow.update();

        return mPopupWindow;
    }

    /**
     * 关闭popWindow
     */
    public void dissmiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public static class PopupWindowBuilder {
        private PopupWindowUtils mPopupWindowUtils;

        public PopupWindowBuilder(Context context) {
            mPopupWindowUtils = new PopupWindowUtils(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            mPopupWindowUtils.mWidth = width;
            mPopupWindowUtils.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable) {
            mPopupWindowUtils.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId) {
            mPopupWindowUtils.mResLayoutId = resLayoutId;
            mPopupWindowUtils.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            mPopupWindowUtils.mContentView = view;
            mPopupWindowUtils.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            mPopupWindowUtils.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         *
         * @param animationStyle
         * @return
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            mPopupWindowUtils.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable) {
            mPopupWindowUtils.mClippEnable = enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            mPopupWindowUtils.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int mode) {
            mPopupWindowUtils.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            mPopupWindowUtils.mOnDismissListener = onDissmissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            mPopupWindowUtils.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable) {
            mPopupWindowUtils.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            mPopupWindowUtils.mOnTouchListener = touchIntercepter;
            return this;
        }

        public PopupWindowUtils create() {
            // 构建PopWindow
            mPopupWindowUtils.build();
            return mPopupWindowUtils;
        }

    }
}

/*
*
* 用法:



//下方弹出
private void showPopBottom() {
		CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this).setView(R.layout.pop_layout1)
				.setFocusable(true).setOutsideTouchable(true).create().showAsDropDown(mButton1, 0, 10);
	}
//上方弹出
	private void showPopTop() {
		CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this).setView(R.layout.pop_layout2).create();
		popWindow.showAsDropDown(mButton2, 0, -(mButton2.getHeight() + popWindow.getHeight()));
		// popWindow.showAtLocation(mButton1, Gravity.NO_GRAVITY,0,0);
	}
//弹出菜单
	private void showPopMenu() {
		View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu, null);
		// 处理popWindow 显示内容
		handleLogic(contentView);
		// 创建并显示popWindow
		mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this).setView(contentView).create()
				.showAsDropDown(mButton3, 0, 20);

	}


//弹出列表
	private void showPopListView() {
		View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
		// 处理popWindow 显示内容
		handleListView(contentView);
		// 创建并显示popWindow
		mListPopWindow = new CustomPopWindow.PopupWindowBuilder(this).setView(contentView)
				.size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)// 显示大小
				.create().showAsDropDown(mButton4, 0, 20);
	}
*
* */
