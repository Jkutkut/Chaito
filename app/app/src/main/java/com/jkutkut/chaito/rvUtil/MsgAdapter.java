package com.jkutkut.chaito.rvUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkutkut.chaito.R;
import com.jkutkut.chaito.model.Msg;

import java.util.ArrayList;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {
    private final ArrayList<Msg> msgs;

    public MsgAdapter(ArrayList<Msg> msgs) {
        this.msgs = msgs;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_msg, parent, false);
        return new MsgViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        Msg msg = msgs.get(position);
        holder.bind(msg, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder {
        public TextView txtvUsername;
        public TextView txtvMsgType;
        public TextView txtvMsg;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvUsername = itemView.findViewById(R.id.txtvUsername);
            txtvMsgType = itemView.findViewById(R.id.txtvMsgType);
            txtvMsg = itemView.findViewById(R.id.txtvMsg);
        }

        public void bind(Msg msg, Context context) {
            txtvUsername.setText(msg.getSender());
            if (msg.getTarget().equals(Msg.ALL_TARGET)) {
                txtvMsgType.setText(context.getString(R.string.says));
            } else {
                txtvMsgType.setText(context.getString(R.string.whispers));
            }
            txtvMsg.setText(msg.getMsg());
        }
    }
}