package face.com.zdl.cctools.Permission;

/**
 * Created by 89667 on 2018/3/14.
 * 支持任意重写方法,而无需重写所有的方法
 */

public class PermissionResultAdapter implements PermissionResultCallBack {

    @Override
    public void onPerAllAllow() {

    }

    @Override
    public void onPerAllowList(String... permissions) {

    }

    @Override
    public void onPerNegativeAndNoRemind(String... permissions) {

    }

    @Override
    public void onPerNegativeAndRemind(String... permissions) {

    }


}
