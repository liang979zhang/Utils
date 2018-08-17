package face.com.zdl.cctools.Permission2;

import java.util.List;

public interface OnPermission {

    /**
     * 有权限被授予时回调
     *
     * @param granted 请求成功的权限组
     * @param isAll   是否全部授予了
     */
    void hasPermission(List<String> granted, boolean isAll);

    /**
     * 有权限被拒绝授予时回调
     *
     * @param denied 请求失败的权限组
     * @param quick  是否被系统自动拒绝了
     */
    void noPermission(List<String> denied, boolean quick);
}
