package upload.file.test.controller;

import io.lvdaxian.upload.file.expose.UploadFileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
  
  @Resource
  private UploadFileUtils fileUtils;
  
  @GetMapping("/getFile/{filename}")
  public String getFile(@PathVariable("filename") String filename) {
    fileUtils.getMultipartFileByName(filename);
    return null;
  }
}
