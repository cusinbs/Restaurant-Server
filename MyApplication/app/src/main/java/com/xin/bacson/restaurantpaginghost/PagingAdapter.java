package com.xin.bacson.restaurantpaginghost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Welcomes on 8/14/2017.
 */

public class PagingAdapter extends BaseAdapter{
    private MainActivity context;
    private int layout;
    private List<Paging> pagingList;

    public PagingAdapter(MainActivity context, int layout, List<Paging> pagingList) {
        this.context = context;
        this.layout = layout;
        this.pagingList = pagingList;
    }

    @Override
    public int getCount() {
        return pagingList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtName, txtNum, txtReady;
        ImageView imgDelete, imgEdit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtName          = (TextView) convertView.findViewById(R.id.nameCustom);
            holder.txtNum           = (TextView) convertView.findViewById(R.id.numCustom);
            holder.txtReady         = (TextView) convertView.findViewById(R.id.readyCustom);
            holder.imgDelete        = (ImageView) convertView.findViewById(R.id.imageViewDelete);
            holder.imgEdit          = (ImageView) convertView.findViewById(R.id.imageViewEdit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Paging paging = pagingList.get(position);
        holder.txtName.setText(paging.getName());
        holder.txtNum.setText("Number of People: " + paging.getNum());
        if(paging.getReady() == 0){
            holder.txtReady.setText("Not ready yet!");
        }else{
            holder.txtReady.setText("Ready!");
        }

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateCustomerActivity.class);
                intent.putExtra("dataPaging", paging);
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete(paging.getName(),paging.getId());
            }
        });
        return convertView;
    }

    private void confirmDelete(String name, final int id){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
        dialogDelete.setMessage("Do you want to delete " + name + "?");
        dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.deleteCustomer(id);
            }
        });
        dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelete.show();
    }
}
