package com.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;



//url:  http://localhost:8080/HelloSpringMVC/hello/mvc

@Controller  //告诉DispatcherServlet相关的容器， 这是一个Controller，
@RequestMapping(value="/hello")  //类级别的RequestMapping，告诉DispatcherServlet由这个类负责处理以及URL。HandlerMapping依靠这个标签来工作
public class HelloController {
	
	private static Logger log = LoggerFactory.getLogger(HelloController.class);
		
	//方法级别的RequestMapping， 限制并缩小了URL路径匹配，同类级别的标签协同工作，最终确定拦截到的URL由那个方法处理
	//并指定访问方法为GET
	    @RequestMapping(value="/mvc",method=RequestMethod.GET)  
	    public String HelloWorld(Model model){  
		  
	        model.addAttribute("message","Hello Spring MVC!!!");  //传参数给前端
	        
	      //视图渲染，/WEB-INF/view/HelloWorld.jsp
	        return "HelloWorld";  //页面的名称，根据此字符串会去寻找名为HelloWorld.jsp的页面
	    }  
	    
	  //定位到上传文件界面 /hello/upload
		@RequestMapping(value="/upload", method=RequestMethod.GET)
		public String showUploadPage(){	
			return "uploadFile";		 //上传单个文件
		}
		
		/**
		 * 上传单个文件操作
		 * @param multi
		 * @return
		 */
		@RequestMapping(value="/doUpload", method=RequestMethod.POST)
		public String doUploadFile(@RequestParam("file") MultipartFile file){

			if(!file.isEmpty()){
				log.debug("Process file: {}", file.getOriginalFilename());
				try {
					//这里将上传得到的文件保存至 d:\\temp\\file 目录
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File("d:\\temp\\file\\", 
							System.currentTimeMillis()+ file.getOriginalFilename()));
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e.toString());
				}
			}

			return "success";
		}
		
		//定位到上传多个文件界面 /hello/uploadMulti
		@RequestMapping(value="/uploadMulti", method=RequestMethod.GET)
		public String showUploadPage2(){	
			return "uploadMultifile";		 //view文件夹下的上传多个文件的页面
		}
		
		
		/**
		 * 上传多个附件的操作类
		 * @param multiRequest
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value="/doMultiUpload", method=RequestMethod.POST)
		public String doUploadFile2(MultipartHttpServletRequest multiRequest) throws IOException{

			Iterator<String> filesNames = multiRequest.getFileNames();
			while(filesNames.hasNext()){
				String fileName =filesNames.next();
				MultipartFile file =  multiRequest.getFile(fileName);
				if(!file.isEmpty()){
					log.debug("Process file: {}", file.getOriginalFilename());
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File("d:\\temp\\file\\", 
							System.currentTimeMillis()+ file.getOriginalFilename()));
				}

			}

			return "success";
		}
		
		
}
