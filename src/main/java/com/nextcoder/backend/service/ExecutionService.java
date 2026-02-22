package com.nextcoder.backend.service;

import com.nextcoder.backend.dto.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutionService {
    public CodeExecutionResponse execute(CodeExecutionRequest request) {

    long startTime = System.currentTimeMillis();
    Path sandboxPath = null;

    try {
        // 🔹 Create unique sandbox folder
        String folderName = "sandbox_" + UUID.randomUUID();
        sandboxPath = Files.createDirectory(Paths.get(folderName));

        // 🔹 Write user code to Main.java
        Path javaFile = sandboxPath.resolve("Main.java");
        Files.writeString(javaFile, request.getCode());

        // 🔹 Docker execution command
        ProcessBuilder processBuilder = new ProcessBuilder(
                "docker", "run", "--rm",
                "-i",   // 🔥 VERY IMPORTANT
                "--memory=256m",
                "--cpus=0.5",
                "-v", sandboxPath.toAbsolutePath() + ":/app",
                "nextcoder-java-sandbox",
                "sh", "-c",
                "javac /app/Main.java && java -cp /app Main"
        );

        processBuilder.redirectErrorStream(false); // keep error separate
        Process process = processBuilder.start();

        // 🔥 SEND INPUT
        if (request.getInput() != null && !request.getInput().isEmpty()) {
            try (BufferedWriter writer =
                         new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(request.getInput());
                writer.flush();
            }
        }

        // 🔥 READ STDOUT IN PARALLEL
        StringBuilder output = new StringBuilder();
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (Exception ignored) {}
        });

        // 🔥 READ STDERR IN PARALLEL
        StringBuilder errorOutput = new StringBuilder();
        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            } catch (Exception ignored) {}
        });

        outputThread.start();
        errorThread.start();

        // 🔥 WAIT WITH TIME LIMIT
        boolean finished = process.waitFor(5, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            return new CodeExecutionResponse(null, "Time Limit Exceeded", 0);
        }

        // Wait for stream threads to finish
        outputThread.join();
        errorThread.join();

        long endTime = System.currentTimeMillis();

        // 🔥 If compilation/runtime error occurred
        if (errorOutput.length() > 0) {
            return new CodeExecutionResponse(
                    null,
                    errorOutput.toString(),
                    endTime - startTime
            );
        }

        return new CodeExecutionResponse(
                output.toString(),
                null,
                endTime - startTime
        );

    } catch (Exception e) {
        return new CodeExecutionResponse(null, e.getMessage(), 0);
    } finally {
        // 🔥 CLEAN SANDBOX
        if (sandboxPath != null) {
            try {
                Files.walk(sandboxPath)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try { Files.delete(path); } catch (Exception ignored) {}
                        });
            } catch (Exception ignored) {}
        }
    }
}
}