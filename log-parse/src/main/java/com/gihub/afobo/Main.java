package com.gihub.afobo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String fileName = "C:\\work-p\\udacity\\mlnd-capstone\\log-parse\\src\\main\\resources\\abondar.20181217125633-deploy-auto.log.1";

        Date startDate = null;
        Date endDate = null;
        boolean failed = false;
        boolean completed = false;
        String clusterName;
        String vmCount;
        String buildName;

        String attempt = fileName.substring(fileName.lastIndexOf('.') + 1);
        System.out.println("attempt = " + attempt);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String prevLine = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"with")) {
                    System.out.println(line);
                    KeyValue kv = parseKeyValueEquals(line);
                    System.out.println("kv = " + kv);
                }
                if (line.startsWith("\"cluster_name")) {
                    System.out.println(line);
                    KeyValue kv = parseKeyValueColon(line);
                    clusterName = kv.value;
                }
                if (line.startsWith("\"vm_count")) {
                    System.out.println(line);
                    KeyValue kv = parseKeyValueColon(line);
                    vmCount = kv.value;
                }
                if (line.startsWith("\"build_name")) {
                    System.out.println(line);
                    KeyValue kv = parseKeyValueColon(line);
                    buildName = kv.value;
                }
                if (startDate == null) {
                    startDate = parseDate(line);
                }

                if (line.contains("NO MORE HOSTS LEFT")) {
                    failed = true;
                }

                if (line.contains("PLAY RECAP")) {
                    completed = true;
                }

                prevLine = line;
            }

            if (prevLine != null) {
                endDate = parseDate(prevLine);
            }

            System.out.println("startDate = " + startDate);
            System.out.println("endDate = " + endDate);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static Date parseDate(String line) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        return format.parse(line);
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

    private static class KeyValue {
        String key;
        String value;

        KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "KeyValue{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
