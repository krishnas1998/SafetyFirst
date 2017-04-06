package com.vikas.dtu.safetyfirst2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vikas.dtu.safetyfirst2.R;

public class VideoWebViewActivity extends AppCompatActivity {
    WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web_view);
        url=getIntent().getStringExtra("videostring");

        Display display=getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int Screenwidth=point.x;
        int ScreenHeght=point.y;
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = ScreenHeght / density;
        float dpWidth  = Screenwidth / density;
        int nav_height=getNavigationBarHeight(VideoWebViewActivity.this);
        float nav_dp_height=nav_height / density;

        Log.d("tag001",String.valueOf(dpHeight));
        Log.d("tag001",String.valueOf(dpWidth));
        Log.d("tag001",String.valueOf(nav_dp_height));
        String frameVideo = "<html><body><iframe width=\""+String.valueOf(dpWidth-40)+"\" height=\""+String.valueOf(dpHeight-nav_dp_height-25)+"\" src=\""+url+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";



        webView=(WebView)findViewById(R.id.video_web_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(frameVideo, "text/html", "utf-8");

        webView.setBackgroundColor(getResources().getColor(R.color.black));

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();
    }
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==event.KEYCODE_BACK){
//
//
//            return super.onKeyDown(keyCode,event);
//        }else{
//        return super.onKeyDown(keyCode, event);}
//    }

    @Override
    public void onBackPressed() {
        Log.d("tag01","BACK pressed");
        webView.stopLoading();
        webView.destroy();
        finish();
        super.onBackPressed();
    }
}
