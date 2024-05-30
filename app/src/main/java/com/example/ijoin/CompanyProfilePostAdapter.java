package com.example.ijoin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyProfilePostAdapter extends RecyclerView.Adapter<CompanyProfilePostAdapter.ViewHolder> {
    private List<Post> posts;
    private Context context;

    public CompanyProfilePostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompanyProfilePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.company_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyProfilePostAdapter.ViewHolder holder, int position) {
        if (posts != null) {
            Post post = posts.get(position);
            holder.postName.setText(post.getPostName());
            holder.description.setText(post.getDescription());
            holder.viewApplicantsButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postName;
        TextView description;
        AppCompatButton viewApplicantsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postName = itemView.findViewById(R.id.post_name);
            description = itemView.findViewById(R.id.post_description);
            viewApplicantsButton = itemView.findViewById(R.id.view_applicants_button);
        }
    }
}
