package com.example.ijoin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.List;
import java.util.Objects;

public class UserAdapter1 extends RecyclerView.Adapter<UserAdapter1.UserViewHolder>{
    private List<Users> userList;
    private OnUserClickListener listener;
    public UserAdapter1(List<Users> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adminpage, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);
        String namesurname;
        if(Objects.equals(user.getUserType(), "Vol")){
            namesurname = user.getName() + " " + user.getSurname();
            holder.nameSurname.setText(namesurname);
            holder.usertype.setText("Volunteer");
            holder.email.setText(user.getEmail());
            holder.uid.setText(user.getUid1());
        }
        if(Objects.equals(user.getUserType(), "Comp")){
            namesurname = user.getName();
            holder.nameSurname.setText(namesurname);
            holder.usertype.setText("Company");
            holder.email.setText(user.getEmail());
            holder.uid.setText(user.getUid1());
        }
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUpdateUser(user);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteUser(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView nameSurname, email, uid, usertype;
        public ImageView updateButton, deleteButton;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameSurname = itemView.findViewById(R.id.name_surname);
            email = itemView.findViewById(R.id.email_text_view);
            uid = itemView.findViewById(R.id.uid);
            usertype = itemView.findViewById(R.id.userType);
            updateButton = itemView.findViewById(R.id.update_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
    public interface OnUserClickListener {
        void onUpdateUser(Users user);
        void onDeleteUser(Users user);
    }
}
