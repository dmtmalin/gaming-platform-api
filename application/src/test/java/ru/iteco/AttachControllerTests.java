package ru.iteco;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.iteco.config.WebSecurityConfiguration;
import ru.iteco.core.attach.AttachService;
import ru.iteco.core.attach.MalwareScanService;
import ru.iteco.core.attach.web.ApiAttachController;
import ru.iteco.core.attach.web.AttachArchiveForm;
import ru.iteco.core.attach.web.AttachIconForm;
import ru.iteco.core.attach.web.AttachScreenForm;
import ru.iteco.service.StorageService;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ApiAttachController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.iteco.core.account.security.*")})
@AutoConfigureMockMvc(secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class AttachControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private AttachService attachService;
    @MockBean
    private StorageService storageService;
    @MockBean
    private MalwareScanService malwareScanService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachIconForm attachIconForm = new AttachIconForm();
        attachIconForm.setUri("attach/icon.mock");
        AttachScreenForm attachScreenForm = new AttachScreenForm();
        attachScreenForm.setUri("attach/screen.mock");
        AttachArchiveForm attachArchiveForm = new AttachArchiveForm();
        attachArchiveForm.setUri("attach/archive.mock");
        when(attachService.attach(Matchers.isA(AttachIconForm.class))).thenReturn(attachIconForm);
        when(attachService.attach(Matchers.isA(AttachScreenForm.class))).thenReturn(attachScreenForm);
        when(attachService.attach(Matchers.isA(AttachArchiveForm.class))).thenReturn(attachArchiveForm);
    }

    @Test
    public void attachIconUnsupportedResolution() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("100x100.png").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile icon = new MockMultipartFile("file", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/icon")
                .file(icon))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/icon")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("ImageResolution")))
                .andExpect(jsonPath("errors[0].objectName", is("attachIcon")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")))
                .andExpect(jsonPath("errors[0].value", is("icon.jpeg")));
    }

    @Test
    public void attachIconUnsupportedFormat() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("150x200.jpeg").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile icon = new MockMultipartFile("file", "icon.gif", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/icon")
                .file(icon))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/icon")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("SupportedFormat")))
                .andExpect(jsonPath("errors[0].objectName", is("attachIcon")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")))
                .andExpect(jsonPath("errors[0].value", is("icon.gif")));
    }

    @Test
    public void attachIconEmptyFile() throws Exception {
        MockMultipartFile icon = new MockMultipartFile("file", new byte[] {});
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/icon")
                .file(icon))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/icon")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("FileNotEmpty")))
                .andExpect(jsonPath("errors[0].objectName", is("attachIcon")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")));
    }

    @Test
    public void attachIconSuccess() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("150x200.jpeg").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile icon = new MockMultipartFile("file", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/icon")
                .file(icon))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("uri", is("attach/icon.mock")))
                .andDo(document("attachIcon"));
    }

    @Test
    public void attachScreenUnsupportedResolution() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("150x200.jpeg").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile screen = new MockMultipartFile("file", "screen.jpeg", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/screen")
                .file(screen))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/screen")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("ImageMinResolution")))
                .andExpect(jsonPath("errors[0].objectName", is("attachScreen")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")))
                .andExpect(jsonPath("errors[0].value", is("screen.jpeg")));
    }

    @Test
    public void attachScreenUnsupportedFormat() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("640x480.png").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile screen = new MockMultipartFile("file", "screen.gif", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/screen")
                .file(screen))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/screen")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("SupportedFormat")))
                .andExpect(jsonPath("errors[0].objectName", is("attachScreen")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")))
                .andExpect(jsonPath("errors[0].value", is("screen.gif")));
    }

    @Test
    public void attachScreenEmptyFile() throws Exception {
        MockMultipartFile screen = new MockMultipartFile("file", new byte[] {});
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/screen")
                .file(screen))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/screen")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("FileNotEmpty")))
                .andExpect(jsonPath("errors[0].objectName", is("attachScreen")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")));
    }

    @Test
    public void attachScreenSuccess() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("640x480.png").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile screen = new MockMultipartFile("file", "screen.jpeg", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/screen")
                .file(screen))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("uri", is("attach/screen.mock")))
                .andDo(document("attachScreen"));
    }

    @Test
    public void attachArchiveEmptyFile() throws Exception {
        MockMultipartFile archive = new MockMultipartFile("file", new byte[] {});
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/archive")
                .file(archive))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/archive")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("FileNotEmpty")))
                .andExpect(jsonPath("errors[0].objectName", is("attachArchive")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")));
    }

    @Test
    public void attachArchiveNotZipFile() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("150x200.jpeg").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile archive = new MockMultipartFile("file", "archive.zip", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/archive")
                .file(archive))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/archive")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("FileIsZip")))
                .andExpect(jsonPath("errors[0].objectName", is("attachArchive")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("file")))
                .andExpect(jsonPath("errors[0].value", is("archive.zip")));
    }

    @Test
    public void attachArchiveWithMalware() throws Exception {
        ScanResult scanResult = new ScanResult(ScanResult.Status.VIRUS_FOUND, new HashMap<>());
        when(malwareScanService.scan(Matchers.isA(InputStream.class))).thenReturn(scanResult);
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("malware.zip").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile archive = new MockMultipartFile("file", "malware.zip", MediaType.APPLICATION_OCTET_STREAM.toString(), image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/archive")
                .file(archive))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/attach/archive")))
                .andExpect(jsonPath("exception", containsString("MalwareFoundException")));
    }

    @Test
    public void attachArchiveSuccess() throws Exception {
        ScanResult scanResult = new ScanResult(ScanResult.Status.OK, new HashMap<>());
        when(malwareScanService.scan(Matchers.isA(InputStream.class))).thenReturn(scanResult);
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("game.zip").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        MockMultipartFile archive = new MockMultipartFile("file", "malware.zip", MediaType.APPLICATION_OCTET_STREAM.toString(), image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attach/archive")
                .file(archive))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("uri", is("attach/archive.mock")))
                .andDo(document("attachArchive"));
    }
}
