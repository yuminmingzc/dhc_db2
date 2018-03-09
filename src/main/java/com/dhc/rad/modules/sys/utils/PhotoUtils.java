/**
 * Copyright &copy; 2012-2014 <a href="http://www.dhc.com.cn">DHC</a> All rights reserved.
 */
package com.dhc.rad.modules.sys.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 用户工具类
 * @author 李俊杰
 * @version 2016-3-16
 */
public class PhotoUtils {

	/**
	 * 图片等比缩放方法
	 * @param src 原图片路径
	 * @param dest 目标图片路径
	 * @param w 宽度
	 * @param h 高度
	 * @author lijunjie
	 * @version 2016-3-16
	 */
	public static void zoomImage(String src,String dest,int w,int h) throws Exception {  
        int wr=0, hr=0;  
        try{
        Image srcFile = ImageIO.read(new File(src));
        if(srcFile.getWidth(null)>w){
        	wr=w;
        	hr=srcFile.getHeight(null)*w/srcFile.getWidth(null);
        }else{
        	wr=srcFile.getWidth(null);
        	hr=srcFile.getHeight(null);
        }
        BufferedImage tag = new BufferedImage(wr, hr,BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(srcFile, 0, 0, wr, hr, null);
        //String filePrex = oldFile.substring(0, oldFile.indexOf('.'));

        /** 压缩之后临时存放位置 */
        File newFile = new File(dest);
        FileOutputStream out = new FileOutputStream(newFile);

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
        /** 压缩质量 */
        jep.setQuality(1.0f, true);
        encoder.encode(tag, jep);
        out.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}catch (IOException e) {
            e.printStackTrace();
        }
          
    } 
	
}
