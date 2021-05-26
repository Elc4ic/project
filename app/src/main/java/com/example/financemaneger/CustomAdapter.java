package com.example.financemaneger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList date, notes,amount, mount,id,image,week,num;
    int vid;
    private final String id_z;



    public CustomAdapter(Context context, Activity activity, ArrayList id , ArrayList notes, ArrayList date, ArrayList amount, ArrayList mount,ArrayList  image,ArrayList week,int vid,ArrayList num,String id_z) {
        this.vid = vid;
        this.activity = activity;
        this.context = context;
        this.date = date;
        this.notes = notes;
        this.amount = amount;
        this.mount = mount;
        this.week=week;
        this.id = id;
        this.image = image;
        this.num=num;
        this.id_z=id_z;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.retrieve_layout, parent, false);
        return new MyViewHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (vid==0) {
            setItems(holder,position);
        } else if (vid==2){
            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Months months = Months.monthsBetween(epoch, now);
            if (Integer.parseInt((String) mount.get(position)) == months.getMonths()){
                setItems(holder,position);
            }else {
                holder.mainLayout.setVisibility(View.GONE);
                holder.mainLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        else if (vid==3){
            SimpleDateFormat dataFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar1 = Calendar.getInstance();
            String date1 = dataFormat1.format(calendar1.getTime());
            if (date.get(position).equals(date1)){
                setItems(holder,position);
            }else {
                holder.mainLayout.setVisibility(View.GONE);
                holder.mainLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        else if (vid==1){
            MutableDateTime epoch = new MutableDateTime();
            epoch.setDate(0);
            DateTime now = new DateTime();
            Weeks weeks = Weeks.weeksBetween(epoch,now);
            if (Integer.parseInt((String) week.get(position)) == weeks.getWeeks()){
                setItems(holder,position);
            }else {
                holder.mainLayout.setVisibility(View.GONE);
                holder.mainLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        else if (vid==4){
            if (num.get(position).equals("0")){
                setItems(holder,position);
            }else {
                holder.mainLayout.setVisibility(View.GONE);
                holder.mainLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        else if (vid==5){
            if (num.get(position).equals(id_z)){
                setItems(holder,position);
            }else {
                holder.mainLayout.setVisibility(View.GONE);
                holder.mainLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ras", vid);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("amount", Integer.parseInt((String) amount.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout mainLayout;
        View mView;
        TextView rId,rNotes,rData,rAmount;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            rAmount = mView.findViewById(R.id.amount);
            rId = itemView.findViewById(R.id.id_txt);
            rNotes= itemView.findViewById(R.id.note);
            imageView= itemView.findViewById(R.id.imageView);
            rData = itemView.findViewById(R.id.date);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
    private void setItems (@NonNull MyViewHolder holder,int position){
            holder.rId.setText(String.valueOf(id.get(position)));
            holder.rData.setText(String.valueOf(date.get(position)));
            holder.rAmount.setText(amount.get(position) + "p");
            holder.rNotes.setText(String.valueOf(notes.get(position)));

            if ("Transport".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_transport);
        } else if ("Food".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_food);
        } else if ("House".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_house);
        } else if ("Entertainment".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_entertainment);
        } else if ("Education".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_education);
        } else if ("Charity".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_consultancy);
        } else if ("Apparel".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_shirt);
        } else if ("Health".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_health);
        } else if ("Personal".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_personalcare);
        } else if ("Other".equals(image.get(position))) {
            holder.imageView.setImageResource(R.drawable.ic_other);
        }else if ("Salary".equals(image.get(position))) {
                holder.imageView.setImageResource(R.drawable.ic_salary);
            }
    }
}
