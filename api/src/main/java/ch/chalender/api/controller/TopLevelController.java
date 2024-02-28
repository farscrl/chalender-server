package ch.chalender.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@Tag(name = "Contact form", description = "Contact form API")
public class TopLevelController {
    @RequestMapping("/")
    public String home(){
        return "chalender.ch API";
    }

    @RequestMapping(value={"/robots.txt", "/robot.txt"})
    @ResponseBody
    public String getRobotsTxt() {
        return """
                User-agent: *
                Disallow: /
                """;
    }
}
