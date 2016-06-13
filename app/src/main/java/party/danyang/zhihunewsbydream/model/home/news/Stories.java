package party.danyang.zhihunewsbydream.model.home.news;

import com.google.gson.annotations.Expose;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import party.danyang.zhihunewsbydream.model.RealmString;

/**
 * Created by yaki on 16-6-8.
 */
public class Stories extends RealmObject {
    @Expose
    private RealmList<RealmString> images;

    @Expose
    private int type;

    @PrimaryKey
    @Expose
    private int id;

    @Expose
    private String ga_prefix;

    @Expose
    private String title;

    @Expose
    private String date;

    public static List<Stories> all(Realm realm) {
        return realm.where(Stories.class).findAll();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImages(RealmList<RealmString> images) {
        this.images = images;
    }

    public RealmList<RealmString> getImages() {
        return this.images;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getGa_prefix() {
        return this.ga_prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
