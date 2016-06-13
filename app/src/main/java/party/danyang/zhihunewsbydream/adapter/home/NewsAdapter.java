package party.danyang.zhihunewsbydream.adapter.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.code19.library.DateUtils;
import com.code19.library.NetUtils;
import com.code19.library.SPUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;

import party.danyang.zhihunewsbydream.R;
import party.danyang.zhihunewsbydream.adapter.ArrayRecyclerAdapter;
import party.danyang.zhihunewsbydream.model.home.news.Stories;
import party.danyang.zhihunewsbydream.ui.home.MainActivity;

/**
 * Created by yaki on 16-6-8.
 */
public class NewsAdapter extends ArrayRecyclerAdapter<Stories, NewsAdapter.ViewHolder> {

    private Context context;

//    public NewsAdapter(Context context) {
//    }

    public NewsAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }


    @Override
    public long getItemId(int position) {
        if (get(position) != null && get(position).getImages() != null && get(position).getImages().size() > 0) {
            return get(position).getImages().get(0).hashCode();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stories data = get(position);
        if (data != null) {
            holder.title.setText(data.getTitle());
            if (data.getImages().size() > 0 && !TextUtils.isEmpty(data.getImages().get(0).getStringValue())) {
                //在wifi下或开了无图或没网（picasso缓存可能有图片）,则加载图片
                if (!(Boolean) SPUtils.getSp(context, MainActivity.PREF_NO_PIC, Boolean.FALSE)
                        || (NetUtils.isConnected(context) && NetUtils.isWiFi(context)) || !NetUtils.isConnected(context)) {
                    Picasso.with(context)
                            .load(data.getImages().get(0).getStringValue())
                            .error(R.drawable.error_view_cloud)
                            .noFade()
                            .placeholder(R.drawable.error_view_cloud)
                            .tag("1")
                            .into(holder.img);
                }

            }
            holder.date.setVisibility(View.VISIBLE);
            Date date = DateUtils.string2Date(data.getDate(), "yyyyMMdd");
            holder.date.setText(DateUtils.formatDateCustom(date, "yyyy-MM-dd"));
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) itemView.findViewById(R.id.tv_home_item);
            img = (ImageView) itemView.findViewById(R.id.img_home_item);
            date = (TextView) itemView.findViewById(R.id.tv_date_home_item);
        }
    }
}
