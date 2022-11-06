package platform;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class WebAPIController {

    @Resource
    private CodeService codeService;

    @GetMapping(value = "/code/{uuid}")
    public ModelAndView getHtml(@PathVariable String uuid) {
        return  codeService.getHtml(uuid);
    }

    @GetMapping("/api/code/{uuid}")
    @ResponseBody
    public CodeInfo getJson(@PathVariable String uuid) {
        return  codeService.getJson(uuid);
    }

    @GetMapping("/code/new")
    public ModelAndView getFormHtml() {
        return codeService.getFormHtml();
    }

    @PostMapping("/api/code/new")
    @ResponseBody
    public CodeNrResponse newJson(@RequestBody CodeInfo codeInfo) {
        return codeService.newJson(codeInfo);
    }

    @GetMapping("/api/code/latest")
    @ResponseBody
    public List<CodeInfo> getLatestJson() {
       return codeService.getLatestJson();
    }

    @GetMapping(value = "/code/latest")
    public ModelAndView getLatestHtml() {
        return codeService.getLatestHtml();
    }
}
