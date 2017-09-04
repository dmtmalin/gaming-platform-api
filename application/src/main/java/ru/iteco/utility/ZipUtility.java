package ru.iteco.utility;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ZipUtility {

	public byte[] unpackFile(ZipInputStream zipInputStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(zipInputStream, outputStream);
		byte[] bytes = outputStream.toByteArray();
		outputStream.close();
		return bytes;
	}

	public boolean containsFile(InputStream inputStream, String filename) throws IOException {
		ZipInputStream zipStream = new ZipInputStream(inputStream);
		ZipEntry entry = zipStream.getNextEntry();
		boolean isFindFile = false;
		while (entry != null) {
			if (!entry.isDirectory()) {
				String name = FilenameUtils.getName(entry.getName());
				if (name.equals(filename)) {
					isFindFile = true;
					break;
				}
			}
			zipStream.closeEntry();
			entry = zipStream.getNextEntry();
		}
		zipStream.close();
		return isFindFile;
	}
}
