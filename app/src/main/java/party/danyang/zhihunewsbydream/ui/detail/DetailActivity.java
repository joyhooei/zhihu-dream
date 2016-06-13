package party.danyang.zhihunewsbydream.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.code19.library.NetUtils;
import com.code19.library.SPUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import party.danyang.zhihunewsbydream.R;
import party.danyang.zhihunewsbydream.model.details.Detail;
import party.danyang.zhihunewsbydream.net.DetailsApi;
import party.danyang.zhihunewsbydream.ui.home.MainActivity;
import party.danyang.zhihunewsbydream.widget.UnlimitedMarqueeTextVew;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailActivity extends AppCompatActivity {


    public static final String ID = "id";
    public static final String COLOR = "color";
    public static final String TITLE = "title";

    @Bind(R.id.toolbar_detail)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout_detail)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.img_collapsing_toolbar)
    ImageView imgToolbar;
    @Bind(R.id.tv_img_recourse)
    TextView tvImgRec;
    @Bind(R.id.web_view_detail)
    WebView webView;
    @Bind(R.id.app_bar_detail)
    AppBarLayout appBar;
    @Bind(R.id.tv_toolbar_title_detail)
    UnlimitedMarqueeTextVew tvCollapsedTitle;
    @Bind(R.id.tv_expended_title_detail)
    TextView tvExpendedTitle;

    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private Realm realm;
    private int id;
    private String title;
    private int color;
    private boolean hasLoad = false;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        realm = Realm.getInstance(this);
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra(ID, 0);
            color = intent.getIntExtra(COLOR, getResources().getColor(R.color.colorPrimary));
            title = intent.getStringExtra(TITLE);
        }
        initViews();
    }

    private void initViews() {
        setupToolbar();
        setupWebView();
        lazyLoad();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(1);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        if (!(Boolean) SPUtils.getSp(this, MainActivity.PREF_NO_PIC, Boolean.FALSE)
                || (NetUtils.isConnected(this) && NetUtils.isWiFi(this)) || !NetUtils.isConnected(this)) {
            webSettings.setBlockNetworkImage(false);
        } else {
            webSettings.setBlockNetworkImage(true);
        }
    }

    private void setupToolbar() {
        toolbarLayout.setBackgroundColor(color);
        toolbarLayout.setContentScrimColor(color);
        toolbarLayout.setStatusBarScrimColor(color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.supportFinishAfterTransition();
            }
        });

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        tvCollapsedTitle.setVisibility(View.GONE);
                        tvExpendedTitle.setVisibility(View.VISIBLE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        //折叠
                        tvCollapsedTitle.setVisibility(View.VISIBLE);
                        tvExpendedTitle.setVisibility(View.GONE);
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            //由折叠变为中间状态时隐藏播放按钮
                            tvCollapsedTitle.setVisibility(View.GONE);
                            tvExpendedTitle.setVisibility(View.VISIBLE);
                        }
                        toolbarLayout.setTitle("");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        tvExpendedTitle.setText(title);
        tvCollapsedTitle.setText(title);

    }

    private void lazyLoad() {
        getDetailsFromRealm();
    }

    private void sendToLoad() {
        if (hasLoad) {
            return;
        }
        getDetails();
    }

    private void setupDetailToViews(final Detail detail) {
        toolbarLayout.setTitle("");
        if (!(Boolean) SPUtils.getSp(this, MainActivity.PREF_NO_PIC, Boolean.FALSE)
                || (NetUtils.isConnected(this) && NetUtils.isWiFi(this)) || !NetUtils.isConnected(this)) {
            Picasso.with(this)
                    .load(detail.getImage())
                    .into(imgToolbar);
        }
        tvImgRec.setHint(detail.getImage_source());
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">";

        String data = "<html><head>" + css + "</head><body>" + detail.getBody() + "</body></html>";
        data = data.replace("<div class=\"img-place-holder\"></div>", "");
        webView.loadDataWithBaseURL(getBaseUrl(), data
                , "text/html", "utf-8", null);

        if (TextUtils.isEmpty(title)) {
            tvExpendedTitle.setText(detail.getTitle());
            tvCollapsedTitle.setText(detail.getTitle());
        }
    }

    private String getBaseUrl() {
        return "http://news-at.zhihu.com/api/4/news/" + id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        realm.close();
    }

    private void getDetails() {
        hasLoad = true;
        DetailsApi.loadDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Detail>() {
                    @Override
                    public void onCompleted() {
                        hasLoad = false;
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hasLoad = false;
                        if (webView != null) {
                            Snackbar.make(webView, "error:" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                        unsubscribe();

                    }

                    @Override
                    public void onNext(Detail detail) {
                        setupDetailToViews(detail);
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(detail);
                        realm.commitTransaction();
                    }
                });
    }

    private void getDetailsFromRealm(){
        Detail detail=Detail.all(realm,id);
        if (detail!=null&&detail.getBody()!=null){
            setupDetailToViews(detail);
        }else {
            sendToLoad();
        }
    }
}
