package i1170.com.lfgame;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Ray_L_Pain on 2017/10/10 0010.
 * X5内核的WebView
 */

public class X5WebView extends WebView {
    public static final String TAG = "X5WebView";
    /**
     * 如果是APP里调起H5支付，需要在webview中手动设置referer，如(
     * Map extraHeaders = new HashMap();
     * extraHeaders.put("Referer", "商户申请H5时提交的授权域名");//例如 http://www.baidu.com ))
     */
    public static final String WX_PAY_REFERER_KEY = "Referer";
    public static final String WX_PAY_REFERER_VALUE = "http://game.koko360.com";
    /**
     * H5微信支付地址前缀
     */
    public static final String WX_PAY_URL_PREFIX = "https://wx.tenpay.com";
    OnScrollChangedCallback mOnScrollChangedCallback;

    public X5WebView(Context context) {
        super(context);
        setBackgroundColor(85621);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        initWebViewSettings();
        this.getView().setClickable(true);
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
//        1、提高渲染的优先级
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback OnScrollChangedCallback) {
        mOnScrollChangedCallback = OnScrollChangedCallback;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
        }
    }

    public interface OnScrollChangedCallback {
        public void onScroll(int l, int t, int oldl, int oldt);
    }
}
