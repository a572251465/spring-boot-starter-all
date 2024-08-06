package upload.file.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  
  
  @GetMapping("/getFile/{filename}")
  public String getFile(@PathVariable("filename") String filename) {
    return null;
  }
}
