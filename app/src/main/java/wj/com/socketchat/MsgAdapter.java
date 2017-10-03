package wj.com.socketchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wj on 2017/9/29.
 */

public class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceID;
    private LinearLayout left_layout;
    private TextView left_msg;
    private LinearLayout right_layout;
    private TextView right_msg;
    private TextView sys_msg;
    private TextView r_i;
    private TextView l_i;
    public MsgAdapter(Context context, int resource, List<Msg> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        View view;
        if (convertView!=null){
            view = convertView;
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        }
        left_layout = (LinearLayout) view.findViewById(R.id.left_layout);
        right_layout = (LinearLayout) view.findViewById(R.id.right_layout);
        left_msg = (TextView) view.findViewById(R.id.left_msg);
        right_msg = (TextView) view.findViewById(R.id.right_msg);
        sys_msg = (TextView) view.findViewById(R.id.sys_msg);
        r_i = (TextView) view.findViewById(R.id.right_ip);
        l_i = (TextView) view.findViewById(R.id.left_ip);
        if (msg.getType()==1){
            right_layout.setVisibility(View.GONE);
            r_i.setVisibility(View.GONE);
            left_layout.setVisibility(View.VISIBLE);
            l_i.setVisibility(View.VISIBLE);
            l_i.setText(msg.getIp());
            sys_msg.setVisibility(View.GONE);
            left_msg.setText(msg.getMsg());
        }else if(msg.getType()==0){
            right_layout.setVisibility(View.VISIBLE);
            r_i.setVisibility(View.VISIBLE);
            r_i.setText(msg.getIp());
            left_layout.setVisibility(View.GONE);
            l_i.setVisibility(View.GONE);
            sys_msg.setVisibility(View.GONE);
            right_msg.setText(msg.getMsg());
        }else {
            l_i.setVisibility(View.GONE);
            r_i.setVisibility(View.GONE);
            right_layout.setVisibility(View.GONE);
            left_layout.setVisibility(View.GONE);
            sys_msg.setVisibility(View.VISIBLE);
            sys_msg.setText(msg.getMsg());
        }
        return view;
    }
}
