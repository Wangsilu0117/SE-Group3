package me.bakumon.moneykeeper.login;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.security.KeyStore;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.login.util.FingerprintDialogFragment;
import me.bakumon.moneykeeper.login.util.cache.ACache;
import me.bakumon.moneykeeper.login.util.constant.Constant;
import me.bakumon.moneykeeper.ui.LauncherActivity;

//import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout enterChoice;
    private Button mLoginTextChoice,mLoginGestureChoice,mFingerChoice;
    private RelativeLayout mLoginConfirm;
    private LockPatternView mLoginGesture;
    private EditText mLoginText;
    private byte[] gesturePassword;
    private ACache aCache;
    private static final long DELAYTIME = 600l;

    private static final String DEFAULT_KEY_NAME = "default_key";

    KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        final SharedPreferences setting = getSharedPreferences("user", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);  //第一次会取出True，因为没有创建这个键对

        if(user_first){//第一次
            Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(register);
        }
        this.init();

        mLoginGestureChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginGesture.setVisibility(View.VISIBLE);
                mLoginText.setVisibility(View.INVISIBLE);
                mLoginConfirm.setVisibility(View.INVISIBLE);
            }
        });

        mLoginTextChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginGesture.setVisibility(View.INVISIBLE);
                mLoginText.setVisibility(View.VISIBLE);
                mLoginConfirm.setVisibility(View.VISIBLE);
            }
        });

        mFingerChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginText.setVisibility(View.INVISIBLE);
                mLoginConfirm.setVisibility(View.INVISIBLE);
                mLoginGesture.setVisibility(View.INVISIBLE);
                if (supportFingerprint()) {
                    initKey();
                    initCipher();
                }
//                Intent intent = new Intent(LoginActivity.this,FingerActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        mLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mLoginText.getText().toString();
//              String x = setting.getString("password","0");
                String encode = setting.getString("password","0");
                byte[] decode = Base64.decode(encode.getBytes(), Base64.DEFAULT);
                String x = new String(decode,0,decode.length);
                if (password.equals(x)){
                    Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //String[] a = PasswordHelp.readPassword(LoginActivity.this);
                    Toast.makeText(LoginActivity.this,"登录失败，请检查密码", Toast.LENGTH_SHORT).show();
                    mLoginText.setText("");
                }
            }
        });


    }

    //初始化控件和控件状态
    private void init(){
        enterChoice = findViewById(R.id.enterChoice);
        mLoginTextChoice = findViewById(R.id.login_textChoice);
        mLoginGestureChoice = findViewById(R.id.login_gestureChoice);
        mLoginGesture = findViewById(R.id.login_gesture);
        mLoginText = findViewById(R.id.login_text);
        mLoginConfirm = findViewById(R.id.login_confirm);
        mFingerChoice = findViewById(R.id.fingerChoice);
        mLoginGesture.setVisibility(View.INVISIBLE);
        mLoginTextChoice.getBackground().setAlpha(0);
        mLoginGestureChoice.getBackground().setAlpha(0);
        mFingerChoice.getBackground().setAlpha(0);
        //mLoginConfirm.getBackground().setAlpha(00);
        aCache = ACache.get(LoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
        mLoginGesture.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            mLoginGesture.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    System.out.println("密码匹配成功！");
                    updateStatus(Status.CORRECT);
                } else {
                    System.out.println("密码匹配失败！");
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        switch (status) {
            case DEFAULT:
                mLoginGesture.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                mLoginGesture.setPattern(LockPatternView.DisplayMode.ERROR);
                mLoginGesture.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                mLoginGesture.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();

    }

//    /**
//     * 忘记手势密码（去账号登录界面）
//     */
//    @OnClick(R.id.forgetGestureBtn)
//    void forgetGesturePasswrod() {
//        Intent intent = new Intent(GestureLoginActivity.this, CreateGestureActivity.class);
//        startActivity(intent);
//        this.finish();
//    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }


    //以下是指纹密码部分
    public boolean supportFingerprint() {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(this, "您的系统版本过低，不支持指纹功能", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(this, "您的手机不支持指纹功能", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(this, "您还未设置锁屏，请先设置锁屏并添加一个指纹", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "您至少需要在系统设置中添加一个指纹", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    private void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(23)
    private void initCipher() {
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            showFingerPrintDialog(cipher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        FingerprintDialogFragment fragment = new FingerprintDialogFragment();
        fragment.setCipher(cipher);
        fragment.show( getSupportFragmentManager(), "fingerprint");
    }

    public void onAuthenticated() {
        Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }

}