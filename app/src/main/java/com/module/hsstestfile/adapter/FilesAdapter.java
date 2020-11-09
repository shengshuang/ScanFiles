package com.module.hsstestfile.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.module.hsstestfile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hss
 * @time 2020/11/4 00:32
 * @describe
 **/
public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileHolder> {

    private List<String> data = new ArrayList<>();
    private Context context;

    public FilesAdapter(Context context){
        this.context = context;
    }

    public void setData(List<String> dataList){
        this.data = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilesAdapter.FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fileItemView = LayoutInflater.from(context).inflate(R.layout.file_item, parent, false);
        return new FilesAdapter.FileHolder(fileItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.FileHolder holder, int position) {
        holder.setFileName(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FileHolder extends RecyclerView.ViewHolder {

        TextView fileItem;
        public FileHolder(@NonNull View itemView) {
            super(itemView);
            fileItem = itemView.findViewById(R.id.id_file_item);
        }

        public void setFileName(String fileName){
            fileItem.setText(fileName);
        }

    }
}
