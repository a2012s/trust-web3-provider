package trust.web3provider;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.io.InputStream;

public class MainActivityWebview2 extends AppCompatActivity {

    private static final String TAG = "logcat";
    private BridgeWebView mWebView;
    //private FrameLayout mRootView;
    private String mJsString = "";

    private TextView textView;


    //private String url = "http://public.rongcloud.cn/view/D4F444BE2D94D760329F3CF38B4AE35C";
    private String url = "https://app.compound.finance";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_web2);


        textView = findViewById(R.id.textView);
        mWebView = findViewById(R.id.webView);


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); //允许h5使用javascript
        webSettings.setDomStorageEnabled(true);//允许android调用javascript



        //android 5.0以上不允许WebView加载http的图片，通过以下设置可以解决
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);
                // hidenBanner(webView);

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    // progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("进度：" + newProgress);
                    //更新进度
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setProgress(newProgress);
                    // hidenBanner(webView);
                }
            }

        });

        //必须和js函数名字一致，注册好具体执行回调函数，类似java实现类。
        mWebView.registerHandler("requestAccounts", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                String str = "这是html返回给java的数据:" + data;
                // 例如你可以对原始数据进行处理
                str = str + ",Java经过处理后截取了一部分：" + str.substring(0, 5);
                Log.i(TAG, "handler = requestAccounts, data from web = " + data);
                Toast.makeText(MainActivityWebview2.this, str, Toast.LENGTH_SHORT).show();
                //回调返回给Js
                function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
            }

        });


    }


    private void hidenBanner(WebView view) {
        //隐藏元素
        String javascript = "javascript:function hideBanner() {" +
                "var banners = document.getElementsByClassName('content-header-info');" +
                "var firstbanner = banners[0];" +
                "firstbanner.remove();" + "}";

        view.loadUrl(javascript);//创建方法
        view.loadUrl("javascript:hideBanner();");//加载方法
    }

    private void inputJS(WebView view) {
        //隐藏元素
        String jsStr = "";

        try {
            Resources res = getResources();
            InputStream in = res.openRawResource(trust.web3provider.R.raw.trust_min);

            byte[] b = new byte[in.available()];
            int readLen = in.read(b);
            jsStr = String.format("Len: %1$s\n%2$s", readLen, new String(b));

            ((TextView) findViewById(R.id.out)).setText(jsStr);
        } catch (Exception e) {
            ((TextView) findViewById(R.id.out)).setText(e.getMessage());
        }
        view.loadUrl(jsStr);//创建方法

        //String rpcURL="https://mainnet.infura.io/v3/76208b0279d342f1aa8857cb5a31e45e"; chainId:1 address:从当前钱包获取
        view.loadUrl("(function() {\n" +
                "     var config = {\n" +
                "                    address: '0xEa5CDC6625416c87913180847ace2Bf42Dbb422e'.toLowerCase(),\n" +
                "                    chainId: 1,\n" +
                "                    rpcUrl: \'https://mainnet.infura.io/v3/76208b0279d342f1aa8857cb5a31e45e'\n" +
                "                };\n" +
                "                const provider = new window.Trust(config);\n" +
                "                window.ethereum = provider;\n" +
                "                window.web3 = new window.Web3(provider);\n" +
                "                window.web3.eth.defaultAccount = config.address;\n" +
                "\n" +
                "                window.chrome = {webstore: {}};\n" +
                "            })();");//加载方法


    }


    @Override
    protected void onDestroy() {

        /**
         * 先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
         */
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            /**
             * 清理cache 和历史记录
             */
            mWebView.clearCache(true);
            mWebView.clearHistory();
            /**
             * 销毁 Webview
             * Webview调用destory时
             * Webview仍绑定在Activity
             */
            // mRootView.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        System.exit(0);
    }


}




