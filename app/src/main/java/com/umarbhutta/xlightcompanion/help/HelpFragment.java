package com.umarbhutta.xlightcompanion.help;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.main.SlidingMenuMainActivity;
import com.umarbhutta.xlightcompanion.okHttp.HttpUtils;
import com.umarbhutta.xlightcompanion.okHttp.NetConfig;
import com.umarbhutta.xlightcompanion.views.ProgressDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/5.
 */

public class HelpFragment extends Fragment implements View.OnClickListener {

    private WebView webView;
    private ImageButton ib_back, ib_refresh;
    private ImageView iv_menu;
    private TextView textTitle;
    private Button btn_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        textTitle = (TextView) view.findViewById(R.id.tvTitle);
        textTitle.setText(R.string.help);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.setVisibility(View.INVISIBLE);

        webView = (WebView) view.findViewById(R.id.helpWebview);
        ib_back = (ImageButton) view.findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        ib_refresh = (ImageButton) view.findViewById(R.id.ib_refresh);
        ib_refresh.setOnClickListener(this);
        // webview加载数据
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成

                }  else {
                    // 加载中

                }

            }
        });
        getHelpUrl();
        return view;
    }
    private ProgressDialog mDialog;
    /**
     * 获取帮助的url
     */
    public void getHelpUrl() {
        mDialog = ProgressDialogUtils.showProgressDialog(getActivity(), getString(R.string.loading));
        if(mDialog!=null){
            mDialog.show();
        }
        HttpUtils.getInstance().getRequestInfo(NetConfig.URL_GET_HELP_URL, null, new HttpUtils.OnHttpRequestCallBack() {
            @Override
            public void onHttpRequestSuccess(final Object result) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject((String) result);
                            JSONObject dataObj = jsonObject.getJSONObject("data");
                            String helpUrl = dataObj.getString("url");
                            webView.loadUrl(helpUrl);
                            if(mDialog!=null){
                                mDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onHttpRequestFail(int code, String errMsg) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                if (webView.canGoBack()) {
                    webView.goBack();//返回上一页面
                }
                break;
            case R.id.ib_refresh:
                webView.reload();//刷新
                break;
            case R.id.iv_menu:
                switchFragment();
                break;
        }
    }

    // the meat of switching the above fragment
    private void switchFragment() {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuMainActivity) {
            SlidingMenuMainActivity ra = (SlidingMenuMainActivity) getActivity();
            ra.toggle();
        }
    }
}
