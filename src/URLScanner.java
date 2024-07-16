import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class URLScanner extends JFrame {
    private JTextField urlField;
    private JTextArea resultArea;
    private JButton scanButton;
    private JButton batchScanButton;
    private JTextArea batchUrlArea;
    private ExecutorService executorService;

    private static final List<String> PATHS = Arrays.asList(
            "/api-docs",
            "/actuator",
            "/actuator/./env",
            "/actuator/auditLog",
            "/actuator/auditevents",
            "/actuator/autoconfig",
            "/actuator/beans",
            "/actuator/caches",
            "/actuator/conditions",
            "/actuator/configurationMetadata",
            "/actuator/configprops",
            "/actuator/dump",
            "/actuator/env",
            "/actuator/events",
            "/actuator/exportRegisteredServices",
            "/actuator/features",
            "/actuator/flyway",
            "/actuator/health",
            "/actuator/healthcheck",
            "/actuator/httptrace",
            "/actuator/hystrix.stream",
            "/actuator/info",
            "/actuator/integrationgraph",
            "/actuator/jolokia",
            "/actuator/logfile",
            "/actuator/loggers",
            "/actuator/loggingConfig",
            "/actuator/liquibase",
            "/actuator/metrics",
            "/actuator/mappings",
            "/actuator/scheduledtasks",
            "/actuator/swagger-ui.html",
            "/actuator/prometheus",
            "/actuator/refresh",
            "/actuator/registeredServices",
            "/actuator/releaseAttributes",
            "/actuator/resolveAttributes",
            "/actuator/sessions",
            "/actuator/springWebflow",
            "/actuator/sso",
            "/actuator/ssoSessions",
            "/actuator/statistics",
            "/actuator/status",
            "/actuator/threaddump",
            "/actuator/trace",
            "/actuator/env.css",
            "/artemis-portal/artemis/env",
            "/artemis/api",
            "/artemis/api/env",
            "/auditevents",
            "/autoconfig",
            "/api",
            "/api.html",
            "/api/actuator",
            "/api/doc",
            "/api/index.html",
            "/api/swaggerui",
            "/api/swagger-ui.html",
            "/api/swagger",
            "/api/swagger/ui",
            "/api/v2/api-docs",
            "/api/v2;%0A/api-docs",
            "/api/v2;%252Ftest/api-docs",
            "/beans",
            "/caches",
            "/cloudfoundryapplication",
            "/conditions",
            "/configprops",
            "/distv2/index.html",
            "/docs",
            "/doc.html",
            "/druid",
            "/druid/index.html",
            "/druid/login.html",
            "/druid/websession.html",
            "/dubbo-provider/distv2/index.html",
            "/dump",
            "/decision/login",
            "/entity/all",
            "/env",
            "/env.css",
            "/env/(name)",
            "/eureka",
            "/flyway",
            "/gateway/actuator",
            "/gateway/actuator/auditevents",
            "/gateway/actuator/beans",
            "/gateway/actuator/conditions",
            "/gateway/actuator/configprops",
            "/gateway/actuator/env",
            "/gateway/actuator/health",
            "/gateway/actuator/httptrace",
            "/gateway/actuator/hystrix.stream",
            "/gateway/actuator/info",
            "/gateway/actuator/jolokia",
            "/gateway/actuator/logfile",
            "/gateway/actuator/loggers",
            "/gateway/actuator/mappings",
            "/gateway/actuator/metrics",
            "/gateway/actuator/scheduledtasks",
            "/gateway/actuator/swagger-ui.html",
            "/gateway/actuator/threaddump",
            "/gateway/actuator/trace",
            "/gateway/routes",
            "/health",
            "/httptrace",
            "/hystrix",
            "/info",
            "/integrationgraph",
            "/jolokia",
            "/jolokia/list",
            "/jeecg/swagger-ui",
            "/jeecg/swagger/",
            "/libs/swaggerui",
            "/liquibase",
            "/list",
            "/logfile",
            "/loggers",
            "/metrics",
            "/mappings",
            "/monitor",
            "/nacos",
            "/prod-api/actuator",
            "/prometheus",
            "/portal/conf/config.properties",
            "/portal/env/",
            "/refresh",
            "/scheduledtasks",
            "/sessions",
            "/spring-security-oauth-resource/swagger-ui.html",
            "/spring-security-rest/api/swagger-ui.html",
            "/static/swagger.json",
            "/sw/swagger-ui.html",
            "/swagger",
            "/swagger/codes",
            "/swagger/doc.json",
            "/swagger/index.html",
            "/swagger/static/index.html",
            "/swagger/swagger-ui.html",
            "/Swagger/ui/index",
            "/swagger/ui",
            "/swagger/v1/swagger.json",
            "/swagger/v2/swagger.json",
            "/swagger-dubbo/api-docs",
            "/swagger-resources",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui.html;",
            "/swagger-ui/html",
            "/swagger-ui/index.html",
            "/system/druid/index.html",
            "/system/druid/webseesion.html",
            "/threaddump",
            "/template/swagger-ui.html",
            "/trace",
            "/users",
            "/user/swagger-ui.html",
            "/version",
            "/v1/api-docs/",
            "/v2/api-docs/",
            "/v3/api-docs/",
            "/v1/swagger-resources",
            "/v2/swagger-resources",
            "/v3/swagger-resources",
            "/v1.1/swagger-ui.html",
            "/v1.1;%0A/api-docs",
            "/v1.2/swagger-ui.html",
            "/v1.2;%0A/api-docs",
            "/v1.3/swagger-ui.html",
            "/v1.3;%0A/api-docs",
            "/v1.4/swagger-ui.html",
            "/v1.4;%0A/api-docs",
            "/v1.5/swagger-ui.html",
            "/v1.5;%0A/api-docs",
            "/v1.6/swagger-ui.html",
            "/v1.6;%0A/api-docs",
            "/v1.7/swagger-ui.html",
            "/v1.7;%0A/api-docs",
            "/v1.8/swagger-ui.html",
            "/v1.8;%0A/api-docs",
            "/v1.9/swagger-ui.html",
            "/v1.9;%0A/api-docs",
            "/v2.0/swagger-ui.html",
            "/v2.0;%0A/api-docs",
            "/v2.1/swagger-ui.html",
            "/v2.1;%0A/api-docs",
            "/v2.2/swagger-ui.html",
            "/v2.2;%0A/api-docs",
            "/v2.3/swagger-ui.html",
            "/v2.3;%0A/api-docs",
            "/v1/swagger.json",
            "/v2/swagger.json",
            "/v3/swagger.json",
            "/v2;%0A/api-docs",
            "/v3;%0A/api-docs",
            "/v2;%252Ftest/api-docs",
            "/v3;%252Ftest/api-docs",
            "/webpage/system/druid/websession.html",
            "/webpage/system/druid/index.html",
            "/webroot/decision/login",
            "/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js",
            "/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js?v=2.9.2",
            "/webjars/springfox-swagger-ui/springfox.js",
            "/webjars/springfox-swagger-ui/springfox.js?v=2.9.2",
            "/webjars/springfox-swagger-ui/swagger-ui-bundle.js",
            "/webjars/springfox-swagger-ui/swagger-ui-bundle.js?v=2.9.2",
            "/%20/swagger-ui.html",
            "/v2/api-docs",
            "/heapdump",
            "/intergrationgraph",
            "/shutdown",
            "/actuator/heapdump",
            "/actuator/shutdown",
            "/gateway/actuator/heapdump",
            "/heapdump.json",
            "/hystrix.stream",
            "/v1/console/server/state?accessToken=&username=",
            "/nacos/v1/console/server/state?accessToken=&username="
    );

    static {
        // 在静态初始化块中设置信任所有证书
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 创建所有主机名验证器
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public URLScanner() {
        super("SpringBoot_Scanner v0.1  by CatalyzeSec");
        initComponents();
        initExecutors();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Single URL scan panel
        JPanel singleScanPanel = new JPanel(new BorderLayout());
        urlField = new JTextField(50);
        scanButton = new JButton("Start Scan");
        scanButton.addActionListener(e -> startScan());
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel(" Enter URL:"), BorderLayout.WEST);
        inputPanel.add(urlField, BorderLayout.CENTER);
        inputPanel.add(scanButton, BorderLayout.EAST);
        singleScanPanel.add(inputPanel, BorderLayout.NORTH);

        // Batch URL scan panel
        JPanel batchScanPanel = new JPanel(new BorderLayout());
        batchUrlArea = new JTextArea(10, 50);
        batchUrlArea.setLineWrap(true);
        JScrollPane batchUrlScrollPane = new JScrollPane(batchUrlArea);
        batchScanButton = new JButton("Start Batch Scan");
        batchScanButton.addActionListener(e -> startBatchScan());
        batchScanPanel.add(new JLabel("Enter URLs (one per line):"), BorderLayout.NORTH);
        batchScanPanel.add(batchUrlScrollPane, BorderLayout.CENTER);
        batchScanPanel.add(batchScanButton, BorderLayout.SOUTH);

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        resultArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openURL(e.getPoint());
                }
            }
        });

        // Add tabs
        tabbedPane.addTab("Single URL Scan", singleScanPanel);
        tabbedPane.addTab("Batch URL Scan", batchScanPanel);

        add(tabbedPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initExecutors() {
        executorService = Executors.newFixedThreadPool(20);
    }

    private void startScan() {
        String url = urlField.getText().trim();
        if (url.isEmpty()) {
            resultArea.setText("Error: Please enter a URL");
            return;
        }

        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

        resultArea.setText("Scanning URL: " + url + "/\n\n");
        String finalUrl = url;
        CompletableFuture.runAsync(() -> scan(finalUrl), executorService);
    }

    private void startBatchScan() {
        String[] urls = batchUrlArea.getText().split("\n");
        if (urls.length == 0) {
            resultArea.setText("Error: Please enter at least one URL");
            return;
        }

        resultArea.setText("Starting batch scan of " + urls.length + " URLs\n\n");
        for (String url : urls) {
            url = url.trim();
            if (!url.isEmpty()) {
                url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
                String finalUrl = url;
                CompletableFuture.runAsync(() -> scan(finalUrl), executorService);
            }
        }
    }

    private void scan(String baseUrl) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (String path : PATHS) {
            String fullUrl = baseUrl + path;
            futures.add(CompletableFuture.supplyAsync(() -> scanUrl(fullUrl), executorService));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> SwingUtilities.invokeLater(() -> {
                    resultArea.append("\nDone scanning " + baseUrl + "\n");
                    resultArea.setCaretPosition(resultArea.getDocument().getLength());
                }));

        for (CompletableFuture<String> future : futures) {
            future.thenAccept(this::appendResult);
        }
    }

    private String scanUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection;
            if (url.getProtocol().toLowerCase().equals("https")) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java URL Scanner");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return String.format("%s\t%d\t%d\n", urlString, responseCode, response.length());
            } else {
                return null;
            }
        } catch (Exception e) {
            return String.format("%s Error: %s\n", urlString, e.getMessage());
        }
    }

    private void appendResult(String result) {
        if (result != null) {
            SwingUtilities.invokeLater(() -> {
                resultArea.append(result);
                resultArea.setCaretPosition(resultArea.getDocument().getLength());
            });
        }
    }

    private void openURL(Point point) {
        try {
            int pos = resultArea.viewToModel(point);
            int lineStart = resultArea.getLineStartOffset(resultArea.getLineOfOffset(pos));
            int lineEnd = resultArea.getLineEndOffset(resultArea.getLineOfOffset(pos));
            String line = resultArea.getText(lineStart, lineEnd - lineStart);

            Pattern pattern = Pattern.compile("(https?://\\S+)\\s");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Desktop.getDesktop().browse(new URI(matcher.group(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new URLScanner().setVisible(true));
    }
}