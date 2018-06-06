package face.com.zdl.cctools.dialog;

/**
 * Created by Administrator on 2018/4/26.
 */

public class TipInfo {
    public String sureBtnText;
    public String cancelBtnText;
    public int iconResId;
    public String title;
    public String msg;

    public static TipInfo createTipInfo(String title, String msg, String sureBtnText, String cancelBtnText, int iconResId) {
        TipInfo tipInfo = new TipInfo();
        tipInfo.msg = msg;
        tipInfo.sureBtnText = sureBtnText;
        tipInfo.cancelBtnText = cancelBtnText;
        tipInfo.iconResId = iconResId;
        tipInfo.title = title;
        return tipInfo;
    }
}
