package com.example.dasom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.dasom.api.NetworkHelper;
import com.example.dasom.data.UserJoin;
import com.example.dasom.data.UserLogin;
import com.example.dasom.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private String id,pw;
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;
    private ActivityLoginBinding binding;
    private final static String TAG = "asdasd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);

        //전화번호 넣어줘
        id = "01024169913";


        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);



    }
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.e(TAG, "Pin complete: " + pin);

            NetworkHelper.getInstance().SignIn(id,pin).enqueue(new Callback<UserLogin>() {
                @Override
                public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {

                    UserLogin userLogin = response.body();
                    Log.e("asd",response.code()+"");
                    Log.e("asd",userLogin.getMessage());
                    login();

                }

                @Override
                public void onFailure(Call<UserLogin> call, Throwable t) {

                }
            });


        }

        @Override
        public void onEmpty() {
            Log.e(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.e(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };


    public void login(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}