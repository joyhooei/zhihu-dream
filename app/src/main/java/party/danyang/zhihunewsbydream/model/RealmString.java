package party.danyang.zhihunewsbydream.model;

import io.realm.RealmObject;

/**
 * Created by yaki on 16-6-12.
 */
public class RealmString extends RealmObject {
    private String stringValue;

    public RealmString() {
    }

    public RealmString(String stringValue) {
        this.stringValue = stringValue;
    }


    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
