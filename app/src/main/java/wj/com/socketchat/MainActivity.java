package wj.com.socketchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String ServerIp;
    private int ServerPort;
    private Socket socket = null;
    PrintWriter pw = null;
    BufferedReader re = null;
    String socketmsg = null;

    private List<Msg> msgs = new ArrayList<>();
    private ListView show;
    MsgAdapter adapter;
    private EditText edit;
    private Button send;
    TextView title;
    Button set;

    @Override
    protected void onResume() {
        super.onResume();
        String ip = ServerIp;
        int port = ServerPort;
        getIPandPort();
        if (ip!=ServerIp||port!=ServerPort){
            reconn();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        set = (Button)findViewById(R.id.set_titile);
        title = (TextView)findViewById(R.id.text_title);
        show = (ListView)findViewById(R.id.show);
        edit = (EditText)findViewById(R.id.edit);
        send = (Button)findViewById(R.id.send);
        adapter = new MsgAdapter(this,R.layout.item,msgs);
        show.setAdapter(adapter);
        if (socket==null||socket.isClosed()||!socket.isConnected()){
            getIPandPort();
            creatSock();
            Log.d("aaaaaaa", "onCreate: 1111111111");
        }else {
            Log.d("aaaaaaa", "onCreate: 122222222222");
            heart();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送button监听
                if(sendMsg(edit.getText().toString())){
                    edit.setText("");
                }
            }
        });
        //输入法不自动弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,setActivity.class);
                startActivity(i);
            }
        });
    }
    public void showReconn(){
        Snackbar.make(getWindow().getDecorView(),"无法连接服务器...",Snackbar.LENGTH_INDEFINITE)
                .setAction("重连", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIPandPort();
                        creatSock();
                    }
                }).show();
    }
    //关闭连接
//    public void clconn(){
//        if (socket!=null){
//            try{
//                Log.d("aaaaaa", "clconn: 111111111111111111111111");
//                re.close();
//                pw.close();
//                socket.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
    //读取ip与Port
    public void getIPandPort(){
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        ServerIp = preferences.getString("ip","192.168.1.133");
        ServerPort = preferences.getInt("port",6066);
        title.setText(ServerIp+"\n"+ServerPort);
    }
    //心跳检测
//    public void heartconn(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//               while (true){heart();
//                   try{
//
//                    //   Thread.sleep(5000);
//                   }catch (Exception e){
//                       e.printStackTrace();
//                   }
//               }
//            }
//        }).start();
//    }
    public void heart(){
        if (socket!=null){
            try{
                socket.sendUrgentData(0xff);
            }catch (Exception e){
                e.printStackTrace();
                reconn();
            }
        }else {
            reconn();
        }
    }
    //建立连接
    public void creatSock(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
//                    if (socket!=null){
//                       clconn();
//                    }
                    InetAddress inetAddress = InetAddress.getByName(ServerIp);
                    socket = new Socket(inetAddress, ServerPort);
                    AcceptMsg();
                }catch (Exception e) {
                    e.printStackTrace();
                    reconn();
                    Log.d("aaaa", "run: 连接失败");
                }
            }
        }).start();
    }
    public void reconn(){
        sendMessenger(new Msg("无法连接服务器...请重设PORT或IP",2,"sys"));
        showReconn();
    }
    public  void sendMessenger(final Msg msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgs.add(msg);
                adapter.notifyDataSetChanged();
            }
        });
    }
    //发送
    private boolean sendMsg(final String msg){
        if(socket!=null&&socket.isConnected()){
            if (!msg.equals("")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                            pw.println(msg);
                            if (!msg.equals("EndEndClosethesocket")){
                                sendMessenger(new Msg(msg,0,"Me"));
                            }
                            Log.d("XXXXXXX", "发送成功");
                        }catch (Exception e) {
                            heart();
                        }


                    }
                }).start();
                return true;
            }
        }
        return false;
    }
    //接受
    public void AcceptMsg(){
        if (socket.isConnected()&&!socket.isClosed()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        re = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while((socketmsg=re.readLine())!=null){
                            String[] msg = socketmsg.split(":##:");
                            Msg m;
                            if(msg[0].equals("sys")){
                                m = new Msg(msg[1],2,msg[0]);
                            }else{
                                Log.d("sxxxxx", "run: "+msg[0]);
                                String ip = msg[0].substring(msg[0].length()-3,msg[0].length());
                                m = new Msg(msg[1],1,ip);
                            }
                            sendMessenger(m);
                            Log.d("xxxxxxxxx", "接受成功"+socketmsg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        heart();
                        Log.d("aaaa", "run: 接受失败");
                    }
                }
            }).start();
        }
        heart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendMsg("EndEndClosethesocket");
        Log.d("aaaa", "onStop: 111111111111111111111111");
       // clconn();
    }
}
