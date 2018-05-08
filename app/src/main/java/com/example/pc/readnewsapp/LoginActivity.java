package com.example.pc.readnewsapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button buttonLogin, buttonRegister, buttonExit, buttonLoginFB;
    //private String username, password;

    FirebaseAuth mAuthencation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuthencation = FirebaseAuth.getInstance();
        map();
        controlButton();
//        LoginFB();

    }



    private void controlButton() {



        //tạo sự kiện cho nút Exit
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tạo hộp thoại, nó khác Dialog ở chỗ nó đã có layout mình chỉ cần lấy ra để sử dụng, còn Dialod thì mình phải tạo ra
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog);

                builder.setTitle("Are you sure exit?");
                builder.setMessage("Please choose");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();//hàm giúp cho việc thoát app
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();//để show ra hộp thoại mà ta đã tạo
            }
        });

        buttonLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FbActivity.class);
                startActivity(intent);
            }
        });

        //tạo sự kiện cho nút Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(LoginActivity.this);//cho nó màn hình đứng, ko thêm theme

                dialog.setTitle("Dialog box process");
                dialog.setCancelable(false);//có click ra ngoài hộp thoại thì vẫn ko tắt hộp thoại đi, nếu ko set cho nó thì nó mặc định là bị tắt
                dialog.setContentView(R.layout.register_dialog);//set view để tìm tới cái layout mà mình đã tạo tương úng với cái cần

                final EditText edtRegisterUsername = (EditText) dialog.findViewById(R.id.etRegisterUsername);
                final EditText edtRegisterPassword = (EditText) dialog.findViewById(R.id.etRegisterPassword);

                Button buttonCancel = (Button) dialog.findViewById(R.id.btnCancel);
                Button buttonCreate = (Button) dialog.findViewById(R.id.btnCreate);

                buttonCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final ProgressDialog myPd_ring=ProgressDialog.show(LoginActivity.this, "Please wait", "Loading please wait..", false);

                        myPd_ring.setCancelable(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try
                                {
                                    Thread.sleep(5000);
                                }catch(Exception e){}
                                myPd_ring.dismiss();
                            }
                        }).start();

                        final String username = edtRegisterUsername.getText().toString();
                        final String password =  edtRegisterPassword.getText().toString();
                        mAuthencation.createUserWithEmailAndPassword(username,password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                           edtUsername.setText(username);
                                           edtPassword.setText(password);
                                           dialog.cancel();
                                        } else {
                                            Toast.makeText(LoginActivity.this,"Your register failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });



        //tạo sự kiện cho nút Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra xem đã nhập tên và pass hay chưa
                if(edtUsername.getText().length() != 0 && edtPassword.getText().length() != 0) {

                    Login();

                } else {
                    Toast.makeText(LoginActivity.this,"Please enter enough information",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void LoginFB() {
//        buttonLoginFB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, FbActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    private void Login(){
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        mAuthencation.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,"You are successful login",Toast.LENGTH_SHORT).show();//thời gian để nó hiển thị

                        //hàm chuyển qua màn hình khác
                        //gọi lại màn hình đang đứng và màn hính mình muốn truyền qua
                        Intent intent = new Intent(LoginActivity.this,FinishLoginActivity.class);

                        //câu lệnh chuyển màn hình
                        startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,"Your login failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void map() {
        edtUsername = (EditText) findViewById(R.id.etUsername);
        edtPassword = (EditText) findViewById(R.id.etPassword);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonRegister = (Button) findViewById(R.id.btnRegister);
        buttonExit = (Button) findViewById(R.id.btnExit);
        buttonLoginFB = (Button) findViewById(R.id.btnLoginFB);

    }
}
