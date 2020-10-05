package me.bakumon.moneykeeper.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;


import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.login.util.cache.ACache;
import me.bakumon.moneykeeper.login.util.constant.Constant;
import me.bakumon.moneykeeper.ui.LauncherActivity;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout enterChoice;
    private Button mLoginTextChoice,mLoginGestureChoice,mLoginConfirm;
    private LockPatternView mLoginGesture;
    private EditText mLoginText;
    private byte[] gesturePassword;
    private ACache aCache;
    private static final long DELAYTIME = 600l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        final SharedPreferences setting = getSharedPreferences("login", 0);
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

        mLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mLoginText.getText().toString();
                if (password.equals(setting.getString("textPassword","0"))){
                    Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"登录失败，请检查密码",Toast.LENGTH_SHORT).show();
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
        mLoginGesture.setVisibility(View.INVISIBLE);

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
        Intent intent = new Intent(LoginActivity.this,LauncherActivity.class);
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

}