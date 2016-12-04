package com.kaishengit.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
//乱码啦
@WebServlet("/upload")
public class FileuploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/views/upload.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//�ļ��ϴ����ŵ�·��
		File saveDir = new File("F:/upload");
		if(!saveDir.exists()){
			saveDir.mkdirs();
		}
		//�ļ���ŵ���ʱ·����
		File temDir = new File("F:/temDir");
		if(!temDir.exists()){
			temDir.mkdirs();
		}
		
		//�жϱ��Ƿ�����enctype����
		if(ServletFileUpload.isMultipartContent(req)){
			DiskFileItemFactory itemFactory = new DiskFileItemFactory();
			itemFactory.setSizeThreshold(1024*1024);
			itemFactory.setRepository(temDir);
			
			ServletFileUpload servletfileupload = new ServletFileUpload(itemFactory);
			servletfileupload.setSizeMax(1024*1024*10);
			
			try {
				List<FileItem> fileItemList = servletfileupload.parseRequest(req);
				for(FileItem item:fileItemList){
					if(item.isFormField()){
						//��ͨԪ��
						System.out.println("FieldName:" + item.getFieldName());
						System.out.println("getString:" + item.getString("UTF-8"));
					}else{
						 //�ļ�Ԫ��
                        System.out.println("FieldName:" + item.getFieldName()); //��ȡ����name���Ե�ֵ
                        System.out.println("Name:" + item.getName()); //��ȡ�ϴ��ļ���ԭʼ����(�ļ���)
                        
                        //��ȡ�ļ���������
                        InputStream inputstream=item.getInputStream();
                        String filename = item.getName();
                        String Newfilename = UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
                        
                        FileOutputStream outputstream = new  FileOutputStream(new File(saveDir,Newfilename));
                        IOUtils.copy(inputstream, outputstream);
                        outputstream.flush();
                        outputstream.close();
                        inputstream.close();
					}
				}
			} catch (FileUploadException e) {
				
				e.printStackTrace();
			}
			
		}else{
			throw new RuntimeException("form��enctype���������쳣");
		}
		
	}

}
