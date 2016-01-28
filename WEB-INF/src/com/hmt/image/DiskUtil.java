package com.hmt.image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import net.sf.json.JSONObject;

public class DiskUtil {
	private static String CACHE_PATH="Cache";
	private static String JSON_CACHE="json";
	private static String IMAGE_CACHE="image";
	private static String CACHE_INFO="cacheInfo";
	private static int CACHE_SIZE=100*1024;
	private static int MAX_IMAGES=1000;
	private static String IMAGE_SIZE_KEY="imagesize";
	
public static void saveJson(String json,String tid,String aid,File dir){
	int cachesize=(int)getDirSize(getJsonCacheDir());
	if (cachesize>CACHE_SIZE) {
		deleteDir(getJsonCacheDir());
		
	}
	  FileOutputStream o=null;  
	  try {  
	   o = new FileOutputStream(new File(dir,hashKeyForDisk(tid+aid)));  
	      o.write(json.getBytes("GBK"));  
	      o.close();  
	  } catch (Exception e) {  
	   // TODO: handle exception  
	   e.printStackTrace();  
	  }finally{  
	
	  } 
	
	
}
public static String getJson(String tid,String aid){
	File fileName=new File(getJsonCacheDir(),hashKeyForDisk(tid+aid));

	  StringBuilder builder=new StringBuilder();
	  FileReader fileReader=null;  
	  BufferedReader bufferedReader=null;  
	  try{  
	   fileReader=new FileReader(fileName);  
	   bufferedReader=new BufferedReader(fileReader);  
	   try{  
	    String read=null;  
	  
	    while((read=bufferedReader.readLine())!=null){ 
	    	builder.append(read);
	
	    }  
	   }catch(Exception e){  
	    e.printStackTrace();  
	   }  
	  }catch(Exception e){  
	   e.printStackTrace();  
	  }finally{  
	   if(bufferedReader!=null){  
	    try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   }  
	   if(fileReader!=null){  
	    try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   }  
	  }  
  
	  return builder.toString();
	
}
public static void saveImage(BufferedImage src,String tid,String aid){
	JSONObject	 json=getInfo();
if (json!=null) {
	
	if (Integer.parseInt((String)json.get(IMAGE_SIZE_KEY))>=MAX_IMAGES){
		deleteFile(CACHE_PATH);
		
	}
	 
}
     
         saveInfo(1);
	
	   File file=new File(DiskUtil.getImageCacheDir(),DiskUtil.hashKeyForDisk(tid+aid)+".jpg");
    try {
		throw new Exception(DiskUtil.getImageCacheDir().toString());
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   
	   try {
		ImageIO.write(src, "jpg",  file);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public static FileImageInputStream getImage(String width,String tid,String aid){
	FileImageInputStream in = null;
	
	try {
		 in=new FileImageInputStream(new File(getImageCacheDir(),hashKeyForDisk(width+tid+aid)+".jpg"));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return in;
}

public static File getImageCacheDir() {  
    String cachePath=CACHE_PATH;  
   String uniqueName=IMAGE_CACHE;
    File file=new File(cachePath + File.separator + uniqueName);  
  
    if (!file.exists()) {  
    	
    	file.mkdirs();  
    }
    return file;
} 

public static File getJsonCacheDir() {  
    String cachePath=CACHE_PATH;  
   String uniqueName=JSON_CACHE;
   File file=new File(cachePath + File.separator + uniqueName);  
   
   if (!file.exists()) {  
   	
   	file.mkdirs();  
   }
   return file; 
} 

public static File getInfoDir() {  
    String cachePath=CACHE_PATH;  
   String uniqueName=CACHE_INFO;
   File file=new File(cachePath);  
   
   if (!file.exists()) {  
   	
   	file.mkdirs();  
   }
   return file; 
} 

public static String hashKeyForDisk(String key) {  
    String cacheKey;  
    try {  
        final MessageDigest mDigest = MessageDigest.getInstance("MD5");  
        mDigest.update(key.getBytes());  
        cacheKey = bytesToHexString(mDigest.digest());  
    } catch (NoSuchAlgorithmException e) {  
        cacheKey = String.valueOf(key.hashCode());  
    }  
    return cacheKey;  
}  
  
private static String bytesToHexString(byte[] bytes) {  
    StringBuilder sb = new StringBuilder();  
    for (int i = 0; i < bytes.length; i++) {  
        String hex = Integer.toHexString(0xFF & bytes[i]);  
        if (hex.length() == 1) {  
            sb.append('0');  
        }  
        sb.append(hex);  
    }  
    return sb.toString();  
}

private static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
        String[] children = dir.list();

        for (int i=0; i<children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }
   
    return dir.delete();
}

private static void deleteFile(String file){
	deleteDir(new File(file));
	
}
public static double getDirSize(File file) {     
    //判断文件是否存在     
    if (file.exists()) {     
        //如果是目录则递归计算其内容的总大小    
        if (file.isDirectory()) {     
            File[] children = file.listFiles();     
            double size = 0;     
            for (File f : children)     
                size += getDirSize(f);     
            return size;     
        } else {//如果是文件则直接返回其大小,以“兆”为单位   
            double size = (double) file.length() / 1024 / 1024;        
            return size;     
        }     
    } else {     
        System.out.println("文件或者文件夹不存在，请检查路径是否正确！");     
        return 0.0;     
    }     
}

public static void saveInfo(int size){
	File infoFile=new File(getInfoDir(),CACHE_INFO);
	if (!infoFile.exists()){
		net.sf.json.JSONObject jb=new JSONObject();
		
		jb.put(IMAGE_SIZE_KEY, String.valueOf(size));
		  FileOutputStream o=null;  
		  try {  
		   o = new FileOutputStream(infoFile);  
		      o.write(jb.toString().getBytes("GBK"));  
		      o.close();  
		  } catch (Exception e) {  
		   // TODO: handle exception  
		   e.printStackTrace();  
		  }finally{  
		
		  }
		
	}
	else {
	      net.sf.json.JSONObject jb = getInfo();
	      if (jb==null) {
	    	  return;
			
		}
		  String msize=jb.getString(IMAGE_SIZE_KEY);
		  size+=Integer.parseInt(msize);
		  jb.put(IMAGE_SIZE_KEY, String.valueOf(size));
		  FileOutputStream o=null;  
		  try {  
		   o = new FileOutputStream(new File(CACHE_PATH,CACHE_INFO));  
		      o.write(jb.toString().getBytes("GBK"));  
		      o.close();  
		  } catch (Exception e) {  
		   // TODO: handle exception  
		   e.printStackTrace();  
		  }finally{  
		
		  }
	}
	
 
}

public static JSONObject getInfo(){
	File infoFile=new File(getInfoDir(),CACHE_INFO);
	
	if (!infoFile.exists()){
		System.out.println("\ninfo file not exists!");
		return null;
		
		
	}
	 StringBuilder builder=new StringBuilder();
	  FileReader fileReader=null;  
	  BufferedReader bufferedReader=null;  
	  try{  
	   fileReader=new FileReader(infoFile);  
	   bufferedReader=new BufferedReader(fileReader);  
	   try{  
	    String read=null;  
	  
	    while((read=bufferedReader.readLine())!=null){ 
	    	builder.append(read);
	
	    }  
	   }catch(Exception e){  
	    e.printStackTrace();  
	   }  
	  }catch(Exception e){  
	   e.printStackTrace();  
	  }finally{  
	   if(bufferedReader!=null){  
	    try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   }  
	   if(fileReader!=null){  
	    try {
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	   }  
	  }  
 
	 
	
	
	  net.sf.json.JSONObject jb = net.sf.json.JSONObject.fromObject(builder.toString());
	  return jb;
	
	
}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
