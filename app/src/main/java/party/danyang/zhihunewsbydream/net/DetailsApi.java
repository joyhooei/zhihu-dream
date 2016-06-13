package party.danyang.zhihunewsbydream.net;

import party.danyang.zhihunewsbydream.model.details.Detail;
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
public class DetailsApi {
    public static final String API_URL = "http://news-at.zhihu.com/api/4/";

    public interface DetailApi {
        @GET("news/{id}")
        Observable<Detail> loadDetailsApi(@Path("id") String id);
    }

    public static final Observable<Detail> loadDetail(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(Utils.gsonBuilder()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(DetailApi.class).loadDetailsApi(String.valueOf(id));
    }
}
