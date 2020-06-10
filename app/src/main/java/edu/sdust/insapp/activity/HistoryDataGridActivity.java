package edu.sdust.insapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;

public class HistoryDataGridActivity extends AppCompatActivity {
    @BindView(R.id.wv_history_data_grid_view)
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String url = "http://120.23.37.112:8088/grid/historyGrid.html";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data_grid);

        ButterKnife.bind(this);
        web.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = web.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        web.loadUrl(url);
    }
}
