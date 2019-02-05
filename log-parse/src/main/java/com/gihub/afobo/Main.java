package com.gihub.afobo;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
//        String logDir = "C:\\work-p\\udacity\\mlnd-capstone\\log-parse\\src\\main\\resources\\";
        String logDir = "C:\\work-p\\udacity\\mlnd-capstone\\tower-log";

        Collection<Attempt> attempts = parseLogDir(logDir);

        ///
        System.out.println("========= Merged: " + attempts.size());
        List<Attempt> failed = attempts.stream()
                .filter(Attempt::isFailed)
                .sorted(Comparator.comparing(Attempt::getStartDate))
                .collect(Collectors.toList());
        System.out.println("========= Failed: " + failed.size());
        failed.forEach(System.out::println);

        Set<String> optionNames = attempts.stream().flatMap(a -> a.getOptions().keySet().stream()).collect(Collectors.toCollection(TreeSet::new));

        System.out.println("========= Option names: ");
        optionNames.forEach(System.out::println);

    }

    private static Collection<Attempt> parseLogDir(String logDir) throws IOException {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(logDir), "*-deploy-auto.log.*");

        Collection<Attempt> attempts = StreamSupport.stream(directoryStream.spliterator(), false)
                .map(path -> parseSingleLog(path.toFile()))
                .collect(Collectors.toList());

        return attempts.stream()
                .collect(Collectors.toMap(
                        Attempt::getClusterName,
                        a -> a,
                        (a1, a2) -> {
                            // take latest attempt
                            Attempt a = a1.getAttemptNumber() > a2.getAttemptNumber() ? a1 : a2;
                            a.setStartDate(min(a1.getStartDate(), a2.getStartDate()));
                            a.setEndDate(max(a1.getEndDate(), a2.getEndDate()));
                            return a;
                        }
                )).values();
    }

    private static Date min(Date d1, Date d2) {
        return d1.before(d2) ? d1 : d2;
    }

    private static Date max(Date d1, Date d2) {
        return d1.after(d2) ? d1 : d2;
    }

    private static Attempt parseSingleLog(File file) {
        Attempt attempt = new Attempt();

        int attemptNumber = Integer.parseInt(file.getName().substring(file.getName().lastIndexOf('.') + 1));
        attempt.setAttemptNumber(attemptNumber);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Date lastParsedDate = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"with")) {
                    KeyValue kv = parseKeyValueEquals(line);
                    attempt.getOptions().put(kv.key, kv.value);
                }
                if (line.startsWith("\"cluster_name")) {
                    KeyValue kv = parseKeyValueColon(line);
                    attempt.setClusterName(kv.value);
                }
                if (line.startsWith("\"vm_count")) {
                    KeyValue kv = parseKeyValueColon(line);
                    attempt.setVmCount(kv.value);
                }
                if (line.startsWith("\"build_name")) {
                    KeyValue kv = parseKeyValueColon(line);
                    attempt.setBuildName(kv.value);
                }
                if (line.contains("NO MORE HOSTS LEFT") || line.contains("[ERROR]")) {
                    attempt.setFailed(true);
                }
                if (line.contains("PLAY RECAP")) {
                    attempt.setCompleted(true);
                }
                Date lineDate = parseDate(line);
                if (lineDate != null) {
                    lastParsedDate = lineDate;
                }
                if (lineDate != null && attempt.getStartDate() == null) {
                    attempt.setStartDate(lineDate);
                }
            }

            attempt.setEndDate(lastParsedDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse " + file, e);
        }
        return attempt;
    }

    private static Date parseDate(String line) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        try {
            return format.parse(line);
        } catch (ParseException e) {
            return null;
        }
    }

    private static KeyValue parseKeyValueEquals(String line) {
        // "with_docker_version = 18.03.1.ce",
        Matcher matcher = Pattern.compile("\"(.*) = (.*)\",").matcher(line);
        if (matcher.find()) {
            return new KeyValue(matcher.group(1), matcher.group(2));
        }
        throw new IllegalStateException("Can't parse line:" + line);
    }

    private static KeyValue parseKeyValueColon(String line) {
        // "vm_count": "20"
        Matcher matcher = Pattern.compile("\"(.*)\": \"(.*)\"").matcher(line);
        if (matcher.find()) {
            return new KeyValue(matcher.group(1), matcher.group(2));
        }
        throw new IllegalStateException("Can't parse line:" + line);
    }

    @AllArgsConstructor
    private static class KeyValue {
        String key;
        String value;
    }
}
