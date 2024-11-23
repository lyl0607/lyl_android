package liu.yonglu.login_ui;

import android.content.Intent;
import android.icu.text.DateIntervalFormat;
import android.os.Bundle;
import android.print.PrinterCapabilitiesInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class reset_password_Activity extends AppCompatActivity implements View.OnClickListener{
    private EditText new_password, again_new_password, verification_code;
    private Button get_verification_code, confirm;
    private String mPhone, mVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new_password = findViewById(R.id.new_password);
        again_new_password = findViewById(R.id.again_new_password);
        verification_code = findViewById(R.id.verification_code);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.get_verification_code).setOnClickListener(this);
        mPhone = getIntent().getStringExtra("phone");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.get_verification_code){
            if(mPhone== null || mPhone.length() < 11){
                Toast.makeText(reset_password_Activity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                return;
            }
            mVerifyCode = String.format("%06d", new Random().nextInt(999999));
            AlertDialog.Builder builder = new AlertDialog.Builder(reset_password_Activity.this);
            builder.setTitle("请记住验证码");
            builder.setMessage("手机号" + mPhone + "本次验证码是" +mVerifyCode + ",请输入验证码");
            builder.setPositiveButton("好的", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(v.getId() == R.id.confirm){
            String password_first = new_password.getText().toString();
            String password_second = again_new_password.getText().toString();
            if(password_first.length() < 6 || password_second.length() < 6){
                Toast.makeText(reset_password_Activity.this,"密码长度至少为6位",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password_first.equals(password_second)){
                Toast.makeText(reset_password_Activity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!mVerifyCode.equals(verification_code.getText().toString())){
                Toast.makeText(reset_password_Activity.this,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("new_password", password_first);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}