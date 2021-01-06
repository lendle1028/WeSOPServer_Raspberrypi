/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author lendle
 */
public class PlayItemSerializer implements JsonSerializer<PlayItem> {

    @Override
    public JsonElement serialize(PlayItem t, Type type, JsonSerializationContext jsc) {
        
        final JsonObject baseElement = new JsonObject();
        if(t.getDisplayName()!=null){
            //baseElement.add("displayName", new JsonPrimitive(t.getDisplayName()));
            baseElement.addProperty("displayName", t.getDisplayName());
        }
        if(t instanceof FilePlayItem){
            FilePlayItem ft=(FilePlayItem) t;
            //baseElement.add("file", new JsonPrimitive(ft.getFile()));
            baseElement.addProperty("file", ft.getFile());
        }
        //baseElement.add("className", new JsonPrimitive(t.getClass().getName()));
        baseElement.addProperty("className", t.getClass().getName());
        return baseElement;
    }
    
    public static void main(String [] args) throws Exception{
        FilePlayItem item=new FilePlayItem("123");
        System.out.println(GsonFactory.newGson().toJson(item));
    }
    
}
