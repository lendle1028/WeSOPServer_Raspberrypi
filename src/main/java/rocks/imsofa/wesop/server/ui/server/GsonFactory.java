/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author lendle
 */
public class GsonFactory {
    public static Gson newGson(){
        GsonBuilder builder=new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(PlayItem.class, new PlayItemSerializer());
        builder.registerTypeAdapter(PlayItem.class, new PlayItemDeserializer());
        builder.registerTypeAdapter(FilePlayItem.class, new PlayItemSerializer());
        builder.registerTypeAdapter(FilePlayItem.class, new PlayItemDeserializer());
        return builder.create();
    }
}
