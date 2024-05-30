package com.example.ijoin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<Upload> uploadList;

    public FileAdapter(List<Upload> uploadList) {
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        Upload upload = uploadList.get(position);
        holder.fileName.setText(upload.getName());
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        public TextView fileName;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
        }
    }
}
