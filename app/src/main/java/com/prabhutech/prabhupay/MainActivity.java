package com.prabhutech.prabhupay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.prabhutech.prabhupackages.wallet.activities.starteractivity.StarterActivity;
import com.prabhutech.prabhupackages.wallet.views.AppInfoClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppInfoClass.AppDrawableIcon = R.drawable.login_logo;
        AppInfoClass.AppLoginIcon = R.drawable.login_logo;
        AppInfoClass.AppTheme = R.style.Theme_PrabhuCoop;
      Intent intent =   new Intent(MainActivity.this,StarterActivity.class);
      startActivity(intent);
      finishAffinity();
    }
}