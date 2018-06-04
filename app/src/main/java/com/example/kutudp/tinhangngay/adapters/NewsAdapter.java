package com.example.kutudp.tinhangngay.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.kutudp.tinhangngay.R;
import com.example.kutudp.tinhangngay.models.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class NewsAdapter extends BaseAdapter {

    private ArrayList<News> listNews;
    private Context context;

    public NewsAdapter(ArrayList<News> listNews, Context context) {
        this.listNews = listNews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listNews.size();
    }

    @Override
    public Object getItem(int position) {
        return listNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_news, null);
            viewHolder.imageViewNews = (ImageView) convertView.findViewById(R.id.imageViewNews);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtPreview = (TextView) convertView.findViewById(R.id.txtPreview);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        News news = listNews.get(position);
        viewHolder.txtName.setMaxLines(2);
        viewHolder.txtName.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtName.setText(news.getName());
        viewHolder.txtDate.setText(news.getDateCreated());
        viewHolder.txtPreview.setMaxLines(2);
        viewHolder.txtPreview.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtPreview.setText(news.getPreview());
        Picasso.get().load(news.getPicture()).placeholder(R.drawable.ic_crop_original_black_24dp).error(R.drawable.ic_error_outline_black_24dp).into(viewHolder.imageViewNews);
        return convertView;
    }

    public class ViewHolder{
        private ImageView imageViewNews;
        private TextView txtName, txtPreview, txtDate;
    }

}
