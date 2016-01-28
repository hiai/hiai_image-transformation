package com.hmt.image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;



/**
 * @author hiai
 * 
 * 图片网络工具类
 *
 */
public class ImageNetworkUtil{
	 public static final String GET_POST_THREADS_ATTACHMENT_BY_TID_AND_AID="http://hometown.scau.edu.cn/bbs/plugin.php?id=iltc_open:attachment&action=view&tid=";
//	缩放图地址
	 private static String  PHOTO_DIR="http://127.0.0.1:8080/theone/thecache/photo/";
      public static String getUrl(String tid,String aid) {
		 final String Url = GET_POST_THREADS_ATTACHMENT_BY_TID_AND_AID + tid + "&aid=" + aid;
		 HttpURLConnection connection = null;
		 URL url;
		try {
			url = new URL(Url);
			 connection = (HttpURLConnection) url.openConnection();connection.setRequestMethod("GET");
			 connection.setConnectTimeout(8000);
			 connection.setReadTimeout(8000);
			 InputStream in = (InputStream) connection.getInputStream();
			 
			
			 // 下面对获取到的输入流进行读取
			 BufferedReader reader = new BufferedReader(new
			 InputStreamReader(in));
			 StringBuilder response = new StringBuilder();
			 String line;
			 while ((line = reader.readLine()) != null) {
			 response.append(line);
			 }
			 System.out.print(response.toString());
             DiskUtil.saveJson(response.toString(), tid, aid,DiskUtil.getJsonCacheDir());
			 net.sf.json.JSONObject jb = net.sf.json.JSONObject.fromObject(response.toString());
			 net.sf.json.JSONObject jb2=jb.getJSONObject("data");
			 String photoUrl=jb2.getString("attachment");
			// System.out.print(photoUrl);
			 return photoUrl;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (connection != null) {
			connection.disconnect();
			}
			}
		
		return null;
	}
	public static void loadImage(String width,String tid,String aid) {
		
		
		 
		try {
			String key = DiskUtil.hashKeyForDisk(width+tid+aid); 
		File file=new File(DiskUtil.getImageCacheDir(),key+".jpg");
			if (file.exists()) {  
//				硬盘里有缩放图
				System.out.print("snapShot1 != null\n");
		
		    }else{
		    
		    	File file1=new File(DiskUtil.getImageCacheDir(),DiskUtil.hashKeyForDisk(tid+aid)+".jpg");
		    	
		    	if (file1.exists()){
		    		System.out.print("snapShot2 != null\n");
//		    		硬盘只有原图，则缩放
		    	
		    		BufferedImage  image = ImageIO.read(file1);
		    		 System.out.print(width+"\n");
		    		ImageModifyUtil.scale(image,tid,aid, Integer.parseInt(width));
		    		
		    	
    	}
		    	//硬盘没原图则请求网络
		    	else {
		    		
		    		System.out.print("no image in disk \n");
		    		URL url=new URL(ImageNetworkUtil.getUrl(tid, aid));
		    		 BufferedImage bufferedImage=ImageIO.read(url);
		    		 DiskUtil.saveImage(bufferedImage, tid, aid);
		    		 
		              ImageModifyUtil.scale(bufferedImage,tid,aid, Integer.parseInt(width));
		             
		        
		    		
		    	
		          
				}		    	
		    }  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	 public static void main(String[] args) {
		// getPhoto.getUrl("809563","490962");
		 
		System.out.print(DiskUtil.getJson("809563","490962"));
	 }
}
