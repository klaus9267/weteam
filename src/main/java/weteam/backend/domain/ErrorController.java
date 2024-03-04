package weteam.backend.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
  @RequestMapping("/error-weteam")
  public String errorPage() {
    return  "forward:/error.html";
  }
}
