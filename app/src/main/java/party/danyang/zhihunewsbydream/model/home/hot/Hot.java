package party.danyang.zhihunewsbydream.model.home.hot;

import com.google.gson.annotations.Expose;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by yaki on 16-6-8.
 */
public class Hot extends RealmObject{
    @Expose
    private RealmList<Recent> recent;

    public void setRecent(RealmList<Recent> recent) {
        this.recent = recent;
    }

    public RealmList<Recent> getRecent() {
        return this.recent;
    }
}
