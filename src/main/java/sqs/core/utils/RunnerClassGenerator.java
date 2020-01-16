package sqs.core.utils;

import org.apache.log4j.Logger;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import sqs.framework.FrameworkData;
import sqs.framework.FrameworkBase;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import static sqs.core.constants.Constants.EXCEPTION_ON;

public class RunnerClassGenerator {
    private static Logger logger = Logger.getLogger(RunnerClassGenerator.class);

    private RunnerClassGenerator() {
        throw new IllegalStateException("RunnerClassGenerator class");
    }

    public static boolean generateRunnerClass(String featureFilePath, String tag, String reportFolderPath) throws IOException {
        boolean errorOccured=false;
        try {
            StringBuilder sourceCode = new StringBuilder();
            File sourceFile = File.createTempFile("CucumberRunner", ".java");
            String classname = sourceFile.getName().split("\\.")[0];
            sourceCode.append("import org.junit.runner.RunWith;\r\n");
            sourceCode.append("import cucumber.api.CucumberOptions;\r\n");
            sourceCode.append("import cucumber.api.junit.Cucumber;\r\n");
            sourceCode.append("@CucumberOptions(\r\n");
            sourceCode.append("strict = true, features = { \"" + featureFilePath + "\" }, glue =  \"sqs.cucumber\","+ getTagsForRunner(tag) + "monochrome = false, plugin = { \"pretty\",\r\n");
            sourceCode.append("\"html:" + reportFolderPath + "\",\r\n");
            sourceCode.append("\"junit:" + reportFolderPath + "/cucumber.xml\",\r\n");
            sourceCode.append("\"usage:" + reportFolderPath + "/cucumber-usage.json\",\r\n");
            sourceCode.append("\"json:" + reportFolderPath + "/cucumber.json\" })\r\n");
            sourceCode.append("@RunWith(Cucumber.class)\r\n");
            sourceCode.append(" public class " + classname + "{}\r\n");
            System.out.println(sourceCode.toString());
            System.out.println("Source file path");
           // System.exit(0);
            System.out.println(sourceFile.getAbsolutePath());
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(sourceCode.toString());
            } catch (Exception exception) {
                logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            }
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                logger.info("Kindly check rt.jar is available in jre lib folder,/r/n check tools.jar is available in jdk lib folder and/r/n check jdk path is configured in system environment 'path' variable.");
            } else {
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
                File parentDirectory = sourceFile.getParentFile();
                fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
                Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
                compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
                fileManager.close();
                try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{parentDirectory.toURI().toURL()})) {
                    System.out.println("Class Name "+ classLoader);
                    Class<?> helloClass = classLoader.loadClass(classname);
                    System.out.println("Hello Class ");

                    logger.debug("Runner Class Started...");
                    JUnitCore.runClasses(helloClass);
                }catch (Exception exception){
                    System.out.println("Error on load runner file");
                    exception.printStackTrace();
                    errorOccured=true;

                }
            }
        } catch (Exception exception) {
            logger.error(EXCEPTION_ON + Utilities.getCallerMethodName() + " :" ,exception);
            errorOccured=true;

        } finally {
            FrameworkBase.afterTestSuite();
        }
        return errorOccured;
    }

    private static String getProjectSpecificStepDefinition() {
        String projectSpecificStepDefinition = "";
        String projectSpecificClass = "sqs.cucumber.stepdefinitions." ;//+ FrameworkData.projectName.toLowerCase();

        //if (Utilities.isClassExists(projectSpecificClass)) {
        projectSpecificStepDefinition = ",\"sqs.cucumber.stepdefinitions.\"";
        // } else {
        //logger.warn("Application specific Stepdefinition not found : '" + projectSpecificClass + "'. If it required Please add the stepdefinition.");
        // }

        return projectSpecificStepDefinition;
    }

    private static String getTagsForRunner(String tags) {
        StringBuilder tagsForRunnerClass = new StringBuilder();
        if (tags == null || tags.isEmpty() || tags.equalsIgnoreCase("all")) {
            tagsForRunnerClass.append("tags ={\"not @Ignore\"}, ");
        } else {
            String[] tagsAsArray = tags.split(",");
            for (String currentTag : tagsAsArray) {
                if (!currentTag.startsWith("@")) {
                    currentTag = "@" + currentTag;
                }
                if (tagsForRunnerClass.length() == 0) {
                    tagsForRunnerClass.append("tags = { \"" + currentTag);
                } else {
                    if (tagsAsArray.length == 1) {
                        tagsForRunnerClass.append(currentTag);
                    } else
                        tagsForRunnerClass.append("," + currentTag);
                }
            }
            tagsForRunnerClass.append("\",\" not @Ignore\"}, ");
        }
        return String.valueOf(tagsForRunnerClass);
    }
}
