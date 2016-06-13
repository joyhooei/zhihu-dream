package party.danyang.zhihunewsbydream.model.home.hot;

import com.google.gson.annotations.Expose;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaki on 16-6-8.
 */
public class Recent extends RealmObject {
    @PrimaryKey
    @Expose
    private int news_id;

    @Expose
    private String url;

    @Expose
    private String thumbnail;

    @Expose
    private String title;

    public static List<Recent> all(Realm realm) {
        return realm.where(Recent.class).findAll();
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public int getNews_id() {
        return this.news_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
