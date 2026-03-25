package org.example.boot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test controller with @Controller annotation
 */
@Controller
public class TestController {
    
    @GetMapping("/test")
    public ModelAndView test(@RequestParam("name") String name) {
        ModelAndView model = new ModelAndView("test");
        model.addObject("message", "Hello, " + name);
        return model;
    }
}
