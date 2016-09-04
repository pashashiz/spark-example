package com.ps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Utils {

    private Utils() {}

    public static void fakeHadoopIfAbsent() {
        // If Hadoop is absent on the system Spark for windows will fail
        if (System.getProperty("os.name").startsWith("Windows") && System.getenv("HADOOP_HOME") == null) {
            // This property should specify full path to the bin folder with winutils
            // To download winutils.exe go http://public-repo-1.hortonworks.com/hdp-win-alpha/winutils.exe.
            System.setProperty("hadoop.home.dir", "C:/hadoop");
        }
    }

    public static Path makeResourceAsFile(String resource) {
        try {
            String ext = getFileExtension(resource);
            Path file = Files.createTempFile("temp-", "-file" + ((ext != null) ? "." + ext : ""));
            Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource),
                    file, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String getFileExtension(String name) {
        String[] tokens = name.split("\\.");
        return tokens.length > 1 ? tokens[tokens.length - 1] : null;
    }

}
