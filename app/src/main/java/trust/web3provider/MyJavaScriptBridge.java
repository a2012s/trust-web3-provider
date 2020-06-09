package trust.web3provider;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class MyJavaScriptBridge {
    private static String mPhoneNumber = null;
    private static String mUsername = null;
    private static String mIdCard = null;

    @JavascriptInterface
    public void showSource() {
        //TODO 打印HTML
        //System.out.print(html);
        Log.e("logcat", "html=   ");

    }

    @JavascriptInterface
    public void showDescription(String str) {
        //TODO 描述
    }

    @JavascriptInterface
    public void showLoginPhone(String phone) {
        //返回手机号码的回调
        mPhoneNumber = phone;
    }

    @JavascriptInterface
    public void showNameAndIdCard(String username, String idcardNo) {
        mUsername = username;
        mIdCard = idcardNo;
        uploadInfo();
    }

    /**
     * 上传数据到服务器
     */
    private void uploadInfo() {
//        if (ClickUtils.isFastClick()) {
//            UserInfo userInfo = new UserInfo();
//            userInfo.setUsername(mUsername);
//            userInfo.setPhoneNo(mPhoneNumber);
//            userInfo.setIdcardNo(mIdCard);
//            userInfo.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null) {
//                        Log.i("test", "添加数据成功，返回objectId为：" + s);
//                    } else {
//                        Log.i("test", "创建数据失败：" + e.getMessage());
//                    }
//                }
//            });
//        }
    }
}
