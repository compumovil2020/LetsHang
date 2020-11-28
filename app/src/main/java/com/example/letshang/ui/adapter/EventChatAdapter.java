package com.example.letshang.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.letshang.R;
import com.example.letshang.model.EventChat;

import java.util.List;

public class EventChatAdapter extends BaseAdapter {

    private Context context;
    private List<EventChat> eventChats;

    public EventChatAdapter (Context context, List<EventChat> eventChats){
        this.context = context;
        this.eventChats = eventChats;
    }

    @Override
    public int getCount() {
        return eventChats.size();
    }

    @Override
    public EventChat getItem(int i) {
        return eventChats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Verificar vista
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.from(this.context).inflate(R.layout.eventchatslayout,viewGroup,false);
        }

        return null;
    }
}
