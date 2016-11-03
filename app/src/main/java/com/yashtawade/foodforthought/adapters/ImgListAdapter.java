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

            viewHolder.recipeImage = (ImageView)convertView.findViewById(R.id.recipe_result_item_img);
            viewHolder.recipeName = (TextView)convertView.findViewById(R.id.recipe_result_item_text);
            viewHolder.mSuggest = (TextView)convertView.findViewById(R.id.suggest);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(dataList.get(position).getMissedIngredientCount() == 0){
            viewHolder.mSuggest.setVisibility(View.INVISIBLE);
            viewHolder.recipeName.setText((CharSequence)dataList.get(position).getTitle());
        }else{
            viewHolder.mSuggest.setVisibility(View.VISIBLE);
            viewHolder.recipeName.setText((CharSequence)dataList.get(position).getTitle());
        }

        Picasso.with(mContext).load(dataList.get(position).getImage()).into(viewHolder.recipeImage);

        return convertView;
    }


    /**
     *layout helper
     */
    static class ViewHolder
    {
        /**
         * recipe image
         */
        private ImageView recipeImage;

        /**
         * recipe name
         */
        private TextView recipeName;

        /**
         * recipe mSuggest icon
         */
        private TextView mSuggest;

    }

}