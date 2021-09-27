package com.example.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthcalendar.R;

import java.util.ArrayList;

public class PostAdapter3 extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Post> data; //Item 목록을 담을 배열
    private int layout;

    public PostAdapter3(Context context, int layout, ArrayList<Post> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    public void addData(Post d){
        data.add(d);
    }


    @Override
    public int getCount() { //리스트 안 Item의 개수를 센다.
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        Post post = data.get(position);


        //이름 등 정보 연동
        TextView info = (TextView) convertView.findViewById(R.id.title_);
        info.setText(post.getTitle());

        //전화번호 연동
        TextView phone = (TextView) convertView.findViewById(R.id.category_);
        phone.setText(post.getCategory());

//        TextView postNum = (TextView) convertView.findViewById(R.id.postNum_);
//        postNum.setText(post.getPostNum()+"");


        return convertView;
    }



}
