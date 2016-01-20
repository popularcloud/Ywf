package com.qiuweixin.veface.mvp.adpter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qiuweixin.veface.R;
import com.qiuweixin.veface.mvp.bean.ArticleInfo;
import com.qiuweixin.veface.util.DateUtil;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private ArrayList<ArticleInfo> listInfos;

    public ArticleAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_ariticle,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ArticleInfo articleInfo = listInfos.get(i);

        Uri uri = Uri.parse(articleInfo.getImage());
        viewHolder.image.setImageURI(uri);
        viewHolder.title.setText(articleInfo.getTitle());
        viewHolder.readNumber.setText("阅读数:"+articleInfo.getReadCount()+"");
        viewHolder.time.setText(DateUtil.friendlyFormat(articleInfo.getDate()));
        viewHolder.subscription.setText(articleInfo.getSrc_name());

    }

    @Override
    public int getItemCount() {
        return listInfos!=null?listInfos.size():0;
    }

    public void setArticleData(ArrayList<ArticleInfo> data) {
        listInfos = data;
        notifyDataSetChanged();
    }

    public void appendArticleData(ArrayList<ArticleInfo> data) {
        if (listInfos == null) {
            setArticleData(data);

            return;
        }
        int start =  1 + listInfos.size();
        listInfos.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView image;
        public TextView title;
        public TextView subscription;
        public TextView readNumber;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            subscription = (TextView) itemView.findViewById(R.id.subscription);
            readNumber = (TextView) itemView.findViewById(R.id.read_number);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}