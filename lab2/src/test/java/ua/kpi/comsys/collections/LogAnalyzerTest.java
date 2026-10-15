package ua.kpi.comsys.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogAnalyzerTest {
    private final LogAnalyzer analyzer = new LogAnalyzer();

    private static String log(String ip) {
        return ip + " ident alice [01/May/2025:07:20:10 +0000] \"GET /index.html HTTP/1.1\" 200 9481";
    }

    @Test
    void getUniqueIpsShouldThrowIfLinesNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.getUniqueIps(null));
        assertEquals("logLines cannot be null", exception.getMessage());
    }

    @Test
    void getUniqueIpsShouldReturnEmptySetForEmptyList() {
        assertTrue(analyzer.getUniqueIps(List.of()).isEmpty());
    }

    @Test
    void getUniqueIpsShouldReturnDistinctIps() {
        List<String> lines = List.of(log("127.0.0.1"), log("10.0.0.2"), log("127.0.0.1"));
        assertEquals(Set.of("127.0.0.1", "10.0.0.2"), analyzer.getUniqueIps(lines));
    }

    @Test
    void getUniqueIpsShouldIgnoreMalformedLines() {
        List<String> lines = Arrays.asList(
                log("127.0.0.1"),
                "garbage line",
                "",
                null,
                "10.0.0.3 - - [01/May/2025:07:20:10 +0000] GET /index.html 200 9481",
                "10.0.0.4 - - [01/May/2025:07:20:10 +0000] \"GET / HTTP/1.1\" 404 -"
        );
        assertEquals(Set.of("127.0.0.1", "10.0.0.4"), analyzer.getUniqueIps(lines));
    }

    @Test
    void getTopIpsShouldThrowIfLinesNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.getTopIps(null, 1));
        assertEquals("logLines cannot be null", exception.getMessage());
    }

    @Test
    void getTopIpsShouldThrowIfTopNZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyzer.getTopIps(List.of(log("127.0.0.1")), 0));
        assertEquals("topN must be greater than 0", exception.getMessage());
    }

    @Test
    void getTopIpsShouldThrowIfTopNNegative() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getTopIps(List.of(), -5));
    }

    @Test
    void getTopIpsShouldSortByFrequencyDescending() {
        List<String> lines = List.of(
                log("1.1.1.1"),
                log("2.2.2.2"), log("2.2.2.2"), log("2.2.2.2"),
                log("3.3.3.3"), log("3.3.3.3")
        );
        assertEquals(List.of("2.2.2.2", "3.3.3.3"), analyzer.getTopIps(lines, 2));
    }

    @Test
    void getTopIpsShouldBreakTiesAlphabetically() {
        List<String> lines = List.of(
                log("192.168.0.1"), log("10.0.0.1"), log("172.16.0.1"),
                log("192.168.0.1"), log("10.0.0.1"), log("172.16.0.1"),
                log("8.8.8.8")
        );
        assertEquals(List.of("10.0.0.1", "172.16.0.1", "192.168.0.1", "8.8.8.8"),
                analyzer.getTopIps(lines, 4));
    }

    @Test
    void getTopIpsShouldReturnAllIfFewerThanTopN() {
        List<String> lines = List.of(log("1.1.1.1"), log("2.2.2.2"), log("2.2.2.2"));
        assertEquals(List.of("2.2.2.2", "1.1.1.1"), analyzer.getTopIps(lines, 10));
    }

    @Test
    void getTopIpsShouldNotContainDuplicates() {
        List<String> lines = List.of(log("1.1.1.1"), log("1.1.1.1"), log("1.1.1.1"), log("2.2.2.2"));
        assertEquals(List.of("1.1.1.1", "2.2.2.2"), analyzer.getTopIps(lines, 2));
    }

    @Test
    void getTopIpsShouldIgnoreMalformedLines() {
        List<String> lines = List.of("bad", "bad", "bad", log("1.1.1.1"));
        assertEquals(List.of("1.1.1.1"), analyzer.getTopIps(lines, 3));
    }

    @Test
    void getTopIpsShouldReturnEmptyListForEmptyInput() {
        assertTrue(analyzer.getTopIps(List.of(), 3).isEmpty());
    }
}
