package com.example.roman.service_desk_client.Classes;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.service_desk_client.ClaimActivity;
import com.example.roman.service_desk_client.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.example.roman.service_desk_client.SendData;
import com.example.roman.service_desk_client.UsersMainActivity;

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ClaimsViewHolder> {
    List<Claim> cards;
    private static String selectedDate;
    public String [] colors= {"#d0f0c0","#ede674","#f7d420","#f6903e","#f04f54"};
    private String[] statuses = {"Ожидает назначения мастера", "В работе", "Ожидает подтверждения", "Выполнена"};
    private String[] masters = {"Иван Иванов", "Петр Михайлович"};
    private String previousActivity;
    public ClaimsAdapter(Claim[] _cards, String from){
        previousActivity = from;
        cards = new ArrayList<Claim>();
        for(Claim claim : _cards){
            cards.add(claim);
        }
    }


    @NonNull
    @Override
    public ClaimsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.claimcard, viewGroup, false);
        ClaimsViewHolder taskCardViewHolder = new ClaimsViewHolder(v);
        return taskCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClaimsViewHolder taskCardViewHolder, int i) {
        taskCardViewHolder.claimId = cards.get(i).id;
        taskCardViewHolder.invId = cards.get(i).inv_id;
        taskCardViewHolder.description = cards.get(i).description;
        taskCardViewHolder.status = cards.get(i).status;
        taskCardViewHolder.tvDate.setText(cards.get(i).date);
        taskCardViewHolder.tvTitle.setText(cards.get(i).title);
        taskCardViewHolder.tvStatus.setText(statuses[cards.get(i).status]);
        if(cards.get(i).status == 2) {
            taskCardViewHolder.tvStatus.setTextColor(Color.parseColor("#ff545c"));
        }
        taskCardViewHolder.tvMaster.setText((cards.get(i).serviceman_id != -1 ? "Мастер: " +  masters[cards.get(i).serviceman_id] : ""));
        taskCardViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (taskCardViewHolder.status) {
                    case 0 : {
                        Intent editActivity = new Intent(taskCardViewHolder.cv.getContext(), ClaimActivity.class);
                        editActivity.putExtra("mode", "edit");
                        editActivity.putExtra("id", taskCardViewHolder.claimId);
                        editActivity.putExtra("title", taskCardViewHolder.tvTitle.getText());
                        editActivity.putExtra("description", taskCardViewHolder.description);
                        editActivity.putExtra("inv_id", taskCardViewHolder.invId);
                        editActivity.putExtra("previous_activity", previousActivity);
                        taskCardViewHolder.cv.getContext().startActivity(editActivity);
                        break;
                    }
                    case 1: {
                        Intent showActivity = new Intent(taskCardViewHolder.cv.getContext(), ClaimActivity.class);
                        showActivity.putExtra("mode", "show");
                        showActivity.putExtra("id", taskCardViewHolder.claimId);
                        showActivity.putExtra("title", taskCardViewHolder.tvTitle.getText());
                        showActivity.putExtra("description", taskCardViewHolder.description);
                        showActivity.putExtra("serviceman", taskCardViewHolder.tvMaster.getText());
                        showActivity.putExtra("status", taskCardViewHolder.tvStatus.getText());
                        showActivity.putExtra("inv_id", taskCardViewHolder.invId);
                        showActivity.putExtra("previous_activity", previousActivity);
                        taskCardViewHolder.cv.getContext().startActivity(showActivity);
                        break;
                    }
                    case 2: {
                        if(previousActivity.compareTo("UsersMainActivity") == 0) {
                            new SweetAlertDialog(taskCardViewHolder.cv.getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Проблема решена?")
                                    .setContentText("Заявка переведена в статус завершена. Это означает, что ваша проблема решена." +
                                            "Если проблема действительно решена, нажмите \"Да\"." +
                                            "Если же проблема не решена, нажмите кнопку \"Нет\"")
                                    .setConfirmText("Да")
                                    .setCancelText("Нет")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            taskCardViewHolder.status = 3;
                                            String params = "id=" + taskCardViewHolder.claimId + "&status=3";
                                            SendData sender = new SendData(params, "claim/updatestatus", taskCardViewHolder.cv.getContext());
                                            sender.execute();
                                            sDialog.dismissWithAnimation();
                                            Intent intent = new Intent(taskCardViewHolder.cv.getContext(), UsersMainActivity.class);
                                            taskCardViewHolder.cv.getContext().startActivity(intent);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            taskCardViewHolder.status = 1;
                                            String params = "id=" + taskCardViewHolder.claimId + "&status=1";
                                            SendData sender = new SendData(params, "claim/updatestatus", taskCardViewHolder.cv.getContext());
                                            sender.execute();
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
            }
        });
        taskCardViewHolder.index = i;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ClaimsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvDate, tvTitle, tvStatus, tvMaster;
        int index, claimId, status, invId;
        String description;
        public ClaimsViewHolder(@NonNull final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvStatus = (TextView)itemView.findViewById(R.id.Status);
            tvMaster = (TextView)itemView.findViewById(R.id.tvMaster);
        }
    }
}

