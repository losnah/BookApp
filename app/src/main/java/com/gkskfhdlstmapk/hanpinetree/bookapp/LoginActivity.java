package com.gkskfhdlstmapk.hanpinetree.bookapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText editTextID;
    EditText editTextPW;
    Button buttonLogin;

    String ID;
    String PW;
    //Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = editTextID.getText().toString();
                PW = editTextPW.getText().toString();
                //Toast.makeText(mContext,ID+" "+PW,Toast.LENGTH_SHORT).show();

                if(ID.equals("a") && PW.equals("b")){
                    Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });



    }

    public void init(){
        editTextID = (EditText)findViewById(R.id.editTextID);
        editTextPW = (EditText)findViewById(R.id.editTextPW);
        buttonLogin = (Button)findViewById(R.id.activity_login_login_button);
        //mContext = getApplicationContext();
    }
}