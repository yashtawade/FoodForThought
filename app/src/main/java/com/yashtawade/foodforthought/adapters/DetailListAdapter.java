package com.yashtawade.foodforthought.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.models.Recipe;

import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter{
    public final static int ingredientHeadType = 1;
    public final static int ingredientItemType = 2;
    public final static int instructionHeadType = 3;
    public final static int instructionItemType = 4;


    private Recipe recipe;
    private List<String> instructionList;

    private Context mContext;

    public DetailListAdapter(Context mContext, Recipe recipe, List<String> instructionList)
    {
        super();
        this.mContext = mContext;
        this.recipe = recipe;
        this.instructionList=instructionList;

    }

    @Override
    public int getItemCount()
    {
        int count = recipe.getExtendedIngredients().size() + instructionList.size() + 2;
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

        return instructionItemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        View view;

        switch(type){
            case ingredientHeadType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_ingredient_head,null);
                view.setLayoutParams(lp);
                return new IngregientHeadViewHolder(view);
            case ingredientItemType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_ingredient,null);
                view.setLayoutParams(lp);
                return new IngredientItemViewHolder(view);
            case instructionHeadType:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_instruction_head,null);
                view.setLayoutParams(lp);
                return new InstructionHeadViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_instruction,null);
                view.setLayoutParams(lp);
                return new InstructionItemViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        if(viewHolder instanceof IngregientHeadViewHolder){
            Picasso.with(mContext).load(recipe.getImage()).into(((IngregientHeadViewHolder) viewHolder).imageView);
            ((IngregientHeadViewHolder) viewHolder).title.setText(recipe.getTitle());
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
    }


    /**
     *layout helper
     */
    class IngregientHeadViewHolder extends RecyclerView.ViewHolder
    {
         ImageView imageView;
         TextView title;

        public IngregientHeadViewHolder(View view){
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            title = (TextView) view.findViewById(R.id.title);
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
         * ingredient name
         */
         TextView step;

        public InstructionItemViewHolder(View view) {
            super(view);

            step = (TextView) view.findViewById(R.id.step_content);
        }
    }

}
