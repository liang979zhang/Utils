package face.com.zdl.cctools.iostyledialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BaseDialog {

    public Context mContext;
    public Dialog dialog;
    public View rootView;

    public BaseDialog(Context context) {
        this.mContext = context;
    }

    public BaseDialog setScaleWidth(double scWidth) {
        this.rootView.setLayoutParams(new FrameLayout.LayoutParams((int)((double)this.getScreenWidth() * scWidth), -2));
        return this;
    }

    public int getScreenWidth() {
        Point size = new Point();
        WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public int getScreenHeight() {
        Point size = new Point();
        WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getSize(size);
        return size.y;
    }

    public int dp2px(float dpValue) {
        float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}
