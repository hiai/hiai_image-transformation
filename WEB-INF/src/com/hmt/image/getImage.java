package com.hmt.image;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author hiai
 * 
 * 客户端获取图片的类
 *
 */
public class getImage extends  HttpServlet{
/**
     *
     */
    private static final long serialVersionUID = 1L;
public void doGet(HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
	request.setCharacterEncoding("gb2312");
	String width=request.getParameter("width");
	String tid=request.getParameter("tid");
	String aid=request.getParameter("aid");
//	
	ImageNetworkUtil.loadImage(width,tid,aid);
//
	 OutputStream os = response.getOutputStream();   
	 response.setContentType("image/jpeg"); //设置返回的文件类型     
	 
	FileImageInputStream imageInputStream=(FileImageInputStream)DiskUtil.getImage(width, tid, aid);
	int b;  
     while ((b = imageInputStream.read()) != -1) {  
         os.write(b);  
     }  
   
     
    os.flush();    
    os.close();  
    imageInputStream.close();

  

}

public static void main(String[] args) {
	//System.out.print("\n"+ ImageNetworkUtil.loadImage("301","576714","462270"));
	
	

	 
 }
}