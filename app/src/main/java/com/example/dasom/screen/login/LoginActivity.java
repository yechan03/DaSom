package com.example.dasom.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.example.dasom.R;
import com.example.dasom.api.NetworkHelper;
import com.example.dasom.databinding.ActivityLoginBinding;
import com.example.dasom.screen.MainActivity;
import com.example.dasom.util.PhoneUtil;
import com.example.dasom.util.TokenCache;
import com.example.dasom.util.UserCache;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);

        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);
        PinLockView mPinLockView = findViewById(R.id.pin_lock_view);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        binding.btnLoginDebug.setOnClickListener(v -> {
            //kinda debug stuff
            signIn("010-3511-9295", "1234");
        });

    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            signIn(PhoneUtil.getPhone(LoginActivity.this), pin);
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    private void signIn(String phone, String pin) {
        NetworkHelper.getInstance(getString(R.string.base_url)).SignIn(phone, pin)
                .enqueue(new Callback<UserLogin>() {
                    @Override
                    public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                        if (response.isSuccessful()) {
                            UserLogin userLogin = response.body();

                            TokenCache.setToken(LoginActivity.this, userLogin.getAccessToken());
                            UserCache.setUser(LoginActivity.this, PhoneUtil.getPhone(LoginActivity.this));

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "PIN이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<UserLogin> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

}