package i1170.com.lfgame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author xiaofo on 2018/6/27.
 */

public class AliPayUtil {
    /**
     * 启动支付宝标记
     */
    public static final String ALIPAYS_PLATFORMAPI_START_APP = "alipays://platformapi/startApp";

    /**
     * 检查是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse(ALIPAYS_PLATFORMAPI_START_APP);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}
