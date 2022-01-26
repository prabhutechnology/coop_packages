package com.prabhutech.prabhupackages.wallet.activities.starteractivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.prabhutech.prabhupackages.databinding.ActivityStarterBinding;
import com.prabhutech.prabhupackages.wallet.utils.TimerUtils;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.views.AppInfoClass;

public class StarterActivity extends AppCompatActivity {
    private static final String TAG = "StarterActivity";

    private static String fragmentLabel;

    private Object quitTimeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppInfoClass.AppTheme);
        ActivityStarterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_starter);
    }

    public static String getFragmentLabel() {
        return fragmentLabel;
    }

    public static void setFragmentLabel(String fragmentLabel) {
        StarterActivity.fragmentLabel = fragmentLabel;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentLabel().equals("LoginFragment")) {
            if (quitTimeOut == null) {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                quitTimeOut = TimerUtils.setTimeout(() -> {
                    TimerUtils.clearTimeout(quitTimeOut);
                    quitTimeOut = null;
                }, 2000);
            } else
                super.onBackPressed();
        } else
            super.onBackPressed();
    }
}