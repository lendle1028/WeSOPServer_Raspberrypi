/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import rocks.imsofa.wesop.server.DebugUtils;

/**
 * handle the id maintenance functionality for this machine
 *
 * @author lendle
 */
public class IDUtil {

    private static String id = null;

    public static String getId() throws Exception {
        if (id != null) {
            return id;
        } else {
            //load the stored id from user folder
            try {
                File idFile = new File(PathUtil.getSOPHomeFolder(), ".id");
                if (!idFile.exists()) {

                    //create the id file
                    id = UUID.randomUUID().toString();
                    FileUtils.write(idFile, id, "utf-8");

                } else {
                    id = FileUtils.readFileToString(idFile, "utf-8");
                }
                return id;
            } catch (IOException ex) {
                DebugUtils.log(Level.SEVERE, ex.getMessage(), true);
            }
        }
        throw new Exception("unable to get id");
    }

}
