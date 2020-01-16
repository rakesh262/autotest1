package sqs.core.utils;

import org.apache.log4j.Logger;
import sqs.framework.FrameworkData;

import java.io.*;
import java.nio.file.*;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;
import static sqs.framework.FrameworkData.frameworkData;

@SuppressWarnings("all")
public class FileUtils {
    private static Logger logger = Logger.getLogger(FileUtils.class);

    private FileUtils() {

    }

    public static boolean isFileExists(String filePath) {
        try {
            filePath = getAbsolutePath(filePath);
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            throw exception;
        }
        return false;
    }

    public static boolean checkFileExists(String fileFullName, String fileDescription) {
        boolean isFileFound = false;
        isFileFound = isFileExists(fileFullName);
        if (!isFileFound) {
            try {
                logger.error(fileDescription + " not found:" + fileFullName);
                throw new FileNotFoundException("e");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return isFileFound;
    }
    public static boolean checkFileOrFolderExists(String filePath, String fileDescription) {
        boolean isFileFound = false;
        try {
            filePath = getAbsolutePath(filePath);
            File file = new File(filePath);
            if (file.isFile() || file.isDirectory()) {
                return true;
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            throw exception;
        }
        return false;
        }


    public static String getParrentFolderPath(String filePath) {
        String parentPath = "";
        try {
            filePath = getAbsolutePath(filePath);
            File file = new File(filePath);
            if (file.exists()) {
                parentPath = file.getParent();
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);

        }
        return parentPath;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createFileDeleteIfExists(String filePath) {

        try {
            File file = new File(filePath);
            if (isFileExists(filePath)) {
                Files.delete(Paths.get(getAbsolutePath(filePath)));
                file.createNewFile();
            } else {
                file.createNewFile();
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }
    }


    public static void createFileIfNotExists(String fileFullName) {

        try {
            if (!fileFullName.isEmpty()) {
                File file = new File(fileFullName);
                if (!isFileExists(fileFullName)) {
                    file.createNewFile();
                }
            }
        } catch (IOException e) {
            if (e.getMessage().contains("The system cannot find the path specified")) {
                try {
                    throw new FileNotFoundException("Path Not Found to Create the file:" + fileFullName);
                } catch (FileNotFoundException exception) {
                    logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
                }
            } else {
                try {
                    throw e;
                } catch (IOException exception) {
                    logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
                }
            }
        }
    }

    public static void writeTextNewFile(String txtToWrite, String fileFullName) throws Exception {
        try (PrintWriter writer = new PrintWriter(fileFullName)) {
            createFileDeleteIfExists(fileFullName);
            writer.println(txtToWrite);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void writeTextExistingFile(String fileFullName, String txtToWrite) {
        try {
            txtToWrite = txtToWrite + System.getProperty("line.separator");
            logger.debug("Text to Write:" + txtToWrite.replaceAll("\\r\\n|\\r|\\n", " "));
            logger.debug("Write In:" + fileFullName);
            Files.write(Paths.get(fileFullName), txtToWrite.getBytes(), StandardOpenOption.APPEND);
            logger.debug("Text writed on the file:" + fileFullName);
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }
    }

    public static String getTextFromFile(String fileFullName) {
        StringBuilder fileContent = new StringBuilder();
        String currentLine;
        if (isFileExists(fileFullName)) {
            try (FileReader fileReader = new FileReader(fileFullName)) {
                BufferedReader buff = new BufferedReader(fileReader);
                while ((currentLine = buff.readLine()) != null) {
                    fileContent.append(currentLine);
                }
            } catch (Exception exception) {
                logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            }
        }
        return fileContent.toString();
    }

    public static String getFirstLineTextFromFile(String fileFullName)
    {
        String firstLineContent = "";
        String currentLine;
        if (isFileExists(fileFullName)) {
            try (FileReader fileReader = new FileReader(fileFullName)) {
                BufferedReader buff = new BufferedReader(fileReader);
                currentLine = buff.readLine();
                firstLineContent = currentLine;
                buff.close();
            } catch (Exception exception) {
                logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            }
        }
        return firstLineContent;
    }

    public static boolean isFileContainsText(String fileFullName, String textToFind)  {
        String text = getTextFromFile(fileFullName);
        boolean isTextFoundInFile = false;
        if ((text != null) && (text.toLowerCase().contains(textToFind.toLowerCase()))) {
            isTextFoundInFile = true;
        }
        return isTextFoundInFile;
    }

    public static boolean isFileEmpty(String fileFullName) {
        boolean isFileEmpty = true;
        try {
            String text = getTextFromFile(fileFullName);
            text = Utilities.trimString(text);

            if (!text.isEmpty()) {
                isFileEmpty = false;
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }
        return isFileEmpty;
    }

    public static void removeLineContainsText(String fileFullName, String textToFind)  {
        String reasonToLock = "To Remove line which contains the text '" + textToFind + "'";
        if (isFileExists(fileFullName)) {
            try {
                waitAndLockTheFile(fileFullName, reasonToLock, false);
                File inputFile = null;
                String fileName = null;
                File tempFile = null;
                try {
                    inputFile = new File(fileFullName);
                    fileName = inputFile.getName();
                    tempFile = new File(fileFullName.replaceAll(fileName, "temp_" + fileName));
                    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                            String currentLine;
                            while ((currentLine = reader.readLine()) != null) {
                                currentLine = currentLine.trim();
                                if (!currentLine.equals("") && (!currentLine.contains(textToFind))) {
                                    writer.write(currentLine + System.getProperty("line.separator"));
                                }
                            }
                        }
                    }
                    Files.delete(Paths.get(fileFullName));
                    tempFile.renameTo(inputFile);
                } catch (Exception exception) {
                    logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
                }
                releaseFileLock(fileFullName, reasonToLock);
            } catch (Exception exception) {
                logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            }
        }
    }

    public static void printTextFromFile(String fileFullName) {
        File inputFile = new File(fileFullName);
        logger.info("File to read:" + fileFullName);
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                logger.info("Text readed From File is:" + trimmedLine);
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }
    }

    public static void printBlockedDeviceDetails(String fileFullName)  {
        File inputFile = new File(fileFullName);
        boolean isDeviceInRunningState = false;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String currentLine;
            logger.debug("List of devices in execution state.");
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(",")) {
                    CSVUtilities.printResourceName(currentLine.split(",")[0]);
                    isDeviceInRunningState = true;
                }
            }
            if (isDeviceInRunningState) {
                logger.debug("If about Device is not in Execution, Kindly remove udid from " + fileFullName + " file");
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }
    }

    public static void releaseFileLock(String filePath, String reasonForLock) {
        String lockFile = filePath.replace("." + getExtension(filePath), ".lck");
        String reasonToLockFile = reasonForLock + " < Locked by Session" + FrameworkData.getTestSuiteIDForCurrentJVM() + ">";
       try{
           if (isFileContainsText(lockFile, reasonToLockFile)) {
               File lckfile = new File(lockFile);
               Files.delete(Paths.get(lckfile.getAbsolutePath()));
               logger.debug("File Released for Editing:" + filePath);
               logger.debug("Lock file deleted:" + lockFile);
           } else {
               logger.error("File is not locked for :" + reasonToLockFile + " Not able to Release:" + filePath);
               logger.debug("Lock was not released due to invalid reason");
           }
       } catch (Exception exception) {
           logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
       }
    }

    public static void waitAndLockTheFile(String filePath, String reasonForLock, boolean playBeepIfAlreadyLocked) {

        String reasonToLockFile = reasonForLock + " < Locked by Session" + FrameworkData.getTestSuiteIDForCurrentJVM() + ">";
        try {
            if (filePath != null && filePath.isEmpty()) {
                logger.error("File Path not given to find");
            } else {
                logger.debug("Wait and Lock: " + filePath);
                File file = new File(filePath);
                if (file.exists()) {
                    String lockFile = filePath.replace("." + getExtension(filePath), ".lck");
                    File lckfile = new File(lockFile);
                    String lockedReason;
                    while (lckfile.exists()) {
                        try {
                            lockedReason = getTextFromFile(lockFile);
                        } catch (Exception e) {
                            logger.debug("lock file Is released. trying to lock...");
                            lockedReason = "";
                        }
                        if (!lockedReason.isEmpty()) {
                            logger.debug("File Is already in Locked State for:" + lockedReason);
                            if (playBeepIfAlreadyLocked) {
                                Utilities.playBeepSound();
                            }
                            Utilities.waitFor(500);
                        }
                    }
                    try {
                        lckfile.createNewFile();
                        writeTextExistingFile(lockFile, reasonToLockFile);
                        Utilities.waitFor(200);
                    } catch (Exception exception) {
                        logger.error("Exception on isCheckBoxSelected :" ,exception);
                    }
                    String lockComments = getFirstLineTextFromFile(lockFile);
                    if (lockComments.contains(reasonToLockFile)) {
                        logger.debug("File Locked " + reasonToLockFile + ":" + filePath);
                        printTextFromFile(lockFile);
                    } else {
                        logger.error("File Is not yet locked Retry...");
                        waitAndLockTheFile(filePath, reasonForLock, playBeepIfAlreadyLocked);
                    }
                } else {
                    logger.error("File Not Found:" + file.getAbsolutePath());
                }
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
        }

    }

    public static String getAbsolutePath(String path) {
        if (!FrameworkData.isWindows()) {
            path = path.replaceFirst("(\\\\\\d+.\\d+.\\d+.\\d+)", "Volumes");
            path = path.replaceFirst("(/\\d+.\\d+.\\d+.\\d+)", "Volumes");
            path = path.replace("\\", "/");
        }
        return path;
    }

    public static String getExtension(String fileFullName) {
        String extension;
        extension = fileFullName.substring(fileFullName.lastIndexOf('.') + 1);
        return extension;
    }

    public static void createFolderIfNotExist(String filePath)  {
        if (!isFileExists(filePath)) {
            boolean isFolderCreated = (new File(filePath)).mkdirs();
            if (!isFolderCreated) {
                try {
                    throw new Exception("Folder is not created in the" + filePath);
                } catch (Exception exception) {
                    logger.error(EXCEPTION_ON + Utilities.getCallerMethodName(),exception);
                }
            }
        }

    }
}