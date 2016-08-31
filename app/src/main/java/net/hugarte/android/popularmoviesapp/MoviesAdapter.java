package net.hugarte.android.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hasier on 31/08/2016.
 */
public class MoviesAdapter extends BaseAdapter {

    private ArrayList<MovieInfo> movies;
    private Context context;
    private LayoutInflater mInflater;
    public MoviesAdapter(Context context, ArrayList<MovieInfo> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_movies,null);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.grid_item_movies_imgview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MovieInfo movieInfo = movies.get(position);
        Picasso.with(context).load(movieInfo.getPoster_path()).into(viewHolder.imageView);
        return convertView;

    }
    class ViewHolder{
        public ImageView imageView;
    }
}
