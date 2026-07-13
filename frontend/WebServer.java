import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.net.InetSocketAddress;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServer {
    public static void main(String[] args) throws Exception {
        int port = 3000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null);
        System.out.println("PLUS33 Web Server running on http://localhost:" + port);
        server.start();
    }

    static class FileHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();

            if (path.equals("/api/upload-avatar") && "POST".equalsIgnoreCase(t.getRequestMethod())) {
                try {
                    String username = t.getRequestHeaders().getFirst("X-Username");
                    String role = t.getRequestHeaders().getFirst("X-Role");
                    String contentType = t.getRequestHeaders().getFirst("Content-Type");
                    
                    if (username == null || role == null) {
                        username = "user";
                        role = "guest";
                    }
                    
                    File avatarDir = new File("frontend/imgs/avatars");
                    if (!avatarDir.exists()) {
                        avatarDir.mkdirs();
                    }
                    
                    String ext = "png";
                    if (contentType != null) {
                        if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                            ext = "jpg";
                        } else if (contentType.contains("gif")) {
                            ext = "gif";
                        } else if (contentType.contains("webp")) {
                            ext = "webp";
                        }
                    }
                    
                    int rand = (int)(Math.random() * 9000) + 1000;
                    String filename = String.format("%s(%s)_%d.%s", username, role, rand, ext);
                    File outFile = new File(avatarDir, filename);
                    
                    InputStream is = t.getRequestBody();
                    byte[] bytes = is.readAllBytes();
                    Files.write(outFile.toPath(), bytes);
                    
                    String relativeUrl = "imgs/avatars/" + filename;
                    String jsonResponse = "{\"success\":true,\"url\":\"" + relativeUrl + "\"}";
                    
                    t.getResponseHeaders().set("Content-Type", "application/json");
                    t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    t.sendResponseHeaders(200, jsonResponse.length());
                    OutputStream os = t.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    String errResponse = "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
                    t.getResponseHeaders().set("Content-Type", "application/json");
                    t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    t.sendResponseHeaders(500, errResponse.length());
                    OutputStream os = t.getResponseBody();
                    os.write(errResponse.getBytes());
                    os.close();
                    return;
                }
            }

            // Reverse Proxy for Spring Boot Backend API calls on port 8080
            if (path.startsWith("/api/v1/") || path.startsWith("/api/v2/")) {
                String query = t.getRequestURI().getRawQuery();
                String targetUrl = "http://127.0.0.1:8080" + path + (query != null ? "?" + query : "");
                try {
                    URL url = new URL(targetUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(t.getRequestMethod());
                    
                    // Copy Request Headers (ignoring Cookie to prevent too large request header error)
                    for (String headerKey : t.getRequestHeaders().keySet()) {
                        if (headerKey != null && headerKey.equalsIgnoreCase("Cookie")) {
                            continue;
                        }
                        conn.setRequestProperty(headerKey, t.getRequestHeaders().getFirst(headerKey));
                    }
                    
                    // Copy POST/PUT body payloads
                    if ("POST".equalsIgnoreCase(t.getRequestMethod()) || "PUT".equalsIgnoreCase(t.getRequestMethod())) {
                        conn.setDoOutput(true);
                        InputStream is = t.getRequestBody();
                        byte[] reqBody = is.readAllBytes();
                        OutputStream os = conn.getOutputStream();
                        os.write(reqBody);
                        os.close();
                    }
                    
                    int status = conn.getResponseCode();
                    
                    // Copy Response Headers
                    for (String headerKey : conn.getHeaderFields().keySet()) {
                        if (headerKey != null 
                            && !headerKey.equalsIgnoreCase("Transfer-Encoding")
                            && !headerKey.equalsIgnoreCase("WWW-Authenticate")) {
                            t.getResponseHeaders().set(headerKey, conn.getHeaderField(headerKey));
                        }
                    }
                    t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    
                    byte[] respBody;
                    if (status >= 200 && status < 300) {
                        respBody = conn.getInputStream().readAllBytes();
                    } else {
                        InputStream es = conn.getErrorStream();
                        respBody = (es != null) ? es.readAllBytes() : new byte[0];
                    }
                    
                    t.sendResponseHeaders(status, respBody.length);
                    OutputStream os = t.getResponseBody();
                    os.write(respBody);
                    os.close();
                    return;
                } catch (Exception err) {
                    System.err.println("Proxy exception on: " + targetUrl + " -> " + err.getMessage());
                    String response = "502 Bad Gateway: " + err.getMessage();
                    t.sendResponseHeaders(502, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
            }

            if (path.equals("/")) {
                path = "/index.html";
            }
            
            // Map request to local frontend directory
            File file = new File("frontend" + path);
            if (!file.exists() || file.isDirectory()) {
                file = new File("frontend" + path + "/index.html");
            }
            
            if (!file.exists()) {
                String response = "404 Not Found";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            // Determine MIME types
            String contentType = "text/html";
            if (path.endsWith(".css")) {
                contentType = "text/css";
            } else if (path.endsWith(".js")) {
                contentType = "application/javascript";
            } else if (path.endsWith(".json")) {
                contentType = "application/json";
            } else if (path.endsWith(".png")) {
                contentType = "image/png";
            } else if (path.endsWith(".svg")) {
                contentType = "image/svg+xml";
            }

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            t.getResponseHeaders().set("Content-Type", contentType);
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
            t.getResponseHeaders().set("Pragma", "no-cache");
            t.getResponseHeaders().set("Expires", "0");
            t.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = t.getResponseBody();
            os.write(fileBytes);
            os.close();
        }
    }
}
