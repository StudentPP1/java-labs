package ua.kpi.comsys.collections;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer {

    private static final Pattern COMMON_LOG_FORMAT =
            Pattern.compile("^(\\S+) \\S+ \\S+ \\[[^\\]]+] \"[^\"]*\" \\d{3} (\\d+|-)$");

    public Set<String> getUniqueIps(List<String> logLines) {
        if (logLines == null) {
            throw new IllegalArgumentException("logLines cannot be null");
        }
        Set<String> ipAddresses = new HashSet<>();
        for (String line : logLines) {
            extractIp(line).ifPresent(ipAddresses::add);
        }
        return ipAddresses;
    }

    public List<String> getTopIps(List<String> logLines, int topN) {
        if (logLines == null) {
            throw new IllegalArgumentException("logLines cannot be null");
        }
        if (topN <= 0) {
            throw new IllegalArgumentException("topN must be greater than 0");
        }
        Map<String, Long> frequencyMap = new HashMap<>();
        for (String line : logLines) {
            extractIp(line).ifPresent(ip -> frequencyMap.merge(ip, 1L, Long::sum));
        }
        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }

    private Optional<String> extractIp(String line) {
        if (line == null) {
            return Optional.empty();
        }
        Matcher matcher = COMMON_LOG_FORMAT.matcher(line);
        return matcher.matches() ? Optional.of(matcher.group(1)) : Optional.empty();
    }
}
