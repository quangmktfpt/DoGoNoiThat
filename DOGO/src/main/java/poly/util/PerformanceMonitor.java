package poly.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class để monitor performance của ứng dụng
 */
public class PerformanceMonitor {
    
    private static PerformanceMonitor instance;
    private Map<String, Long> startTimes;
    private Map<String, Long> executionTimes;
    private ScheduledExecutorService scheduler;
    private long memoryUsage;
    private long startTime;
    
    private PerformanceMonitor() {
        startTimes = new HashMap<>();
        executionTimes = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
        startTime = System.currentTimeMillis();
        
        // Monitor memory usage every 30 seconds
        scheduler.scheduleAtFixedRate(this::updateMemoryUsage, 0, 30, TimeUnit.SECONDS);
    }
    
    public static PerformanceMonitor getInstance() {
        if (instance == null) {
            instance = new PerformanceMonitor();
        }
        return instance;
    }
    
    /**
     * Bắt đầu đo thời gian thực thi
     */
    public void startTimer(String operation) {
        startTimes.put(operation, System.currentTimeMillis());
    }
    
    /**
     * Kết thúc đo thời gian thực thi
     */
    public void endTimer(String operation) {
        Long startTime = startTimes.get(operation);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            executionTimes.put(operation, executionTime);
            startTimes.remove(operation);
        }
    }
    
    /**
     * Lấy thời gian thực thi của một operation
     */
    public long getExecutionTime(String operation) {
        return executionTimes.getOrDefault(operation, 0L);
    }
    
    /**
     * Cập nhật thông tin memory usage
     */
    private void updateMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        memoryUsage = runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Lấy thông tin memory usage hiện tại
     */
    public long getMemoryUsage() {
        return memoryUsage;
    }
    
    /**
     * Lấy thông tin memory usage dạng string
     */
    public String getMemoryUsageString() {
        long mb = memoryUsage / (1024 * 1024);
        return mb + " MB";
    }
    
    /**
     * Lấy thời gian chạy của ứng dụng
     */
    public long getUptime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Lấy thời gian chạy dạng string
     */
    public String getUptimeString() {
        long uptime = getUptime();
        long hours = uptime / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (uptime % (1000 * 60)) / 1000;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    /**
     * Lấy performance report
     */
    public String getPerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== PERFORMANCE REPORT ===\n");
        report.append("Uptime: ").append(getUptimeString()).append("\n");
        report.append("Memory Usage: ").append(getMemoryUsageString()).append("\n");
        report.append("Execution Times:\n");
        
        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
            report.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("ms\n");
        }
        
        return report.toString();
    }
    
    /**
     * Clear execution times
     */
    public void clearExecutionTimes() {
        executionTimes.clear();
    }
    
    /**
     * Shutdown monitor
     */
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
} 