package face.com.zdl.cctools.bean;

/**
 * Created by Administrator on 2018/4/26.
 */

public class TipInfo {
    public String sureBtnText;
    public String cancelBtnText;
    public int iconResId;
    public String title;
    public String msg;

    public static TipInfo createTipInfo(String title, String msg) {
        TipInfo tipInfo = new TipInfo();
        tipInfo.msg = msg;
        tipInfo.title = title;
        return tipInfo;
    }
}
