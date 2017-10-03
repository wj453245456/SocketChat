package wj.com.socketchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class setActivity extends AppCompatActivity {
    String ip;
    int port;
    TextView ipshow;
    TextView portshow;
    EditText ipset;
    EditText portset;
    Button back;
    Button set;
    TextView title;
    TextView SocketChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        final LinearLayout setlay = (LinearLayout)findViewById(R.id.set_layout);
        portshow = (TextView)findViewById(R.id.show_port);
        ipshow = (TextView)findViewById(R.id.show_ip);
        portset = (EditText)findViewById(R.id.set_port);
        ipset = (EditText)findViewById(R.id.set_ip);
        Button deset = (Button)findViewById(R.id.set_default);
        final Button startset = (Button)findViewById(R.id.set_start);
        final Button endset = (Button)findViewById(R.id.set_end);
        back = (Button)findViewById(R.id.back_title);
        set = (Button)findViewById(R.id.set_titile);
        title = (TextView)findViewById(R.id.text_title);
        SocketChat = (TextView)findViewById(R.id.SockChat);
        title.setText("SocketChat\nSet");
        SocketChat.setVisibility(View.GONE);
        set.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //从sp读
        read();
        //开始设置
        startset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setlay.setVisibility(View.VISIBLE);
                startset.setVisibility(View.GONE);
            }
        });
        //恢复默认设置
        deset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                editor.putInt("port",6061);
                editor.putString("ip","192.168.1.133");
                editor.apply();
                read();
            }
        });
        //完成设置
        endset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setlay.setVisibility(View.GONE);
                startset.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                editor.putString("ip",ipset.getText().toString());
                editor.putInt("port",Integer.parseInt(portset.getText().toString()));
                editor.apply();
                read();
                InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
                }
            }
        });
    }
    public void read(){
        SharedPreferences preferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        ip=preferences.getString("ip","192.168.1.133");
        port=preferences.getInt("port",6061);
        ipshow.setText("当前指定服务器IP:"+ip);
        portshow.setText("当前指定服务器PORT:"+port);
        ipset.setText(ip);
        portset.setText(port+"");
    }
}
