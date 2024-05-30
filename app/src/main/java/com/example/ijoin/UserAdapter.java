package com.example.ijoin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ijoin.R;
import com.example.ijoin.Users;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Users> userList;
    private static OnItemClickListener listener;

    public UserAdapter(List<Users> userList) {
        this.userList = userList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);
        String nameSurname = user.getName();
        if (Objects.equals(user.getUserType(), "Vol")) {
            nameSurname = nameSurname + " " + user.getSurname();
            holder.textViewUserType.setText("Volunteer");
        } else {
            holder.textViewUserType.setText("Company");
        }
        holder.textViewNameSurname.setText(nameSurname);

        // Load profile picture using Glide
        if (user.getprofileImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(user.getprofileImageUrl())
                    .into(holder.circleImageViewProfile);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNameSurname;
        TextView textViewUserType;
        CircleImageView circleImageViewProfile;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameSurname = itemView.findViewById(R.id.user_name_surname);
            textViewUserType = itemView.findViewById(R.id.textViewUserType);
            circleImageViewProfile = itemView.findViewById(R.id.imageViewUserAvatar);
            itemView.setOnClickListener(this); // Set click listener
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
