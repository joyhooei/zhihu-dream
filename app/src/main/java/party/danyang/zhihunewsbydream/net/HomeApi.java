package party.danyang.zhihunewsbydream.net;

import com.code19.library.DateUtils;
import com.code19.library.L;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.RealmObject;
import party.danyang.zhihunewsbydream.model.home.hot.Hot;
import party.danyang.zhihunewsbydream.model.home.news.News;
import party.danyang.zhihunewsbydream.utils.Utils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yaki on 16-6-8.
 */
public final class HomeApi {

    public static final String API_URL = "http://news-at.zhihu.com/api/4/";

    public interface TodayNewsApi {
        @GET("news/latest")
        Observable<News> loadNewsApi();
    }

    public interface NewsApi {
        @GET("news/before/{date}")
        Observable<News> loadNewsApi(@Path("date") String path);
    }

    public static final Observable<News> loadNews(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(Utils.gsonBuilder()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        if (page == 0) {
            return retrofit.create(TodayNewsApi.class).loadNewsApi();
        } else {
            return retrofit.create(NewsApi.class)
                    .loadNewsApi(DateUtils.formatDateCustom(DateUtils.getDateAfter(new Date(), (page - 1) * -1), "yyyyMMdd"));
        }
    }

    public interface HotApi {
        @GET("news/hot")
        Observable<Hot> loadHotApi();
    }

    public static final Observable<Hot> loadHot() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(Utils.gsonBuilder()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(HotApi.class).loadHotApi();
    }
}
