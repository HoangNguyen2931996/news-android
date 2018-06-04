package com.example.kutudp.tinhangngay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kutudp.tinhangngay.R;
import com.example.kutudp.tinhangngay.models.Resource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResourceAdapter extends BaseAdapter {

    private ArrayList<Resource> listResource;
    private Context context;

    public ResourceAdapter(ArrayList<Resource> listResource, Context context) {
        this.listResource = listResource;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listResource.size();
    }

    @Override
    public Object getItem(int position) {
        return listResource.get(position);
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
            convertView = inflater.inflate(R.layout.item_resource, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Resource resource = (Resource) getItem(position);
        viewHolder.textView.setText(resource.getName());
        Picasso.get().load(resource.getLink()).placeholder(R.drawable.ic_crop_original_black_24dp).error(R.drawable.ic_error_outline_black_24dp).into(viewHolder.imageView);
        return convertView;
    }

    public class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }

}
