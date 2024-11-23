package liu.yonglu.login_ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.security.auth.PrivateCredentialPermission;

import liu.yonglu.login_ui.util.ViewUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText number, password;
    private Button login, forget_password, number_login;
    private CheckBox remember_password;
    private Boolean remember = false;
    private String init_password = "111111";
    private int mRequestCode = 0; // 跳转页面时的请求代码
    private ActivityResultLauncher mLauncher; // 声明一个活动结果启动器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);
        remember_password = findViewById(R.id.remember_cb);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_number).setOnClickListener(this);
        findViewById(R.id.reset_pw).setOnClickListener(this);
        remember_password.setOnCheckedChangeListener(new CheckListener());
        number.addTextChangedListener(new HideTextWatcher(number, 11));
        password.addTextChangedListener(new HideTextWatcher(password, 6));

        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                Bundle bundle = new Bundle();
                bundle = result.getData().getExtras();
                String result_password = bundle.getString("new_password");
                init_password = result_password;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        password.setText("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus && v.getId() == R.id.number){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
        }else if(hasFocus && v.getId() == R.id.password){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private class CheckListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId() == R.id.remember_cb){
                remember = isChecked;
            }
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            super();
            mView = v;
            mMaxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if((str.length() == 11 && mMaxLength == 11) || (str.length() == 6 && mMaxLength == 6)){
                ViewUtil.hideOneInputMethod(LoginActivity.this, mView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String phone = number.getText().toString();
        if(v.getId() == R.id.login){
            if(phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.getText().toString().equals(init_password)){
                Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
            }else { //密码验证成功
                loginSuccess();
            }
        }else if(v.getId() == R.id.login_number){

        }else if(v.getId() == R.id.reset_pw){
            if(phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this,reset_password_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phone", phone);
            intent.putExtras(bundle);
            mLauncher.launch(intent);
        }
    }

    // 校验通过，登录成功
    private void loginSuccess() {
        String desc = String.format("您的手机号码是%s，恭喜你通过登录验证，点击“确定”按钮返回上个页面",
                number.getText().toString());
        // 以下弹出提醒对话框，提示用户登录成功
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("确定返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // 结束当前的活动页面
            }
        });
        builder.setNegativeButton("我再看看", null);
        AlertDialog alert = builder.create();
        alert.show(); // 显示提醒对话框
    }
}