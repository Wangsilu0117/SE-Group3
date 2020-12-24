package me.bakumon.moneykeeper.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.login.util.cache.ACache;
import me.bakumon.moneykeeper.login.util.constant.Constant;


public class RegisterActivity extends AppCompatActivity {

    private TextView mRegisterTextChoice,mRegisterGestureChoice;
    private EditText mRegisterText,mRegisterText2;
    private LinearLayout mRegister_gesture;
    private TextView mRegister_gesture_text;
    LockPatternView lockPatternView;    //手势
    private Button mRegisterTextConfirm;
    private Button resetBtn;    //手势重新设置
    private List<LockPatternView.Cell> mChosenPattern = null;
    private ACache aCache;
    private static final long DELAYTIME = 600L;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化
        this.init();

        //设置文本密码
        mRegisterTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mRegisterText.getText().toString().equals(mRegisterText2.getText().toString()))
                {
                    mRegisterTextChoice.setVisibility(View.INVISIBLE);
                    mRegisterText.setVisibility(View.INVISIBLE);
                    mRegisterText2.setVisibility(View.INVISIBLE);
                    mRegisterTextConfirm.setVisibility(View.INVISIBLE);
                    mRegisterGestureChoice.setVisibility(View.VISIBLE);
                    mRegister_gesture.setVisibility(View.VISIBLE);
                    //保存文本密码
                    String password = mRegisterText.getText().toString();
//        //加密
//        String encode = Base64.encodeToString(s.getBytes(), Base64.NO_WRAP);
//
//        //解密
//        byte[] decode = Base64.decode(encode.getBytes(), Base64.DEFAULT);
//        Log.e("xyh", "encode: " + encode); //eGlhb3llaGFp
//        Log.e("xyh", "decode: " +new String(decode,0,decode.length)); //xiaoyehai

                    //加密
                    String encode = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
                    SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                    editor.putString("password",encode);
                    editor.apply();

                    SharedPreferences setting = getSharedPreferences("user", 0);
                    setting.edit().putBoolean("FIRST", false).commit();
                }

                else{
                    mRegisterText2.setText("");
                    Toast.makeText(RegisterActivity.this,"密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                }


            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenPattern = null;
                updateStatus(Status.DEFAULT, null);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                mRegister_gesture_text.setText("绘制解锁图案");
            }
        });


    }

    //初始化控件和控件状态
    public void init() {
        mRegisterTextChoice = findViewById(R.id.register_textChoice);
        mRegisterGestureChoice = findViewById(R.id.register_gestureChoice);
        mRegisterText = findViewById(R.id.register_text);
        mRegisterText2 = findViewById(R.id.register_text2);
        mRegister_gesture = findViewById(R.id.register_gesture);
        mRegister_gesture_text = findViewById(R.id.note);
        lockPatternView = findViewById(R.id.lockPatternView);
        resetBtn = findViewById(R.id.resetBtn);
        mRegisterTextConfirm = findViewById(R.id.register_text_confirm);
        resetBtn.getBackground().setAlpha(00);
        //mRegisterTextConfirm.getBackground().setAlpha(00);
        //设置控件初始状态（是否隐藏）
        mRegisterGestureChoice.setVisibility(View.INVISIBLE);
        mRegister_gesture.setVisibility(View.INVISIBLE);
        mRegisterTextConfirm.setVisibility(View.VISIBLE);
        //手势
        aCache = ACache.get(RegisterActivity.this);
        lockPatternView.setOnPatternListener(patternListener);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            //updateStatus(Status.DEFAULT, null);
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            //Log.e(TAG, "--onPatternDetected--");
            if(mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {

        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                mRegister_gesture_text.setText("再次绘制解锁图案");
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                mRegister_gesture_text.setText("至少连接4个点，请重新输入");
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }




    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
//        String password2 = bytes.toString();
//        SharedPreferences.Editor editor = getSharedPreferences("login",MODE_PRIVATE).edit();
//        editor.putString("gesturePassword",password2);
//        editor.apply();
        aCache.put(Constant.GESTURE_PASSWORD, bytes);
    }



    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }



}