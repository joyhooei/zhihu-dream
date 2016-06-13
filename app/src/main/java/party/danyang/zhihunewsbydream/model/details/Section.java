package party.danyang.zhihunewsbydream.model.details;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;

/**
 * Created by yaki on 16-6-8.
 */
public class Section extends RealmObject {
    @Expose
    private String thumbnail;

    @Expose
    private int id;

    @Expose
    private String name;

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
