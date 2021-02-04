package com.example.demo_web.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = {"/upload/*"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadingServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationDir = request.getServletContext().getRealPath("");

        String uploadFileDir = applicationDir + File.separator + UPLOAD_DIR + File.separator;

        File fileSaveDir = new File(uploadFileDir);
        if(!fileSaveDir.exists()){
            fileSaveDir.mkdirs();
        }
        //    System.out.println("Upload File Directory ="+fileSaveDir.getAbsolutePath());
        try {
            request.getParts().stream().forEach(part -> {
                try {
                    // part.write(uploadFilePath + File.separator + part.getSubmittedFileName());//.substring(2)
                    String path = part.getSubmittedFileName();
                    String randFilename = UUID.randomUUID() + path.substring(path.lastIndexOf("."));//
                    part.write(uploadFileDir + randFilename);
                    request.setAttribute("upload_result", " upload successfully ");
                } catch (IOException e) {
                    request.setAttribute("upload_result", " upload failed ");
                }
            });
        } catch (IllegalStateException e) {
            request.setAttribute("upload_result", " upload failed: max size");
        }
        request.getRequestDispatcher("/pages/upload_res.jsp").forward(request, response);
//        try {
//            for(Part part : request.getParts()) {
//                if (part.getSubmittedFileName() != null) {
//                   System.out.println(part.getSubmittedFileName());
//                   part.write(uploadFilePath + File.separator + part.getSubmittedFileName());
//                  // part.write("d:\\tmp\\" + part. getSubmittedFileName());
//                   response.getWriter().print(part.getSubmittedFileName() + " upload successfully");
//                }
//            }
//        } catch (IOException e) {
//            System.out.println( "------>>>>" + e);;
//        }

//        InputStream in = null;
//        try {
//            in = request.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        InputSource is = new InputSource(in);

//        File file = new File(uploadFilePath + File.separator + part.getSubmittedFileName().substring(2));
//        OutputStream out = new FileOutputStream(file);
//        byte[] buffer = new byte[1024];
//
//        int len = 0;
//        while((len=in.read(buffer))!=-1){
//            out.write(buffer, 0, len);
//        }
//        out.close();
//        in.close();
    }
}
