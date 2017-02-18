package com.hrc.administrator.zuoye;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button download,reset;
    private EditText adress;
    private ImageView image;
    private Bitmap bitmap;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Download();
                    break;
                case 2:
                    image.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download=(Button)findViewById(R.id.download);
        reset=(Button)findViewById(R.id.reset);
        adress=(EditText)findViewById(R.id.acress);
        image=(ImageView)findViewById(R.id.picture);
        download.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.download:
                String url=adress.getText().toString();
                if(URLUtil.isNetworkUrl(url)){
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }else{
                    Toast.makeText(this,"网址格式错误", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.reset:
                adress.setText("");
                break;
        }
    }

    private void Download(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    URL url=new URL(adress.getText().toString());
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8*1000);
                    InputStream in=connection.getInputStream();
                    bitmap= BitmapFactory.decodeStream(in);
                    in.close();
                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
