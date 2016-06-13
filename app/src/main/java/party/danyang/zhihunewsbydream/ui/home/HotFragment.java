package party.danyang.zhihunewsbydream.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.code19.library.L;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import party.danyang.zhihunewsbydream.R;
import party.danyang.zhihunewsbydream.adapter.ArrayRecyclerAdapter;
import party.danyang.zhihunewsbydream.adapter.home.HotAdapter;
import party.danyang.zhihunewsbydream.model.home.hot.Hot;
import party.danyang.zhihunewsbydream.model.home.hot.Recent;
import party.danyang.zhihunewsbydream.net.HomeApi;
import party.danyang.zhihunewsbydream.ui.detail.DetailActivity;
import party.danyang.zhihunewsbydream.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends Fragment {

    public static final int _LOAD_NEWS = 19940216;
    public static final int _LOAD_HOT = 19940107;
    public static final int _LOAD_NEWS_FROM_REALM = 19960104;
    public static final int _LOAD_HOT_FROM_REALM = 19951114;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    private HotAdapter adapter;

    private Activity context;

    private Realm realm;

    private boolean hasLoad = false;

    public HotFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = getActivity();
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        realm = Realm.getInstance(context);
        initViews();
        return v;
    }

    private void initViews() {
        adapter = new HotAdapter(context);
        adapter.setOnItemClickListener(new ArrayRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recent data = adapter.get(position);
                L.d(data.getTitle() + " " + data.getNews_id());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.ID, data.getNews_id());
                intent.putExtra(DetailActivity.TITLE, data.getTitle());

                ImageView imageView = (ImageView) view.findViewById(R.id.img_home_item);
                Bitmap bitmap = null;
                BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                if (bd != null) {
                    bitmap = bd.getBitmap();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    intent.putExtra(DetailActivity.COLOR, Utils.getPaletteColor(bitmap));
                }
                ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0);

                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(context).resumeTag("1");
                } else {
                    Picasso.with(context).pauseTag("1");
                }
            }
        });
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendToLoad();
            }
        });
        lazyLoad();
    }

    private void lazyLoad() {
        getHotFromRealm();
    }

    private void sendToLoad() {
        if (hasLoad) {
            return;
        }
        getHot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        realm.close();
    }

    private void setRefresher(boolean isRefresh) {
        if (refresher != null) {
            refresher.setRefreshing(isRefresh);
        }
    }

    private void getHot() {
        setRefresher(true);
        hasLoad = true;
        HomeApi.loadHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Hot>() {
                    @Override
                    public void onCompleted() {
                        hasLoad = false;
                        setRefresher(false);
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hasLoad = false;
                        setRefresher(false);
                        if (recyclerView != null) {
                            Snackbar.make(recyclerView, "error:" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                        L.e(e.getMessage());
                        unsubscribe();
                    }

                    @Override
                    public void onNext(Hot hot) {
                        if (hot != null && hot.getRecent() != null && hot.getRecent().size() > 0) {
                            List<Recent> list = hot.getRecent();
                            adapter.clear();
                            adapter.addAll(list);
                            realm.beginTransaction();
                            realm.clear(Recent.class);
                            realm.copyToRealmOrUpdate(list);
                            realm.commitTransaction();
                        }
                    }
                });
    }

    private void getHotFromRealm() {
        List<Recent> list = Recent.all(realm);
        if (list.size() > 0) {
            adapter.clear();
            adapter.addAll(list);
        } else {
            sendToLoad();
        }
    }

}
