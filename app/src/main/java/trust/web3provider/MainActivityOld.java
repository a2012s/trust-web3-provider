package trust.web3provider;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivityOld extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        String jsStr = "";

        try {
            Resources res = getResources();
            InputStream in = res.openRawResource(R.raw.trust);

            byte[] b = new byte[in.available()];
            int readLen = in.read(b);
            jsStr = String.format("Len: %1$s\n%2$s", readLen, new String(b));

            ((TextView) findViewById(R.id.out)).setText(jsStr);
        } catch (Exception e) {
            ((TextView) findViewById(R.id.out)).setText(e.getMessage());
        }
    }
}
