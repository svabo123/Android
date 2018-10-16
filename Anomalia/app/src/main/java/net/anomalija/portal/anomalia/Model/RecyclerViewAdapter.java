package net.anomalija.portal.anomalia.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import net.anomalija.portal.R;
import net.anomalija.portal.anomalia.Activitys.MainActivity;
import net.anomalija.portal.anomalia.Activitys.NewsActivity;

import java.util.ArrayList;
import static android.view.View.*;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context coActivity;
    private ArrayList<News> list;
    public static final int CATEGORY = 2;
    private Category category;


    public RecyclerViewAdapter(Context context, ArrayList<News> news, Category category) {
        list = news;
        coActivity = context;
        this.category = category;
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        final News news = list.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        RequestOptions requestOptions1 = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading_icon);
        Glide.with(coActivity)
                .asBitmap()
                .load(news.getSlika())
                .apply(requestOptions)
                .apply(requestOptions1)
                .into(holder.imageView);
        holder.naslov_main.setText(news.getNaslov());
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coActivity, NewsActivity.class);
                intent.putExtra("news", news);
                coActivity.startActivity(intent);
            }
        });
        holder.naslov_main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coActivity, NewsActivity.class);
                intent.putExtra("news", news);
                intent.putExtra("category", category);
                coActivity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() { return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 ){
            return 1;
        } else return 2;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView naslov_main;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slika);
            naslov_main = itemView.findViewById(R.id.naslov);
        }
    }


    private ViewHolder getViewHolder(ViewGroup parent ,int viewType){
        if (MainActivity.type == MainActivity.MAIN) {
            if (viewType == 1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_list1, parent, false);
//
                return new ViewHolder(view);
            } else if (MainActivity.type == CATEGORY){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_list2, parent, false);
                return new ViewHolder(view);

            }
        }  else if (MainActivity.type == MainActivity.CATEGORY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_list2, parent, false);
            return new ViewHolder(view);

        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_list2, parent, false);
        return new ViewHolder(view);

    }
}
