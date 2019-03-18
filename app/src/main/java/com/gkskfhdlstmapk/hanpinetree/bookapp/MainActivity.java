package com.gkskfhdlstmapk.hanpinetree.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BookAdapter adapter;
    EditText editText;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        ListView listView = (ListView)findViewById(R.id.listView);
        mContext = getApplicationContext();

        adapter = new BookAdapter();


        try {
            //파일 있는지 확인들어갑니다~
            File f = new File(getFilesDir() + "test.txt");
            if(!f.isFile()) {
                //파일이 없으면 다음과 같이 실행해줍니다.
                BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "test.txt", true));
                PrintWriter pw = new PrintWriter(bw, true);
                pw.write(allTxt());
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "예외발생", Toast.LENGTH_LONG).show();
        }
        String alltxt = readTxt();

        String[] word = alltxt.split("@@@");


        for(int i = 0 ; i < word.length; i++){
            adapter.addItem(new BookItem(word[i],0));
        }


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookItem item = (BookItem)adapter.getItem(position);
                show(item.getTxt(), item);

            }
        });

        Button btn_plus = (Button)findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plus_content = editText.getText().toString();
                adapter.addItem(new BookItem(plus_content,0));

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "test.txt",true));
                    PrintWriter pw = new PrintWriter(bw,true);
                    pw.write(plus_content);
                    pw.close();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"1. 예외발생",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "test.txt",true));
                    PrintWriter pw = new PrintWriter(bw,true);
                    pw.write("@@@");
                    pw.close();
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"2. 예외발생",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private String allTxt(){
        String data = null;
        InputStream inputStream = getResources().openRawResource(R.raw.test);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                //if(i == 64) {byteArrayOutputStream.write('\n');byteArrayOutputStream.write('\n');}
                //else
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(),"MS949");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //editText로 반환되는 텍스트를 추가하는 함수입니다.
    private String readTxt() {
        String readStr = "";
        String str = null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"test.txt"));

            while ((str = br.readLine()) != null) readStr += str+"\n";
            br.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"파일이 없습니다.",Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"IO예외발생",Toast.LENGTH_SHORT).show();
        }
        return readStr;
    }

    //자세히보기 Dialog창 설정함수입니다.
    void show(String txt, final BookItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("자세히보기"); //타이틀설정
        builder.setMessage(txt); //내용설정

        builder.setPositiveButton("읽음",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        item.setReadCount(item.getReadCount()+1);
                        Toast.makeText(getApplicationContext(),"현재 Count : "+item.readCount,Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
                    }
                });

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"취소완료",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    ///////

    //BookAdater클래스입니다.
    class BookAdapter extends BaseAdapter{
        //어뎁터가 데이터를 관리하며 데이터를 넣었다가 뺄 수도 있으므로 ArrayList를 활용하여 구현해보자.

        ArrayList<BookItem> items = new ArrayList<BookItem>();
        //걸그룹의 이름, 전화번호 등등이 필요할텐데 이거 하나로는 부족하니까
        //데이터형을 다양하게 담고있는 java파일을 하나 더 만들어줄거에요

        //!! 그런데 ArrayList에 데이터를 넣는 기능이 지금 없으므로 함수를 하나 더 만들어줄게요
        public void addItem(BookItem item){
            items.add(item);
        }

        //너네 어뎁터 안에 몇 개의 아이템이 있니? 아이템갯수 반환함수
        @Override
        public int getCount() {
            return items.size(); //위의 ArrayList내부의 아이템이 몇 개나 들었는지 알려주게됨
        }

        @Override
        public Object getItem(int position) {
            return items.get(position); //position번째의 아이템을 얻을거야.
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BookItemView view = new BookItemView(getApplicationContext());
            BookItem item  = items.get(position);

            view.setText(item.getTxt());
            view.setReadCount(item.getReadCount());

            return view;
        }
    }
}
