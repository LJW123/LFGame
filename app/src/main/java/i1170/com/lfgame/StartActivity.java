package i1170.com.lfgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import i1170.com.lfgame.activity.BaseActivity;

public class StartActivity extends BaseActivity {

    private CustomVideoView  customVideoView;
    private View placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_start);
        initView();
        initListener();
    }

    private void initView() {
        customVideoView = findViewById(R.id.video_view);
        placeholder = findViewById(R.id.placeholder);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.launcher);
        customVideoView.playVideo(uri);
    }

    private void initListener() {
        customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                placeholder.setBackgroundResource(R.mipmap.bg_launch_placeholder);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 记得在销毁的时候让播放的视频终止
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customVideoView != null) {
            customVideoView.stopPlayback();
        }
    }
}
