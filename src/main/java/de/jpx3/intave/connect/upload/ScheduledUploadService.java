package de.jpx3.intave.connect.upload;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.cleanup.ShutdownTasks;
import de.jpx3.intave.executor.BackgroundExecutor;
import de.jpx3.intave.security.ContextSecrets;
import de.jpx3.intave.security.HWIDVerification;
import de.jpx3.intave.security.LicenseAccess;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static de.jpx3.intave.IntaveControl.GOMME_MODE;

public final class ScheduledUploadService {
  private static UUID temporaryId = null;

  private final static long STORAGE_BYTE_LIMIT = 1024 * 1024 * 64; // 64 MB
  private final Map<String, byte[]> storage = new HashMap<>();
  private long storageSize = 0;

  public void enable() {
    mergeFiles();
    ShutdownTasks.addBeforeAll(this::disable);
  }

  private void mergeFiles() {
    BackgroundExecutor.execute(() -> {
      mergeSessionFilesToDayFile();
      uploadDayFilesWeekly();
    });
    // start timer for next day
    Bukkit.getScheduler().scheduleSyncDelayedTask(IntavePlugin.singletonInstance(), this::mergeFiles, millisUntilNextDay() / 50);
  }

  public void scheduledUpload(String name, String data) throws IOException {
    scheduledUpload(name, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
  }

  public void scheduledUpload(String name, byte[] data) throws IOException {
    scheduledUpload(name, new ByteArrayInputStream(data));
  }

  public void scheduledUpload(String name, InputStream data) throws IOException {
    System.out.println("Scheduled upload: " + name + " (" + data.available() + " bytes)");
    if (storageSize + data.available() > STORAGE_BYTE_LIMIT) {
      if (!temporaryFolderPresent()) {
        newTemporaryFolder();
      }
      storage.forEach((k, v) -> {
        try {
          copyToSession(k, v);
        } catch (IOException exception) {
          System.out.println("Failed to copy file to temp-directory: " + k);
          exception.printStackTrace();
        }
      });
      storage.clear();
      storageSize = 0;
      return;
    }
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      byte[] copy = new byte[2048];
      int read;
      while ((read = data.read(copy)) != -1) {
        buffer.write(copy, 0, read);
      }
      byte[] array = buffer.toByteArray();
      storage.put(name, array);
      storageSize += array.length;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public void disable() {
    mergeFilesToSessionFile();
    mergeSessionFilesToDayFile();
    uploadDayFilesWeekly();
  }

  private void mergeFilesToSessionFile() {
    File workingFolder = dataFolder();
    File sessionFile = new File(workingFolder, "X3-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + ".zip");
    sessionFile.getParentFile().mkdirs();
    boolean added = false;
    try {
      sessionFile.createNewFile();
    } catch (IOException exception) {
      exception.printStackTrace();
      return;
    }
    // create a zip file
    try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(sessionFile.toPath()))) {
      if (temporaryFolderPresent()) {
        File[] files = tempDirectory().listFiles();
        if (files != null) {
          for (File file : files) {
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            try (FileInputStream in = new FileInputStream(file)) {
              int len;
              byte[] buffer = new byte[2048];
              while ((len = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
              }
            }
            zipOut.closeEntry();
            file.delete();
            added = true;
          }
        }
        tempDirectory().delete();
        temporaryId = null;
      }
      for (Map.Entry<String, byte[]> entry : storage.entrySet()) {
        String k = entry.getKey();
        byte[] v = entry.getValue();
        zipOut.putNextEntry(new ZipEntry(k));
        zipOut.write(v);
        zipOut.closeEntry();
        added = true;
      }
      storage.clear();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (!added) {
      sessionFile.delete();
    }
  }

  private void mergeSessionFilesToDayFile() {
    File workingFolder = dataFolder();
    File dayFile = new File(workingFolder, "X4-" + (currentDay() - 1) + ".zip");
    if (dayFile.exists()) {
      return;
    }
    dayFile.getParentFile().mkdirs();
    try {
      dayFile.createNewFile();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    // create a zip file
    try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(dayFile.toPath()))) {
      File[] files = workingFolder.listFiles();
      if (files != null) {
        for (File file : files) {
          // copy files from session folder to zip
          if (file.getName().startsWith("X3-") && file.getName().endsWith(".zip")) {
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            try (FileInputStream in = new FileInputStream(file)) {
              int len;
              byte[] buffer = new byte[2048];
              while ((len = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
              }
            }
            zipOut.closeEntry();
            file.delete();
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void uploadDayFilesWeekly() {
    // check if week has passed
    if (currentDay() % 7 != 6) {
      return;
    }
    // open connection
    try {
      URL url = new URL("https://service.intave.de/analytics/upload");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/zip");
      connection.setRequestProperty("Identifier", LicenseAccess.rawLicense());
      connection.setRequestProperty("Machine", HWIDVerification.publicHardwareIdentifier());
      OutputStream outputStream = connection.getOutputStream();
      outputStream = new ZipOutputStream(outputStream);
      File workingFolder = dataFolder();
      File[] files = workingFolder.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.getName().startsWith("X4-") && file.getName().endsWith(".zip")) {
            try (FileInputStream in = new FileInputStream(file)) {
              int len;
              byte[] buffer = new byte[2048];
              while ((len = in.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
              }
            }
          }
          file.delete();
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean temporaryFolderPresent() {
    return temporaryId != null;
  }

  private void newTemporaryFolder() {
    File file;
    do {
      temporaryId = UUID.randomUUID();
      file = tempDirectory();
    } while (file.exists());
    file.mkdir();
  }

  private void copyToSession(String name, byte[] data) throws IOException {
    File file = new File(tempDirectory(), name);
    file.createNewFile();
    file.setWritable(true);
    file.setReadable(true);
    try (java.io.FileOutputStream out = new FileOutputStream(file)) {
      out.write(data);
    }
  }

  private File tempDirectory() {
    // get the temp directory
    return new File(System.getProperty("java.io.tmpdir"), "intave-"+ temporaryId);
  }

  private long currentDay() {
    return System.currentTimeMillis() / (24 * 60 * 60 * 1000);
  }

  private long millisUntilNextDay() {
    return (24 * 60 * 60 * 1000) - (System.currentTimeMillis() % (24 * 60 * 60 * 1000));
  }

  public File dataFolder() {
    String operatingSystem = System.getProperty("os.name").toLowerCase(Locale.ROOT);
    File workDirectory;
    String filePath;
    if (operatingSystem.contains("win")) {
      filePath = System.getenv("APPDATA") + "/Intave/Data/";
    } else {
      if (GOMME_MODE) {
        filePath = ContextSecrets.secret("cache-directory") + "data/";
      } else {
        filePath = System.getProperty("user.home") + "/.intave/data/";
      }
    }
    workDirectory = new File(filePath);
    if (!workDirectory.exists()) {
      workDirectory.mkdir();
    }
    return workDirectory;
  }
}
