package i1170.com.lfgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    public static final String HOME_URL = "http://game.koko360.com/index/index/index.html";
    private X5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.web_view);
        webView.loadUrl(HOME_URL);
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
}
