package ru.iteco.core.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.iteco.core.game.web.GameVersionChangeForm;
import ru.iteco.core.game.web.GameVersionCreateForm;
import ru.iteco.error.ApplicationException;
import ru.iteco.service.StorageService;
import ru.iteco.utility.ZipUtility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class GameVersionServiceImpl implements GameVersionService {
    private final GameVersionRepository gameVersionRepository;
    private final StorageService storageService;
    private final ZipUtility zipUtility;

    @Autowired
    public GameVersionServiceImpl(GameVersionRepository gameVersionRepository,
                                  StorageService storageService,
                                  ZipUtility zipUtility) {
        this.gameVersionRepository = gameVersionRepository;
        this.storageService = storageService;
        this.zipUtility = zipUtility;
    }

    @Override
    public GameVersion create(GameVersionCreateForm form) throws IOException {
        Integer gameId = form.getGameId();
        GameVersion gameVersion = gameVersionRepository.findFirstByGameFkOrderByBuildDesc(gameId);
        Integer build = (gameVersion == null) ? 1 : gameVersion.getBuild() + 1;
        gameVersion = new GameVersion();
        gameVersion.setBuild(build);
        gameVersion.setGameFk(gameId);
        gameVersion.setWhatNew(form.getWhatNew());
        gameVersion.setStatus(GameVersionStatus.MODERATION);
        Boolean isRemote = !StringUtils.isEmpty(form.getRemoteUrl());
        gameVersion.setRemote(isRemote);
        if (isRemote) {
            gameVersion.setUri(form.getRemoteUrl());
        }
        else {
            String archiveUrl = form.getArchiveUrl();
            String uploadPrefix = GameVersionCreateForm.UPLOAD_PREFIX + gameVersion.getGameFk() +
                    "/build/" + gameVersion.getBuild() + "/";
            Resource archive = storageService.load(archiveUrl);
            InputStream archiveStream = archive.getInputStream();
            List<String> storageFiles = unpackAndSaveFilesToStorage(archiveStream, uploadPrefix);
            String entryFile = findEntryHtmlFile(storageFiles);
            gameVersion.setUri(entryFile);
            archiveStream.close();
            storageService.delete(archiveUrl);
        }
        return gameVersionRepository.save(gameVersion);
    }

    @Override
    public GameVersion update(GameVersion gameVersion, GameVersionChangeForm form) {
        gameVersion.setWhatNew(form.getWhatNew());
        gameVersion.setStatus(form.getStatus());
        gameVersion.setReason(form.getReason());
        return gameVersionRepository.save(gameVersion);
    }

    private List<String> unpackAndSaveFilesToStorage(InputStream inputStream, String prefix) throws IOException {
        List<String> storageFilenames = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry = zipInputStream.getNextEntry();
        while(entry != null) {
            if(!entry.isDirectory()) {
                byte[] content = zipUtility.unpackFile(zipInputStream);
                String filename = entry.getName();
                String storageFilename = storageService.store(filename, content, prefix);
                storageFilenames.add(storageFilename);
            }
            zipInputStream.closeEntry();
            entry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return storageFilenames;
    }

    private String findEntryHtmlFile(List<String> files) {
        for(String file: files) {
            if(file.contains("index.html")) {
                return file;
            }
        }
        throw new ApplicationException("Can't find index.html in files: " + String.join(", ", files));
    }
}
