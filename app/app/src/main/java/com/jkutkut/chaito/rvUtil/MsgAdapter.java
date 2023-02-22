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
    private static final int RECEIVED_MSG = 0;
    private static final int SENT_MSG = 1;

    private final ArrayList<Msg> msgs;
    private final String user;

    public MsgAdapter(String user, ArrayList<Msg> msgs) {
        this.user = user;
        this.msgs = msgs;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = viewType == SENT_MSG ? R.layout.fragment_msg_sent : R.layout.fragment_msg_received;
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MsgViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        Msg msg = msgs.get(position);
        holder.bind(user, msg, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public int getItemViewType(int position) {
        Msg msg = msgs.get(position);
        if (msg.getSender().equals(user)) {
            return SENT_MSG;
        } else {
            return RECEIVED_MSG;
        }
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

        public void bind(String user, Msg msg, Context context) {
            txtvUsername.setText(msg.getSender());
            if (msg.getSender().equals(user)) {
                txtvUsername.setText(context.getString(R.string.you));
            }

            if (msg.getTarget().equals(Msg.ALL_TARGET)) { // Someone says to all
                txtvMsgType.setText(context.getString(R.string.says));
                if (msg.getSender().equals(user))
                    txtvMsgType.setText(context.getString(R.string.you_say));
            }
            else if (msg.getTarget().equals(user)) { // Someone to me
                txtvMsgType.setText(String.format(
                    context.getString(R.string.whispers),
                    context.getString(R.string.you).toLowerCase()
                ));
            }
            else { // Someone or me to someone
                txtvMsgType.setText(String.format(
                    context.getString(R.string.you_whisper),
                    msg.getTarget()
                ));
            }
            txtvMsg.setText(msg.getMsg());
        }
    }
}