package com.yashtawade.foodforthought.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.models.Comment;

import java.util.*;

public class CommentListAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private Context context;
    private List<Comment> items;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.items = commentList;
        inflater = LayoutInflater.from(context);
    }

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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_comment, parent, false);

            viewHolder.username = (TextView) convertView.findViewById(R.id.comment_username_textView);
            viewHolder.date = (TextView) convertView.findViewById(R.id.comment_date_textView);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_comment_textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(items.get(position).getUser());
        viewHolder.date.setText(items.get(position).getDate());
        viewHolder.comment.setText(items.get(position).getComment());
        return convertView;
    }

    public void onDateChange(List<Comment> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public class ViewHolder {
        //username
        TextView username;
        //comment date
        TextView date;
        //comment
        TextView comment;
    }

}
