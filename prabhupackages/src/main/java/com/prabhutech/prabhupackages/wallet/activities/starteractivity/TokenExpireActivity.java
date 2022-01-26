package com.prabhutech.prabhupackages.wallet.activities.starteractivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;
import com.prabhutech.prabhupackages.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TokenExpireActivity extends AppCompatActivity {
    private static final String TAG = "TokenExpireActivity";

    Toolbar toolbar;
    AnimButton onBack;
    TextView responseMessage;
    // Intent gotValue;
    String responseMsg = "";
    public static final String CONTACTUS_FALLBACK = "{\"prabhupaysupport\":{\"customersupport\":[{\"supportNumber\":\"9860610000\",\"isActive\":true},{\"supportNumber\":\"9801572999\",\"isActive\":true},{\"supportNumber\":\"9801979111\",\"isActive\":true},{\"supportNumber\":\"9801979112\",\"isActive\":true},{\"supportNumber\":\"9801979115\",\"isActive\":true},{\"supportNumber\":\" 9801979117\",\"isActive\":true}],\"viber\":[{\"vibernumber\":\"9860486117\",\"isActive\":true},{\"vibernumber\":\" 9801007702\",\"isActive\":true}],\"tollfree\":[{\"tollfreenum\":\"166001-88880 (NTC)\",\"isActive\":true}],\"socialmedia\":[{\"medialink\":\"https://www.facebook.com/PrabhuPay\",\"imageurl\":\"http://client.prabhupay.com/images/icon/facebook.png\",\"isActive\":true},{\"medialink\":\"https://www.instagram.com/prabhupay/?hl=ne\",\"imageurl\":\"http://client.prabhupay.com/images/icon/insta.png\",\"isActive\":true}],\"mail\":[{\"mailid\":\"info@prabhupay.com\",\"isActive\":true}]}}";

    TextView supportTitle, viberTitle, tollFreeTitle, emailTitle;
    //Adapter
    ContactSupportAdapter contactSupportAdapter;
    //RecyclerView
    LottieAnimationView accountErrorImage;
    RecyclerView contactSupportRecycler, viberNumberRecycler, tollFreeRecycler, socialMediaRecycler, emailSupportRecycler;
    private Object quitTimeout;

    //Alert animated button
    LottieDrawable giftIconAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_expire);

        toolbar = findViewById(R.id.toolbar);
        responseMessage = findViewById(R.id.reponseMessage);
        onBack = findViewById(R.id.btnback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Error");
        // gotValue = getIntent();
        supportTitle = findViewById(R.id.headerSupport);
        viberTitle = findViewById(R.id.headerViber);
        tollFreeTitle = findViewById(R.id.headerTollFree);
        emailTitle = findViewById(R.id.textView28);
        accountErrorImage = findViewById(R.id.accountErrorImage);

        contactSupportRecycler = findViewById(R.id.contactSupport);
        viberNumberRecycler = findViewById(R.id.viber_recycler_view);
        tollFreeRecycler = findViewById(R.id.tollFree_recycler_view);
        socialMediaRecycler = findViewById(R.id.social_media_recycler_view);
        emailSupportRecycler = findViewById(R.id.email_recycler_view);

//        // Glide.with(getApplicationContext()).load(R.raw.no_service_raw).into(accountErrorImage);
//        MiscRepo.GetContactUs(getApplicationContext(), new JsonObject())
//                .subscribe(new DisposableObserver<JsonObject>() {
//                    @Override
//                    public void onNext(@io.reactivex.annotations.NonNull JsonObject jsonObject) {
//                        JsonObject parsedData = jsonObject.getAsJsonObject("data");
//                        showCotactDailogWithValues(parsedData);
//
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                        JsonObject parsedData = new JsonParser().parse(CONTACTUS_FALLBACK).getAsJsonObject();
//                        showCotactDailogWithValues(parsedData);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


        onBack.setOnClickListener(v -> goToStarterActivity());
    }

    private void goToStarterActivity() {
        Intent intent = new Intent(TokenExpireActivity.this, StarterActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToStarterActivity();
    }

    public class ContactSupportAdapter extends RecyclerView.Adapter<ContactSupportAdapter.ViewHolder> {

        Context context;
        ArrayList<String> phoneNumber;

        public ContactSupportAdapter(Context context, ArrayList<String> phoneNumber) {
            this.context = context;
            this.phoneNumber = phoneNumber;

        }

        @NonNull
        @Override
        public ContactSupportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_support_recycler, parent, false);
            return new ContactSupportAdapter.ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(@NonNull ContactSupportAdapter.ViewHolder holder, int position) {

            Map<String, String> map = new HashMap<String, String>();
            holder.value.setText(phoneNumber.get(position));
            SpannableString content = new SpannableString(phoneNumber.get(position));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            if (phoneNumber.size() == 1) {
//                holder.value.setText(phoneNumber.get(position));
                holder.value.setText(content);
            } else {
                if (position % 2 == 0) {
//                    holder.value.setText(phoneNumber.get(position) + " | ");
                    content = new SpannableString(phoneNumber.get(position) + " | ");
                    content.setSpan(new UnderlineSpan(), 0, content.length() - 3, 0);
                    holder.value.setText(content);
                } else {
                    holder.value.setText(content);
                }
            }

            holder.value.setOnClickListener(v -> {
                String phoneNumer = phoneNumber.get(position);
                if (!phoneNumer.equals("9860486117") && !phoneNumer.equals(" 9801007702")) {
                    try {

                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:" + phoneNumer));
                        startActivity(i);
                    } catch (Exception e) {
                        ClipboardManager myClipboard = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = myClipboard.getPrimaryClip();
                        clip = clip.newPlainText("text", phoneNumer);
                        myClipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Error Occured! Phone Number is Copied", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    try {

                        Intent smsIntent = new Intent("android.intent.action.VIEW");

                        smsIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        smsIntent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");


                        Uri uri = Uri.parse("tel:+977" + Uri.encode(phoneNumer));
                        smsIntent.setData(uri);

                        startActivity(smsIntent);
                    } catch (Exception es) {
                        Log.e(TAG, "onBindViewHolder: " + es.getMessage());
                        Toast.makeText(context, "Viber not Found", Toast.LENGTH_SHORT).show();
                        try {

                            Intent i = new Intent(Intent.ACTION_DIAL);
                            i.setData(Uri.parse("tel:" + phoneNumer));
                            startActivity(i);
                        } catch (Exception e) {
                            ClipboardManager myClipboard = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
                            ClipData clip = myClipboard.getPrimaryClip();
                            clip = clip.newPlainText("text", phoneNumer);
                            myClipboard.setPrimaryClip(clip);
                            Toast.makeText(getApplicationContext(), "Error Occured! Phone Number is Copied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return phoneNumber.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, value;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

//                title = itemView.findViewById(R.id.titleCustomerSupport);
                value = itemView.findViewById(R.id.valueCustomerSupport);
            }
        }
    }

}