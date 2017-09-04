package ru.iteco.core.attach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iteco.core.attach.web.AttachObjectForm;
import ru.iteco.service.StorageService;

@Service
public class AttachServiceImpl implements AttachService {

    private final StorageService storageService;

    @Autowired
    public AttachServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public AttachObjectForm attach(AttachObjectForm form) {
        String url = storageService.store(form.getFile(), "attach/");
        form.setUri(url);
        return form;
    }
}
