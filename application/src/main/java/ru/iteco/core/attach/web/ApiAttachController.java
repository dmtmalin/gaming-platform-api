package ru.iteco.core.attach.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.core.attach.AttachService;
import ru.iteco.core.attach.MalwareScanService;
import ru.iteco.error.MalwareFoundException;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import javax.validation.Valid;
import java.lang.reflect.Method;

@Controller
@RequestMapping(value = "/attach")
public class ApiAttachController {

    private final AttachService attachService;
    private final MalwareScanService malwareScanService;

    @Autowired
    public ApiAttachController(AttachService attachService, MalwareScanService malwareScanService) {
        this.attachService = attachService;
        this.malwareScanService = malwareScanService;
    }

    @PostMapping(value = "/icon")
    @ResponseBody
    public AttachObjectForm attachIcon(
            @Valid @ModelAttribute("attachIcon") AttachIconForm attachIconForm,
            BindingResult bindingResult) throws Exception {
        Method thisMethod = this.getClass().getDeclaredMethod(
                "attachIcon", AttachIconForm.class, BindingResult.class);
        return attachObject(attachIconForm, thisMethod, bindingResult);
    }

    @PostMapping(value = "/archive")
    @ResponseBody
    public AttachObjectForm attachArchive(
            @Valid @ModelAttribute("attachArchive") AttachArchiveForm attachArchiveForm,
            BindingResult bindingResult) throws Exception {
        Method thisMethod = this.getClass().getDeclaredMethod(
                "attachArchive", AttachArchiveForm.class, BindingResult.class);
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(thisMethod, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        MultipartFile archive = attachArchiveForm.getFile();
        ScanResult scanResult = malwareScanService.scan(archive.getInputStream());
        ScanResult.Status status = scanResult.getStatus();
        if (status == ScanResult.Status.VIRUS_FOUND) {
            throw new MalwareFoundException(scanResult.toString());
        }
        return attachObject(attachArchiveForm, thisMethod, bindingResult);
    }

    @PostMapping(value = "/screen")
    @ResponseBody
    public AttachObjectForm attachScreen(
            @Valid @ModelAttribute("attachScreen") AttachScreenForm attachScreenForm,
            BindingResult bindingResult) throws Exception {
        Method thisMethod = this.getClass().getDeclaredMethod(
                "attachScreen", AttachScreenForm.class, BindingResult.class);
        return attachObject(attachScreenForm, thisMethod, bindingResult);
    }

    private AttachObjectForm attachObject (AttachObjectForm object, Method method, BindingResult bindingResult)
            throws Exception {
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        return attachService.attach(object);
    }
}
