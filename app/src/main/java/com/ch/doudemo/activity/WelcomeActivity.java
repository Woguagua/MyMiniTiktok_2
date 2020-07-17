package com.ch.doudemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.doudemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {


    private Button btn;
    private EditText edtex_user_account;
    private EditText edtex_user_password;
//    private Switch switch_showpassword;
//
    private String account = "Wogua";
    private String password = "1234567";
//    private String usrtype = "新用户";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Button btn = findViewById(R.id.button1);
        EditText edtex_user_account = findViewById(R.id.edtex_user_account);
        EditText edtex_user_password = findViewById(R.id.edtex_user_password);
//        Switch switch_showpassword = findViewById(R.id.switch_showpassword);



//        final String UsrAccount = edtex_user_account.getText().toString();
//
//
//        final TextView tv = findViewById(R.id.tv_title);
//
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tv.setText(UsrAccount+"登陆成功");
                if(edtex_user_account.getText().toString().equals(account)){
                    if(edtex_user_password.getText().toString().equals(password)){
                        Intent intent = new Intent(WelcomeActivity.this, Page2Activity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast toast = Toast.makeText(WelcomeActivity.this, "密码不正确，请重试",
                                Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 100);
                        toast.show();
                    }

                }
                else{
                    Toast toast = Toast.makeText(WelcomeActivity.this, "该用户未注册，请重试",
                            Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 100);
                    toast.show();

                }
                Log.i("111111", "Button Down");
                Log.i("111111",edtex_user_account.getText().toString());
                Log.i("111111",edtex_user_password.getText().toString());
            }

        });



//        edtex_user_account.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("4444444", "输入文字中的状态，count是输入字符数");
//                Log.i("4444444", editText.getText().toSrting());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                Log.i("4444444", "输入文本之前的状态");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.i("4444444", "输入文字后的状态");
//            }
//        });







    }
}
