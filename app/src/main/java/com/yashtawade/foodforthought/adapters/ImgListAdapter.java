package com.yashtawade.foodforthought.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.models.Recipe;

import java.util.List;

public class ImgListAdapter extends BaseAdapter
{
    /**
     * data source
     */
    private List<Recipe> dataList;

    private Context mContext;

    public ImgListAdapter(Context mContext)
    {
        super();
        this.mContext = mContext;
    }

    /**
     * set data source
     */
    public void setDataSource(List<Recipe> dataList)
    {
        if (null != dataList)
        {
            this.dataList = dataList;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount()
    {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return (null != dataList && !dataList.isEmpty()) ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return dataList == null ? 0 : position;
    }

    ViewHolder viewHolder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.content_search_result, parent, false);

            viewHolder.mImageView = (ImageView)convertView.findViewById(R.id.recipe_result_item_img);
            viewHolder.mtextName = (TextView)convertView.findViewById(R.id.recipe_result_item_text);
            viewHolder.suggest = (TextView)convertView.findViewById(R.id.suggest);
            convertView.setTag(viewHolder);
//            viewHolder.textInfo = (TextView)convertView.findViewById(R.id.item_info);
//            viewHolder.subscribe = (TextView)convertView.findViewById(R.id.item_un_subscribe);

        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(position < 14){
            viewHolder.suggest.setVisibility(View.INVISIBLE);
            viewHolder.mtextName.setText((CharSequence)dataList.get(position).getTitle());
        }else{
            viewHolder.suggest.setVisibility(View.VISIBLE);
            viewHolder.mtextName.setText((CharSequence)dataList.get(position).getTitle());
        }

        Picasso.with(mContext).load(dataList.get(position).getImage()).into(viewHolder.mImageView);

//        //listener
//        viewHolder.subscribe.setOnClickListener(new MyListener(position));

//        if ((boolean)dataList.get(position).get(MainActivity.KEY[3]))
//        {
//            viewHolder.subscribe.setText("cancel");
//        }
//        else
//        {
//            viewHolder.subscribe.setText("subscribe");
//        }

        return convertView;
    }

//    /**
//     * implement listener
//     */
//    private class MyListener implements OnClickListener
//    {
//        int mPosition;
//
//        public MyListener(int inPosition)
//        {
//            mPosition = inPosition;
//        }
//
//        @Override
//        public void onClick(View v)
//        {
//            //in case of disorder
//            if(v.getId() == viewHolder.subscribe.getId())
//            {
//                //status change
//                dataList.get(mPosition).put(MainActivity.KEY[3], !((boolean)dataList.get(mPosition).get(MainActivity.KEY[3])));
//                MyListAdapter.this.notifyDataSetChanged();
//            }
//        }
//
//    }


    /**
     *layout helper
     */
    static class ViewHolder
    {
        /**
         * recipe image
         */
        private ImageView mImageView;

        /**
         * recipe name
         */
        private TextView mtextName;

        private TextView suggest;

//        /**
//         * information
//         */
//        private TextView textInfo;
//
//        /**
//         * subscribe
//         */
//        private TextView subscribe;
    }

}