package com.coded2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coded2.nabuwatercoach.R;

/**
 * Created by Rogerio on 20/05/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM =1;

    private int navTitles[];
    private int icons[];

    private String name;
    private String email;
    private int profile;

    Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int holderid;

        TextView textView;
        ImageView imageView;
        ImageView profileView;
        TextView name;
        TextView email;
        Context ctx;

        public ViewHolder(View itemView,int viewType,Context ctx) {
            super(itemView);

            if(viewType==TYPE_ITEM){
                itemView.setClickable(true);
                itemView.setOnClickListener(this);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderid = 1;
            }else{
                name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profileView = (ImageView) itemView.findViewById(R.id.circleView);
                holderid = 0;
            }
            this.ctx = ctx;
        }

        @Override
        public void onClick(View v) {
           // Toast.makeText(ctx,"Clicado: "+getLayoutPosition(),Toast.LENGTH_SHORT).show();
        }
    }



        DrawerAdapter(int Titles[], int Icons[], String Name, String Email, int Profile, Context context){
            navTitles = Titles;//have seen earlier
            icons = Icons;
            name = Name;
            email = Email;
            profile = Profile;
            this.context = context;
        }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType,context);

            return vhItem;

        } else if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
                ViewHolder vhHeader = new ViewHolder(v,viewType,context);
                return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.holderid ==1) {
            holder.textView.setText(navTitles[position - 1]);
            holder.imageView.setImageResource(icons[position -1]);
            }
        else{
            holder.profileView.setImageResource(profile);
            holder.name.setText(name);
            holder.email.setText(email);
            }
    }


    @Override
    public int getItemCount() {
        return navTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
        return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
