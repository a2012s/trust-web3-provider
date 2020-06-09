package trust.web3provider;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivityWebview extends AppCompatActivity {

    private static final String TAG = "logcat";
    private WebView mWebView;
    //private FrameLayout mRootView;
    private String mJsString = "";

    private TextView textView;

    private String url = "http://public.rongcloud.cn/view/D4F444BE2D94D760329F3CF38B4AE35C";

    private String jsString = "(function() {\n" +
            "    var PAG_NATIVE = window.PAG_NATIVE = {};\n" +
            "    PAG_NATIVE.callbacks = {};\n" +
            "    PAG_NATIVE.callBacks = {};\n" +
            "    PAG_NATIVE.exec = function(funName, args, callbackId) {\n" +
            "        var commend = {\n" +
            "            functionName: funName,\n" +
            "            arguments: args,\n" +
            "            callbackId: callbackId\n" +
            "        };\n" +
            "        window.android[funName](args);\n" + "    };\n" +
            "    PAG_NATIVE.execCallBack = function(callbackId, res) {\n" +
            "        var callBack = PAG_NATIVE.callBacks[callbackId];\n" +
            "        if (callBack) {\n" +
            "            callBack(res);\n" +
            "            delete PAG_NATIVE.callBacks['callbackId'];\n" +
            "        }\n" +
            "    };\n" +
            "    PAG_NATIVE.openBluetoothAdapter = function(object) {\n" +
            "        var result = PAG_NATIVE.transformObject(object);\n" +
            "        PAG_NATIVE.exec('openBluetoothAdapter', result, \"\");\n" +
            "    };\n" +
            "\n" +
            "    PAG_NATIVE.transformObject = function(object) {\n" +
            "        for (key in object) {\n" +
            "            if (typeof (object[key]) == 'function') {\n" +
            "                var identifier = (new Date()).getTime() + key;\n" +
            "                PAG_NATIVE.callbacks[identifier] = object[key];\n" +
            "                object[key] = identifier;\n" +
            "            }\n" +
            "        }\n" +
            "        return JSON.stringify(object);\n" +
            "    };\n" +
            "\n" +
            "    PAG_NATIVE.commonCallback = function(object) {\n" +
            "        for (key in object) {\n" +
            "            if (typeof (PAG_NATIVE.callbacks[key]) == 'function') {\n" +
            "                PAG_NATIVE.callbacks[key](object[key]);\n" +
            "                PAG_NATIVE.callbacks['key'] = null;\n" +
            "            }\n" +
            "        }\n" +
            "};\n" +
            "})();";

    // private WebView webView;

    /**
     * 虎牙直播  这些需要更换
     * 房间号  ？
     * topSid
     * subSid
     * topSid subSid 获取方式：
     * 随便打开一个虎牙直播界面，
     * chrome 浏览器 --> 更多工具-->开发者工具 -->
     * 或者直接 有机 检查 -->  -->(菜单栏 Elements Ctrl + F 查找 topSid)  复制这两个值即可
     */
    private final static String HUYA_WEB_URL = "http://m.huya.com/" + "mianzai"; // only load js this is not need
    private final static String TOP_SID = "77226790";
    private final static String SUB_SID = "2565893989";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_web);


        textView = findViewById(R.id.textView);
        mWebView = findViewById(R.id.webView);


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); //允许h5使用javascript
        webSettings.setDomStorageEnabled(true);//允许android调用javascript

        // webSettings.setBlockNetworkImage(false);


        //mWebView.addJavascriptInterface(new MyJavaScriptBridge(), "ANDROID_CLIENT");
        mWebView.addJavascriptInterface(new JSObject(), "myObj");
        //android 5.0以上不允许WebView加载http的图片，通过以下设置可以解决
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          Log.e("logcat", "shouldOverrideUrlLoading   " + url);
                                          //Toast.makeText(MainActivityWebview.this, "wodo-7-", Toast.LENGTH_SHORT).show();
                                          textView.setText("public.rongcloud.cn/view/D4F444BE2D94D760329F3CF38B4AE35C");
                                          view.loadUrl(url);
                                          return true;//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                                      }


                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {


                                          Log.e("logcat", "onPageStarted");


                                          String js = "var script = document.createElement('script');";
                                          js += "script.type = 'text/javascript';";
                                          js += "var child=document.getElementsByTagName('a')[0];";
                                          js += "child.οnclick=function(){userIdClick();};";
                                         // js += "function userIdClick(){myObj.getClose();};";
                                          //js += "function userIdClick(){prompt(\"prompt\");};";//
                                           js += "function userIdClick(){alert('hello');};";


                                          view.evaluateJavascript(js, new ValueCallback<String>() {//将读取的js字符串注入webview中
                                              @Override
                                              public void onReceiveValue(String value) {//js与native交互的回调函数
                                                  Log.d(TAG, "value=" + value);
                                              }
                                          });
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url) {

                                          Log.e("logcat", "onPageFinished");

                                          //  mWebView.loadUrl("javascript:alert('hello')");

                                          String js = "var script = document.createElement('script');";
                                          js += "script.type = 'text/javascript';";
                                          js += "var child=document.getElementsByTagName('a')[0];";
//                                          js+="alert(child);";
                                          //js += "child.οnclick=function(){userIdClick();};";
                                          //js += "function userIdClick(){myObj.getClose();};";
                                          //js += "function userIdClick(){prompt(\"prompt\");};";//
                                          //js += "function userIdClick(){alert('hello');};";

                                          js += "child.οnclick=function(){alert('hello');};";
                                          mWebView.loadUrl("javascript:" + js);

                                          super.onPageFinished(view, url);
                                          //view.loadUrl("javascript:window.myObj.showSource(document.getElementsByTagName('p')[0].innerHTML);");
                                          //view.loadUrl("javascript:myObj.showSource(document.getElementsByTagName('p')[0].innerHTML);");   //关键代码

                                          // hide element by class name
//                                          webview.loadUrl("javascript:(function() { " +
//                                                  "document.getElementsByClassName('your_class_name')[0].style.display='none'; })()");
//                                          // hide element by id
//                                          webview.loadUrl("javascript:(function() { " +
//                                                  "document.getElementById('your_id').style.display='none';})()");

                                      }


                                  }


        );


        //捕获弹窗（或console）
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("logcat", "test" + cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("logcat", "alert" + message);
                //劫持alert以后释放，不然会卡死
                result.confirm();
                return true;
            }
        });


//        mRootView = (FrameLayout) findViewById(R.id.activity_main);
//        initWebView();
//        mJsString = initAssertJSToString("js/huya.js");
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


    class JSObject {
        @JavascriptInterface
// sdk17版本以上加上注解
        public String getData(String txt) {
            Log.e("logcat", "12345");
            //Toast.makeText(MainActivityWebview.this, "12345" + txt, Toast.LENGTH_SHORT).show();
            return "12345678";
        }


        @JavascriptInterface
// sdk17版本以上加上注解
        public void getClose() {
            Log.e("logcat", "dododo");
            Toast.makeText(MainActivityWebview.this, "dododo", Toast.LENGTH_SHORT)
                    .show();
// finish();


        }

        @JavascriptInterface
// sdk17版本以上加上注解
        public void showSource(String html) { //关键代码
            // Toast.makeText(MainActivityWebview.this, html, Toast.LENGTH_SHORT).show();
            // System.out.println("====>html="+html);
            Log.e("logcat", "====>html=" + html);
        }


    }

}




