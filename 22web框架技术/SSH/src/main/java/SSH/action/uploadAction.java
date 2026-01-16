package SSH.action;

import SSH.utils.AliOssUtil;
import com.opensymphony.xwork2.ActionSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.apache.struts2.dispatcher.multipart.UploadedFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class uploadAction extends ActionSupport {
    private File file;
    private String fileFileName;
    private String fileContentType;

    private AliOssUtil aliOssUtil;

    public void setAliOssUtil(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    public String upload() throws IOException {
        try {
            String originalFileName = file.getName(); // 获取文件名
            String extension = FilenameUtils.getExtension(originalFileName); // 获取文件扩展名
            //构建新文件名称
            String objectName = UUID.randomUUID()+ extension;
            //将file转为MultipartFile
            MultipartFile multipartFile = getMultipartFile(file);
            //文件请求路径
            String filePath = aliOssUtil.upload(multipartFile.getBytes(), objectName);
            //存入session
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("FilePath", filePath);
            return SUCCESS;
        } catch (IOException e) {
            e.toString();
        }
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("error", "上传失败");
        return INPUT;
    }
    //将file转为MultipartFile
    public static MultipartFile getMultipartFile (File file){

        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }


}