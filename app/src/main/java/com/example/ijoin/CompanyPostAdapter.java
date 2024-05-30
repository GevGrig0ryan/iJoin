package com.example.ijoin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CompanyPostAdapter extends RecyclerView.Adapter<CompanyPostAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;

    public CompanyPostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.company_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (posts != null) {
            Post post = posts.get(position);
            holder.postName.setText(post.getPostName());
            holder.description.setText(post.getDescription());
            holder.viewApplicantsButton.setOnClickListener(v -> viewApplicants(post));
        }
    }

    private void viewApplicants(Post post) {
        Intent intent = new Intent(context, ApplicantsActivity.class);
        intent.putExtra("postId", post.getPostName());
        intent.putExtra("companyUid", post.getComapnyUid());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
