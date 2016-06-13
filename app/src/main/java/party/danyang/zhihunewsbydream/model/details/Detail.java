package party.danyang.zhihunewsbydream.model.details;

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
public class Detail extends RealmObject {
    @Expose
    private String body;

    @Expose
    private String image_source;

    @Expose
    private String title;

    @Expose
    private String image;

    @Expose
    private String share_url;

    @Expose
    private RealmList<RealmString> js;

    @Expose
    private String ga_prefix;

    @Expose
    private Section section;

    @Expose
    private RealmList<RealmString> images;

    @Expose
    private int type;

    @PrimaryKey
    @Expose
    private int id;

    public static Detail all(Realm realm, int id) {
        return realm.where(Detail.class).equalTo("id", id).findFirst();
    }

    @Expose
    private RealmList<RealmString> css;

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getImage_source() {
        return this.image_source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_url() {
        return this.share_url;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getGa_prefix() {
        return this.ga_prefix;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return this.section;
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

    public RealmList<RealmString> getCss() {
        return css;
    }

    public void setCss(RealmList<RealmString> css) {
        this.css = css;
    }

    public RealmList<RealmString> getImages() {
        return images;
    }

    public void setImages(RealmList<RealmString> images) {
        this.images = images;
    }

    public RealmList<RealmString> getJs() {
        return js;
    }

    public void setJs(RealmList<RealmString> js) {
        this.js = js;
    }
}
