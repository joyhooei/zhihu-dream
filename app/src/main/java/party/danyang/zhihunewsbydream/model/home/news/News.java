package party.danyang.zhihunewsbydream.model.home.news;

import com.google.gson.annotations.Expose;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yaki on 16-6-8.
 */
public class News extends RealmObject{
    @Expose
    private String date;

    @Expose
    private RealmList<Stories> stories;

    @Expose
    private RealmList<Stories> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setStories(RealmList<Stories> stories) {
        this.stories = stories;
    }

    public RealmList<Stories> getStories() {
        return this.stories;
    }

    public void setTop_stories(RealmList<Stories> top_stories) {
        this.top_stories = top_stories;
    }

    public RealmList<Stories> getTop_stories() {
        return this.top_stories;
    }
}
