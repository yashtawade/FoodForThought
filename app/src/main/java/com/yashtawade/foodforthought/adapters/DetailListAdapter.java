package com.yashtawade.foodforthought.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.activities.CommentListActivity;
import com.yashtawade.foodforthought.activities.CommentWriteActivity;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;
import com.yashtawade.foodforthought.models.Recipe;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

public class DetailListAdapter extends RecyclerView.Adapter{
    public final static int ingredientHeadType = 1;
    public final static int ingredientItemType = 2;
    public final static int instructionHeadType = 3;
    public final static int instructionItemType = 4;
    public final static int commentButtonType = 5;

    //todo: get uid from cookie
    private int uid = 1;

    private Recipe recipe;
    private List<String> instructionList;
    public int countLike;
    public boolean isLiked;
    private Context mContext;

    public DetailListAdapter(Context mContext, Recipe recipe, List<String> instructionList, int countLike, boolean isLiked)
    {
        super();
        this.mContext = mContext;
        this.recipe = recipe;
        this.instructionList=instructionList;
        this.countLike = countLike;
        this.isLiked = isLiked;
    }

    @Override
    public int getItemCount()
    {
        int count = recipe.getExtendedIngredients().size() + instructionList.size() + 3;
        return count;
    }


    @Override
    public int getItemViewType(int position){

        if(position == 0){
            return ingredientHeadType;
        }

        if(position <= recipe.getExtendedIngredients().size()){
            return ingredientItemType;
        }

        if(position == recipe.getExtendedIngredients().size()+1){
            return instructionHeadType;
        }

        if(position <= recipe.getExtendedIngredients().size() + instructionList.size() + 1){
            return instructionItemType;
        }

        return commentButtonType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        View view;

        switch(type){
            case ingredientHeadType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_ingredient_head,null);
                view.setLayoutParams(lp);
                return new IngredientHeadViewHolder(view);
            case ingredientItemType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_ingredient,null);
                view.setLayoutParams(lp);
                return new IngredientItemViewHolder(view);
            case instructionHeadType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_instruction_head,null);
                view.setLayoutParams(lp);
                return new InstructionHeadViewHolder(view);
            case instructionItemType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_instruction,null);
                view.setLayoutParams(lp);
                return new InstructionItemViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_comment_buttons,null);
                view.setLayoutParams(lp);
                return new CommentButtonsItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position){

        if(viewHolder instanceof IngredientHeadViewHolder){
            Picasso.with(mContext).load(recipe.getImage()).into(((IngredientHeadViewHolder) viewHolder).imageView);
            ((IngredientHeadViewHolder) viewHolder).title.setText(recipe.getTitle());
            if(countLike != 0){
                ((IngredientHeadViewHolder) viewHolder).likeNumberView.setText(countLike + "");
            }

            if(isLiked){
                ((IngredientHeadViewHolder) viewHolder).likeButton.setColorFilter(Color.parseColor("#D91517"));
            }else{
                ((IngredientHeadViewHolder) viewHolder).likeButton.setColorFilter(Color.parseColor("#3b5998"));
            }

            ((IngredientHeadViewHolder) viewHolder).likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){
                        @Override
                        public void onResponse(Response httpResponse, String response, Headers headers) {
                            DataParse dp = JSON.parseObject(response, DataParse.class);
                            if(dp.getError() == 0){
                                if(isLiked){
                                    ((IngredientHeadViewHolder) viewHolder).likeButton.setColorFilter(Color.parseColor("#3b5998"));
                                    ((IngredientHeadViewHolder) viewHolder).likeNumberView.setText(--countLike + "");
                                    isLiked = false;
                                }else{
                                    ((IngredientHeadViewHolder) viewHolder).likeButton.setColorFilter(Color.parseColor("#D91517"));
                                    ((IngredientHeadViewHolder) viewHolder).likeNumberView.setText(++countLike + "");
                                    isLiked = true;
                                }
                            }
                        }
                    };

                    String url = FFTConstant.LONG_BASE_URL + "recipe/like/toggle";
                    RequestParams params = new RequestParams();
                    params.addFormDataPart("rid", recipe.getId());
                    params.addFormDataPart("uid", uid);
                    Http httpRequest = new Http();
                    httpRequest.post(url, params, mCallback);

                }
            });
        }

        if(viewHolder instanceof IngredientItemViewHolder){
            position--;

            ((IngredientItemViewHolder) viewHolder).ingredientName.setText(
                    recipe.getExtendedIngredients().get(position).getName());
            ((IngredientItemViewHolder) viewHolder).ingredientNum.setText(
                    recipe.getExtendedIngredients().get(position).getAmount() + "  " + recipe.getExtendedIngredients().get(position).getUnit());

        }

        if(viewHolder instanceof InstructionItemViewHolder){
            position = position - 2 - recipe.getExtendedIngredients().size();

            ((InstructionItemViewHolder) viewHolder).step.setText(
                    instructionList.get(position)
            );
        }

        if(viewHolder instanceof CommentButtonsItemViewHolder){

            ((CommentButtonsItemViewHolder) viewHolder).write_comment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = CommentWriteActivity.newIntent(mContext, recipe.getId());
                    mContext.startActivity(i);
                }
            });

            ((CommentButtonsItemViewHolder) viewHolder).view_comment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = CommentListActivity.newIntent(mContext, recipe.getId());
                    mContext.startActivity(i);
                }
            });
        }

    }


    /**
     *layout helper
     */
    class IngredientHeadViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView title;
        ImageButton likeButton;
        TextView likeNumberView;

        public IngredientHeadViewHolder(View view){
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            title = (TextView) view.findViewById(R.id.title);
            likeButton = (ImageButton) view.findViewById(R.id.like_button);
            likeNumberView = (TextView) view.findViewById(R.id.like_number_text_view);
        }
    }

    class IngredientItemViewHolder extends RecyclerView.ViewHolder {
        /**
         * ingredient name
         */
         TextView ingredientName;
         TextView ingredientNum;

        public IngredientItemViewHolder(View view) {
            super(view);

            ingredientName = (TextView) view.findViewById(R.id.ingredient_name);
            ingredientNum = (TextView) view.findViewById(R.id.ingredient_num);
        }
    }

    class InstructionHeadViewHolder extends RecyclerView.ViewHolder
    {
        public InstructionHeadViewHolder(View view) {
            super(view);
        }
    }

    class InstructionItemViewHolder extends RecyclerView.ViewHolder
    {
        /**
         * instruction content
         */
         TextView step;

        public InstructionItemViewHolder(View view) {
            super(view);

            step = (TextView) view.findViewById(R.id.step_content);
        }
    }

    class CommentButtonsItemViewHolder extends RecyclerView.ViewHolder{

        Button write_comment;
        Button view_comment;

        public CommentButtonsItemViewHolder(View view) {
            super(view);

            write_comment = (Button) view.findViewById(R.id.button_write_comment);
            view_comment = (Button) view.findViewById(R.id.button_view_comment);

        }
    }

}
