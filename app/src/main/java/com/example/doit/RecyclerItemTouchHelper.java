package com.example.doit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.Adaptor.todoadaptor;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{
    private todoadaptor adaptor;
    public RecyclerItemTouchHelper(todoadaptor adaptor){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adaptor= adaptor;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction){
        final  int position= viewHolder.getAdapterPosition();
        if(direction== ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adaptor.getContext());
            builder.setTitle("Delete task");
            builder.setMessage("are you sure you want to delete this task");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adaptor.deleteItem(position);


                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adaptor.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            adaptor.editItem(position);
        }

    }
    @Override
    public void onChildDraw(@NonNull Canvas c,@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,int actionState, boolean isCurrentlyActive ){
        super.onChildDraw(c, recyclerView, viewHolder,dX,dY,actionState,isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset= 20;
        if(dX>0){
            icon= ContextCompat.getDrawable(adaptor.getContext(),R.drawable.baseline_ediit);
            background= new ColorDrawable(ContextCompat.getColor(adaptor.getContext(), com.google.android.material.R.color.design_default_color_primary_dark));

        }
        else{
            icon= ContextCompat.getDrawable(adaptor.getContext(),R.drawable.baseline_delete);
            background= new ColorDrawable(Color.RED);

        }
        assert icon!=null;
        int iconMargin= (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconTop= itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop +icon.getIntrinsicHeight();

        if(dX>0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft()+ iconMargin+icon.getIntrinsicHeight();
            icon.setBounds(iconLeft,iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft() +((int)dX)+ backgroundCornerOffset, itemView.getBottom());
        } else if (dX<0) {
            int iconLeft = itemView.getRight() + iconMargin-icon.getIntrinsicHeight();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight,iconBottom);

            background.setBounds(itemView.getRight()+ ((int)dX)-backgroundCornerOffset, itemView.getTop(),itemView.getRight() +((int)dX)+ backgroundCornerOffset, itemView.getBottom());


        }
        else {
            background.setBounds(0,0,0,0);

        }
        background.draw(c);
        icon.draw(c);
    }
}
