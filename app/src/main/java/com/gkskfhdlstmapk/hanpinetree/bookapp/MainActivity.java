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
import android.widget.TextView;
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
import java.util.function.Predicate;

public class MainActivity extends AppCompatActivity {
    BookAdapter adapter;
    EditText editText;
    Context mContext;
    Button mButtonPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        ListView listView = (ListView)findViewById(R.id.listView);
        mButtonPlus = (Button)findViewById(R.id.activity_main_add_button);
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
            Toast.makeText(getApplicationContext(), "예외발생 ", Toast.LENGTH_LONG).show();
        }
        String alltxt = readTxt();

        String[] word = alltxt.split("@@@");


        for(int i = 0 ; i < word.length; i++){
            adapter.addItem(new BookItem(word[i],0));
        }
        listView.setAdapter(adapter);

        //alert dialog 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookItem item = (BookItem)adapter.getItem(position);
                show(item.getTxt(), item);

            }
        });
        Button btn_plus = (Button)findViewById(R.id.btn_plus);
        //검색기능 추가합니다.
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 텍스트 변경 및 검색 기능
                if(btn_plus.getText().equals("검색")){
                    btn_plus.setText("취소");
                    //검색하기
                    String searchContent = editText.getText().toString();
                    adapter.searchItem(searchContent);
                    adapter.notifyDataSetChanged();
                }
                else{
                    btn_plus.setText("검색");
                    adapter.clearItem();
                    String alltxt = readTxt();
                    String[] word = alltxt.split("@@@");
                    for(int i = 0 ; i < word.length; i++){
                        adapter.addItem(new BookItem(word[i],0));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //앱테스트용 데이터추가기능
        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText plus = (EditText)findViewById(R.id.activity_main_edittext_enroll);
                String plus_content = plus.getText().toString();

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
//내부 local저장소로부터 모든 텍스트 읽어오기
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

    //BookAdater클래스입니다.
    class BookAdapter extends BaseAdapter {

        ArrayList<BookItem> items = new ArrayList<>();
        public void addItem(BookItem item){
            items.add(item);
        }

        public void searchItem(String content) {
            items.removeIf(new Predicate<BookItem>() {
                @Override
                public boolean test(BookItem bookItem) {
                    return bookItem.txt.contains(content) == false;
                }
            });
        }

        public void clearItem(){ items.clear();}
        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
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
