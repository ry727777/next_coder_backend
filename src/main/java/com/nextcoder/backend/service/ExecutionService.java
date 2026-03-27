package com.nextcoder.backend.service;

import com.nextcoder.backend.dto.*;
import com.nextcoder.backend.entity.*;
import com.nextcoder.backend.repository.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutionService {

    private final TestCaseRepository testCaseRepository;
    private final SubmissionRepository submissionRepository;

    public ExecutionService(TestCaseRepository testCaseRepository, SubmissionRepository submissionRepository) {
        this.testCaseRepository = testCaseRepository;
        this.submissionRepository = submissionRepository;
    }

    // 🔥 MAIN METHOD (Run / Submit)
    public CodeExecutionResponse execute(CodeExecutionRequest request, boolean isRun) {

        List<TestCase> testCases = isRun
                ? testCaseRepository.findByProblemIdAndIsSample(request.getProblemId(), true)
                : testCaseRepository.findAllByProblemId(request.getProblemId());

        int passed = 0;
        List<TestCaseResult> results = new ArrayList<>();

        for (TestCase tc : testCases) {

            ExecutionResult result = runSingleTestCase(
                    request.getCode(),
                    tc.getInputData());

            // 🔥 HANDLE COMPILATION / RUNTIME ERROR
            if (result.getError() != null) {

                String errorMsg = result.getError();

                String status;

                if (errorMsg.contains("error:")) {
                    status = "COMPILATION_ERROR";
                } else {
                    status = "RUNTIME_ERROR";
                }

                results.add(new TestCaseResult(
                        tc.getInputData(),
                        tc.getExpectedOutput(),
                        errorMsg,
                        status));

                return new CodeExecutionResponse(
                        testCases.size(),
                        0,
                        status,
                        results);
            }

            // ✅ NORMAL FLOW
            boolean isPassed = result.getOutput().trim()
                    .equals(tc.getExpectedOutput().trim());

            if (isPassed)
                passed++;

            results.add(new TestCaseResult(
                    tc.getInputData(),
                    tc.getExpectedOutput(),
                    result.getOutput(),
                    isPassed ? "PASSED" : "FAILED"));
        }

        String verdict = (passed == testCases.size()) ? "AC" : "WA";

        if (!isRun) {
            Submission submission = new Submission();

            submission.setCode(request.getCode());
            submission.setLanguage(request.getLanguage());
            submission.setVerdict(verdict);
            submission.setTotalTestCases(testCases.size());
            submission.setPassedTestCases(passed);
            submission.setExecutionTime(0);

            // set problem
            Question q = new Question();
            q.setId(request.getProblemId());
            submission.setProblem(q);

            submissionRepository.save(submission);
        }

        return new CodeExecutionResponse(
                testCases.size(),
                passed,
                verdict,
                results);
    }

    // 🔥 CORE EXECUTION ENGINE (1 test case)
    private ExecutionResult runSingleTestCase(String code, String input) {

        Path sandboxPath = null;

        try {
            // 🔹 Create sandbox
            String folderName = "sandbox_" + UUID.randomUUID();
            sandboxPath = Files.createDirectory(Paths.get(folderName));

            // 🔹 Write code
            Path javaFile = sandboxPath.resolve("Main.java");
            Files.writeString(javaFile, code);

            // 🔹 Docker command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-i",
                    "--memory=256m",
                    "--cpus=1",
                    "-v", sandboxPath.toAbsolutePath() + ":/app",
                    "nextcoder-java-sandbox",
                    "sh", "-c",
                    "javac /app/Main.java && java -cp /app Main");

            processBuilder.redirectErrorStream(false);
            Process process = processBuilder.start();

            // 🔥 PASS INPUT
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(input);
                writer.flush();
            }

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            // 🔥 STDOUT
            Thread outThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (Exception ignored) {
                }
            });

            // 🔥 STDERR
            Thread errThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorOutput.append(line).append("\n");
                    }
                } catch (Exception ignored) {
                }
            });

            outThread.start();
            errThread.start();

            // 🔥 Timeout
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                return new ExecutionResult(null, "TLE");
            }

            outThread.join();
            errThread.join();

            // 🔥 Error handling
            if (errorOutput.length() > 0) {
                return new ExecutionResult(null, errorOutput.toString());
            }

            return new ExecutionResult(output.toString(), null);

        } catch (Exception e) {
            return new ExecutionResult(null, "Runtime Error");
        } finally {
            // 🔥 CLEANUP
            if (sandboxPath != null) {
                try {
                    Files.walk(sandboxPath)
                            .sorted((a, b) -> b.compareTo(a))
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (Exception ignored) {
                                }
                            });
                } catch (Exception ignored) {
                }
            }
        }
    }
}