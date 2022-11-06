package platform;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodeService {
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    @JsonSerialize
    public class EmptyJsonResponse {
    }

    @Resource
    CodeRepository codeRepository;

    @Resource
    CodeMapper codeMapper;

    public CodeNrResponse newJson(CodeInfo codeInfo) {
        Code code = codeMapper.codeInfoToCode(codeInfo);
        LocalDateTime dateTime = LocalDateTime.now();
        code.setStartTime(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        code.setDate(dateTime.format(formatter));
        String generatedToken = UUID.randomUUID().toString();
        code.setUuid(generatedToken);
        code.setShowTime(codeInfo.getTime());
        code.setTimeLimited(codeInfo.getTime() >= 1);
        code.setViewLimited(codeInfo.getViews() >= 1);
        codeRepository.save(code);
        return new CodeNrResponse(generatedToken);
    }

    public ModelAndView getHtml(String uuid) {
        Optional<Code> code = Optional.ofNullable(codeRepository.findByUuid(uuid));
        if (code.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        boolean b = codeOperations(code.get());
        if (b) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            code = Optional.ofNullable(codeRepository.findByUuid(uuid));
            CodeInfo codeInfo = codeMapper.codeToCodeInfo(code.get());
            Boolean timeLimited = code.get().getTimeLimited();
            ModelAndView model = new ModelAndView();
            Boolean viewLimited = code.get().getViewLimited();
            model.addObject("code", codeInfo);
            model.addObject("timeLimited", timeLimited);
            model.addObject("viewLimited", viewLimited);
            model.setViewName("code");
            return model;
        }
    }

    public CodeInfo getJson(String uuid) {
        Optional<Code> code = Optional.ofNullable(codeRepository.findByUuid(uuid));
        if (code.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        boolean b = codeOperations(code.get());
        if (b) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            code = Optional.ofNullable(codeRepository.findByUuid(uuid));
            return codeMapper.codeToCodeInfo(code.get());
        }
    }

    public ModelAndView getFormHtml() {
        ModelAndView model = new ModelAndView();
        model.setViewName("send");
        return model;
    }

    public List<CodeInfo> getLatestJson() {
        return getCodes();
    }

    public ModelAndView getLatestHtml() {
        ModelAndView model = new ModelAndView();
        model.addObject("codes", getCodes());
        model.setViewName("latest");
        return model;
    }

    private List<CodeInfo> getCodes() {
        Iterable<Code> codes = codeRepository.findAll();
        List<CodeInfo> objects = new ArrayList<>();
        for (var entry : codes) {
            if (!entry.getViewLimited() && !entry.getTimeLimited()) {
                CodeInfo codeInfo = codeMapper.codeToCodeInfo(entry);
                objects.add(codeInfo);
            }
        }
        Collections.reverse(objects);
        return objects.stream().limit(10).collect(Collectors.toList());
    }

    public boolean codeOperations(Code code) {
        boolean del = false;
        boolean restrictedByTime = false;
        boolean restrictedByViews = false;
        long time;
        long secondsDiff = Math.abs(Duration.between(code.getStartTime(), LocalDateTime.now()).getSeconds());
        time = Math.max(0L, code.getTime() - secondsDiff);
        if (time == 0L && code.getTimeLimited()) {
            restrictedByTime = true;
        } else if (time > 0L && code.getTimeLimited()) {
            code.setShowTime(time);
            codeRepository.save(code);
        }
        if (code.getViews() == 0 && code.getViewLimited()) {
            restrictedByViews = true;
        } else if (code.getViews() > 0 && code.getViewLimited()) {
            code.setViews(code.getViews() - 1);
            codeRepository.save(code);
        }
        if (restrictedByTime || restrictedByViews) {
            codeRepository.delete(code);
            del = true;
        }
        return del;
    }
}
