package ru.innopolis.csn.finalproject.distributedfilestorage.receiver.services;

import java.io.File;

public interface FileService {
    void saveFile(byte[] fileBytes, String filename, String chatId);
    String getFileNames(String chatId);
    byte[] getFileBytes(String filename, String chatId);
}
