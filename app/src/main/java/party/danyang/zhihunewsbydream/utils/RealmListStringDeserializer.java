package party.danyang.zhihunewsbydream.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.RealmList;
import party.danyang.zhihunewsbydream.model.RealmString;

/**
 * Created by yaki on 16-6-12.
 */
public class RealmListStringDeserializer implements JsonDeserializer<RealmList<RealmString>> {
    @Override
    public RealmList<RealmString> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RealmList<RealmString> realmStrings=new RealmList<>();
        JsonArray stringList=json.getAsJsonArray();

        for (JsonElement stringElement:stringList){
            realmStrings.add(new RealmString(stringElement.getAsString()));
        }

        return realmStrings;
    }
}
