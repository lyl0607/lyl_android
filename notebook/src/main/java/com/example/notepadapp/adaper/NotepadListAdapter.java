package com.example.notepadapp.adaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepadapp.MainActivity;
import com.example.notepadapp.bean.Notepad;
import com.example.notepadapp.databinding.ItemListBinding;
import com.example.notepadapp.ui.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class NotepadListAdapter extends RecyclerView.Adapter<NotepadListAdapter.VH> {

    List<Notepad> list = new ArrayList<>();
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemListBinding binding = ItemListBinding.inflate(inflater);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Notepad notepad = list.get(position);
        holder.binding.contentTv.setText(notepad.getContent());
        holder.binding.timeTv.setText(list.get(position).getTime());

        //长按删除事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(notepad, v.getContext());
                return false;
            }
        });

        //单击跳转到详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) v.getContext();
                Intent intent = new Intent(activity, EditActivity.class);
                intent.putExtra("notepad", notepad);
                activity.startActivityForResult(intent, 100);
            }
        });
    }

    private void showDeleteDialog(Notepad notepad, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确定要删除吗?")
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        MainActivity activity = (MainActivity) context;
                        activity.myDbHelper.delete(notepad);
                        activity.findAll();
                        Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Notepad> nodepadlist) {
        this.list.clear();
        this.list.addAll(nodepadlist);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {

        private ItemListBinding binding;

        public VH(@NonNull ItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
