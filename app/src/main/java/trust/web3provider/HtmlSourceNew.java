package trust.web3provider;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

public class HtmlSourceNew extends AppCompatActivity {
    private static final String TAG = "logcat";
    private BridgeWebView webView;

    /**
     * Called when the activity is first created.
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        verifyStoragePermissions(HtmlSourceNew.this);

        webView = findViewById(R.id.webView);
        //webView.getSettings().setJavaScriptEnabled(true);
       // webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        // webView.loadUrl("https://app.compound.finance");
        // webView.loadUrl("file:///android_asset/demo.html");


//        webView.loadUrl("file:///android_asset/jsbridge.html");

//        inputJS(webView);
        //   webView.loadUrl("https://app.compound.finance");

        //
        //webView.loadUrl("https://www.sogou.com/");
        //webView.loadUrl("http://public.rongcloud.cn/view/D4F444BE2D94D760329F3CF38B4AE35C");


        //js调用安卓的requestAccounts;安卓调用js的 window.ethereum.setAddress(addr)方法
        webView.registerHandler("requestAccounts", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                //  Toast.makeText(getActivity(), "js调用安卓的requestAccounts", Toast.LENGTH_SHORT).show();
                Log.e("logcat", "js调用安卓的requestAccounts");
                //pickFile();


            }

        });

        webView.registerHandler("getOS", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(HtmlSourceNew.this, "js调用安卓的registerHandler", Toast.LENGTH_SHORT).show();
                Log.e("logcat", "js调用安卓的registerHandler");
                HashMap response = new HashMap() {{
                    put("error", 0);
                    put("message", "");
                    put("data", new HashMap() {{
                        put("os", "android");
                    }});
                }};
                function.onCallBack(response.toString());
            }
        });

        WebViewClient webViewClient = new WebViewClient() {
            // 老方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("app://login")) {
                    Uri uri = Uri.parse(url);
                    String account = uri.getQueryParameter("account");
                    String password = uri.getQueryParameter("password");

                    // 这里写执行登录的代码

                    // 完了之后返回true，告诉WebView该链接已经被处理了，不要继续加载这个URL
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("logcat", "onPageStarted");
                inputJS(view);
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                Log.d("logcat", "onPageFinished ");


//            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
//                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");


//            String url2 = "";
//            try {
//                Resources res = getResources();
//                InputStream in = res.openRawResource(R.raw.plus);
//
//                byte[] b = new byte[in.available()];
//                int readLen = in.read(b);
//                url2 = new String(b);//; String.format("Len: %1$s\n%2$s", readLen, new String(b));
//
//                // ((TextView) findViewById(R.id.out)).setText(jsStr);
//                // Log.e(TAG, jsStr);
//            } catch (Exception e) {
//                // ((TextView) findViewById(R.id.out)).setText(e.getMessage());
//                Log.e(TAG, "加载错误 " + e.getMessage());
//            }
//
//            // view.loadUrl(url2);
//            view.evaluateJavascript(url2, new ValueCallback<String>() {//将读取的js字符串注入webview中
//                @Override
//                public void onReceiveValue(String value) {//js与native交互的回调函数
//                    Log.d(TAG, "value=" + value);
//                }
//            });


//            view.evaluateJavascript("javascript:window.local_obj.showSource('<head>'+" +
//                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');", null);

                // inputJSPlus(view);

                String newJs = " console.log(\"ua=\"+ navigator.userAgent);";


                view.evaluateJavascript(newJs, null);

                super.onPageFinished(view, url);
            }

            // 新方法，API21之后支持
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().contentEquals("app")) {

                    if (uri.getHost().contentEquals("login")) {

                        String account = uri.getQueryParameter("account");
                        String password = uri.getQueryParameter("password");

                        // 这里写执行登录的代码

                        // 完了之后返回true，告诉WebView该链接已经被处理了，不要继续加载这个URL
                        return true;
                    }

                    return true;
                }

                return super.shouldOverrideUrlLoading(view, request);
            }
        };


        // webView.loadUrl("https://app.compound.finance");
        // webView.loadUrl("file:///android_asset/demo.html");
        // webView.loadUrl("file:///android_asset/jsbridge.html");

        //webView.setWebViewClient(new MyWebViewClient(webView));
        webView.setWebViewClient(webViewClient);


       // webView.loadUrl("file:///android_asset/jsbridge.html");
        webView.loadUrl("https://app.compound.finance");
    }



    private void inputJS(WebView view) {

        String jsStr = "";

        long time1 = System.currentTimeMillis();
        try {
            Resources res = getResources();
            InputStream in = res.openRawResource(R.raw.trustmin13);

            byte[] b = new byte[in.available()];
            int readLen = in.read(b);
            jsStr = new String(b);//; String.format("Len: %1$s\n%2$s", readLen, new String(b));

            // ((TextView) findViewById(R.id.out)).setText(jsStr);
            Log.e(TAG, jsStr);
        } catch (Exception e) {
            // ((TextView) findViewById(R.id.out)).setText(e.getMessage());
            Log.e(TAG, "加载错误 " + e.getMessage());
        }

        long time2 = System.currentTimeMillis();
        Log.e(TAG, "时间：" + (time2 - time1) / 1000);

        // view.loadUrl(jsStr);//创建方法
        view.evaluateJavascript(jsStr, new ValueCallback<String>() {//将读取的js字符串注入webview中
            @Override
            public void onReceiveValue(String value) {//js与native交互的回调函数
                Log.d(TAG, "value=" + value);
            }
        });


        inputJSPlus(view);


    }

    private void inputJSPlus(WebView view) {
        String url2 = "";
        try {
            Resources res = getResources();
            InputStream in = res.openRawResource(R.raw.plus);

            byte[] b = new byte[in.available()];
            int readLen = in.read(b);
            url2 = new String(b);//; String.format("Len: %1$s\n%2$s", readLen, new String(b));

            // ((TextView) findViewById(R.id.out)).setText(jsStr);
            // Log.e(TAG, jsStr);
        } catch (Exception e) {
            // ((TextView) findViewById(R.id.out)).setText(e.getMessage());
            Log.e(TAG, "加载错误 " + e.getMessage());
        }

        // view.loadUrl(url2);
        view.evaluateJavascript(url2, new ValueCallback<String>() {//将读取的js字符串注入webview中
            @Override
            public void onReceiveValue(String value) {//js与native交互的回调函数
                Log.d(TAG, "value=" + value);
            }
        });
    }


    final class MyWebViewClient extends BridgeWebViewClient {
        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            view.loadUrl(url);
//            return true;
            return super.shouldOverrideUrlLoading(view,url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("logcat", "onPageStarted");
            inputJS(view);
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            Log.d("logcat", "onPageFinished ");


//            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
//                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");


//            String url2 = "";
//            try {
//                Resources res = getResources();
//                InputStream in = res.openRawResource(R.raw.plus);
//
//                byte[] b = new byte[in.available()];
//                int readLen = in.read(b);
//                url2 = new String(b);//; String.format("Len: %1$s\n%2$s", readLen, new String(b));
//
//                // ((TextView) findViewById(R.id.out)).setText(jsStr);
//                // Log.e(TAG, jsStr);
//            } catch (Exception e) {
//                // ((TextView) findViewById(R.id.out)).setText(e.getMessage());
//                Log.e(TAG, "加载错误 " + e.getMessage());
//            }
//
//            // view.loadUrl(url2);
//            view.evaluateJavascript(url2, new ValueCallback<String>() {//将读取的js字符串注入webview中
//                @Override
//                public void onReceiveValue(String value) {//js与native交互的回调函数
//                    Log.d(TAG, "value=" + value);
//                }
//            });


//            view.evaluateJavascript("javascript:window.local_obj.showSource('<head>'+" +
//                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');", null);

           // inputJSPlus(view);

            String newJs = " console.log(\"ua=\"+ navigator.userAgent);";


            view.evaluateJavascript(newJs, null);

            super.onPageFinished(view, url);
        }
    }

    public boolean saveHtml(String html, String charactersets) {
        Log.e(TAG, "SaveHtml======================");


        String name = "test.html";
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "2" + File.separator + name);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "saveHtml0==" + e.getMessage());
            e.printStackTrace();
        }

        try {
            Writer out = null;
            out = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), false), charactersets);
            out.write(html);
            out.close();
            Log.i(TAG, "saveHtml==" + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "saveHtml==" + e.getMessage());
            return false;
        }
        return true;
    }


    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.d("logcat", html);
            saveHtml(html, "utf-8");
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}