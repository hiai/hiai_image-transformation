package com.hmt.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author hiai
 * 
 * 修改图片工具类
 *
 */
public class ImageModifyUtil {
	public  static void scale( BufferedImage src,String tid,String aid,
           int reqWidth) {
		
		 Image image = null;
        int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            
            //计算缩放比例
	        float inSampleSize = calculateInSampleSize(width, reqWidth);
	        if (inSampleSize != 1) {
	           height = (int) ((float) height / inSampleSize);
	            width = reqWidth;
	           
	            image = src.getScaledInstance(width, height,
	                    Image.SCALE_DEFAULT);
	           
	            src = new BufferedImage(image.getWidth(null), image.getHeight(null),BufferedImage.TYPE_INT_RGB);
	            Graphics2D g2 = src.createGraphics();
	            g2.drawImage(image, null, null);
	       
	            File file=new File(DiskUtil.getImageCacheDir(),DiskUtil.hashKeyForDisk(reqWidth+tid+aid)+".jpg");
	           
	     		try {
					ImageIO.write(src, "jpg",  file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
	        }else {
				return ;
			}
       
    }
	   public static float calculateInSampleSize(int imageWidth, int reqWidth) {

	        float Ratio = 1;
	        if (imageWidth > reqWidth) {
	            // 计算出实际宽高和目标宽高的比率

	            Ratio = (float) imageWidth / (float) reqWidth;

	        }

	        return Ratio;
	    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
