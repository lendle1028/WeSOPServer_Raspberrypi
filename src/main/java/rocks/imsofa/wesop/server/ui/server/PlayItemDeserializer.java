/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.File;
import java.lang.reflect.Type;

/**
 *
 * @author lendle
 */
public class PlayItemDeserializer implements JsonDeserializer<PlayItem>{

    @Override
    public PlayItem deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject object=je.getAsJsonObject();
        if(DummyPlayItem.class.getName().equals(object.get("className").getAsString())){
            return new DummyPlayItem();
        }else if(FilePlayItem.class.getName().equals(object.get("className").getAsString())){
            FilePlayItem item=new FilePlayItem();
            //item.setDisplayName(object.get("displayName").getAsString());
            if(object.get("file")!=null){
                item.setFile(object.get("file").getAsString());
            }
            return item;
        }else if(PlaylistPlayItem.class.getName().equals(object.get("className").getAsString())){
            PlaylistPlayItem item=new PlaylistPlayItem();
            item.setDisplayName(object.get("displayName").getAsString());
            JsonArray array=object.getAsJsonArray("files");
            if(array!=null){
                File [] files=new File[array.size()];
                for(int i=0; i<files.length; i++){
                    files[i]=new File(array.get(i).getAsString());
                }
                item.setFiles(files);
            }
            return item;
        }
        return null;
    }
    
}
