package i1170.com.lfgame;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author lxf
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String HOME_URL = "http://game.koko360.com/index/index/index.html";
    /**
     * 微信支付标记
     */
    public static final String WEIXIN_WAP_PAY = "weixin://wap/pay";
    /**
     * 支付宝支付标记
     */
    public static final String ALIPAYS_PLATFORMAPI = "alipays://platformapi";
    //打开相册选择图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;
    private X5WebView webView;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.web_view);
        webView.loadUrl(HOME_URL);
        setWebViewClient();
        setWebChromeClient();
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith(WEIXIN_WAP_PAY)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                if (url.contains(ALIPAYS_PLATFORMAPI)) {
                    boolean visit = AliPayUtil.checkAliPayInstalled(MainActivity.this);
                    if (visit) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });
    }

    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
                                       // For Android < 3.0
                                       public void openFileChooser(ValueCallback<Uri> valueCallback) {
                                           uploadMessage = valueCallback;
                                           openImageChooserActivity();
                                       }

                                       // For Android  >= 3.0
                                       public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                                           uploadMessage = valueCallback;
                                           openImageChooserActivity();
                                       }

                                       //For Android  >= 4.1
                                       @Override
                                       public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                                           uploadMessage = valueCallback;
                                           openImageChooserActivity();
                                       }

                                       // For Android >= 5.0
                                       @Override
                                       public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                                           uploadMessageAboveL = filePathCallback;
                                           openImageChooserActivity();
                                           return true;
                                       }
                                   }
        );
    }

    private void openImageChooserActivity() {
        takePhoto();
    }

    /**
     * 打开相册选择图片
     */
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean canGoBack = webView.canGoBack();
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack) {
            if (webView.getUrl().equals(HOME_URL)) {
                return super.onKeyDown(keyCode, event);
            }
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == uploadMessage && null == uploadMessageAboveL) {
            return;
        }
        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }
        Uri result = null;
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.getData();
            }
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
        Log.i(TAG, "选择结果:" + result);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null) {
                results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

}
