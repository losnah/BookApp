package com.gkskfhdlstmapk.hanpinetree.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BookItemView extends LinearLayout {
    TextView tv_book;
    TextView tv_count;
    ToggleButton btn_heart;

    public BookItemView(Context context) {
        super(context);
        init(context);
    }

    public BookItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.book_item,this, true);
        tv_book = (TextView)findViewById(R.id.tv_book1);
        tv_count = (TextView)findViewById(R.id.tv_count);
        btn_heart = (ToggleButton)findViewById(R.id.btn_heart1);

        //모든 하트에 이벤트 적용합니당 : )
        btn_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_heart.isChecked() == false) btn_heart.setBackgroundResource(R.drawable.heart2);
                else   btn_heart.setBackgroundResource(R.drawable.heart1);
            }
        });

    }
    public void setText(String txt){
        tv_book.setText(txt);
    }

    public void setReadCount(int count){
        tv_count.setText(Integer.toString(count));
    }
}
