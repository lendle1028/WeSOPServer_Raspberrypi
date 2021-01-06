/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.CRC32;

/**
 *
 * @author lendle
 */
public class CRCUtil {
	public static long getCRC(File file) throws Exception{
		CRC32 crc=new CRC32();
		try(InputStream input=new FileInputStream(file)){
			byte [] buffer=new byte[1024];
			int b=input.read(buffer);
			while(b!=-1){
				crc.update(buffer, 0, b);
				b=input.read(buffer);
			}
			return crc.getValue();
		}
	}
}
