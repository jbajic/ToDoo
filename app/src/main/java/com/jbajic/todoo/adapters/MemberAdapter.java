package com.jbajic.todoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jbajic.todoo.R;
import com.jbajic.todoo.helpers.DatabaseHelper;
import com.jbajic.todoo.interfaces.RequestListener;
import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.User;
import com.jbajic.todoo.services.APIService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jbajic on 08.07.17..
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<User> userList;
    private Context context;
    private Project project;

    public MemberAdapter(List<User> userList, Context context, Project project) {
        this.userList = userList;
        this.context = context;
        this.project = project;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);

        return new MemberViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.tvUserName.setText(user.toString());
        holder.btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = APIService.getInstance((Activity) context);

                apiService.associateUserProject(user, project, new RequestListener() {
                    @Override
                    public void failed(String message) {
                        Toast
                                .makeText(context, message, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void finished(String message) {
                        userList.remove(user);
                        DatabaseHelper.getInstance(context).associateUserProject(project.getServerId(), user.getServerId());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_userName)
        TextView tvUserName;
        @InjectView(R.id.btn_addUser)
        Button btnAddUser;

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
