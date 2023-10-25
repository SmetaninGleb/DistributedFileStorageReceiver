package ru.innopolis.csn.finalproject.distributedfilestorage.receiver.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class FileServiceImpl implements FileService {

    @Value("${config.storage_directory}")
    private String fileStorageDirectory;

    @Override
    public void saveFile(byte[] fileBytes, String filename, String chatId) {
        System.out.println(fileStorageDirectory);
        System.out.println(filename);
        System.out.println(chatId);
        String finalFileName = fileStorageDirectory
                .concat("/")
                .concat(chatId)
                .concat("/")
                .concat(filename);
        try {
            String chatDirectory = fileStorageDirectory
                    .concat("/")
                    .concat(chatId);
            Files.createDirectories(Path.of(chatDirectory));
            File file = new File(finalFileName);
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(fileBytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileNames(String chatId) {
        String directoryPath = fileStorageDirectory
                .concat("/")
                .concat(chatId);
        File folder = new File(directoryPath);
        String[] fileNames = folder.list();
        if (fileNames == null) {
            return "";
        } else {
            return Arrays
                    .stream(fileNames)
                    .reduce((acc, str) -> acc.concat("\n").concat(str))
                    .get();
        }
    }

    @Override
    public byte[] getFileBytes(String filename, String chatId) {
        String filePath = fileStorageDirectory
                .concat("/")
                .concat(chatId)
                .concat("/")
                .concat(filename);
        try
        {
            return Files.readAllBytes(Path.of(filePath));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
