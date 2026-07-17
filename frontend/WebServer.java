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
                    String workerId = t.getRequestHeaders().getFirst("X-Worker-Id");
                    String contentType = t.getRequestHeaders().getFirst("Content-Type");
                    
                    if (username == null || role == null) {
                        username = "user";
                        role = "guest";
                    }
                    if (workerId == null || workerId.trim().isEmpty()) {
                        workerId = "unknown";
                    }
                    
                    File avatarDir = new File("frontend/user_uploads/avatars");
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
                    
                    // Sanitize worker id for file systems
                    String sanitizedWorkerId = workerId.replaceAll("[^a-zA-Z0-9-]", "_");
                    String filename = String.format("%s_profile_img.%s", sanitizedWorkerId, ext);
                    File outFile = new File(avatarDir, filename);
                    
                    // Clean up any old avatar files with different extensions for this user
                    File[] existingFiles = avatarDir.listFiles();
                    if (existingFiles != null) {
                        for (File f : existingFiles) {
                            if (f.getName().startsWith(sanitizedWorkerId + "_profile_img.")) {
                                f.delete();
                            }
                        }
                    }
                    
                    InputStream is = t.getRequestBody();
                    byte[] bytes = is.readAllBytes();
                    Files.write(outFile.toPath(), bytes);
                    
                    String relativeUrl = "user_uploads/avatars/" + filename;
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

            // Endpoint: POST /api/upload-document
            // WHAT IT DOES: 
            // Handles multipart document file streams uploaded from the profile page.
            // 
            // PARAMETERS/INPUTS (HEADERS):
            // - X-Worker-Id: unique code identifier of the employee (e.g. EMP_001).
            // - X-Document-Type: category string (e.g. panCard, aadhaarCard, workPermit, contract).
            // - Content-Type: mime type of the file.
            // - X-File-Name: original name of the file.
            // 
            // STORAGE LOCATION:
            // Saves the raw bytes of the file directly on the local server filesystem under:
            // "frontend/user_uploads/documents/<category>/<worker_id>_<docType>_doc.<ext>".
            // 
            // DATA OUTPUT:
            // Returns a JSON response containing the success status and the relative url path of the saved file.
            if (path.equals("/api/upload-document") && "POST".equalsIgnoreCase(t.getRequestMethod())) {
                try {
                    String workerId = t.getRequestHeaders().getFirst("X-Worker-Id");
                    String docType = t.getRequestHeaders().getFirst("X-Document-Type");
                    String contentType = t.getRequestHeaders().getFirst("Content-Type");
                    String fileNameHeader = t.getRequestHeaders().getFirst("X-File-Name");
                    
                    if (workerId == null || workerId.trim().isEmpty()) {
                        workerId = "unknown";
                    }
                    if (docType == null || docType.trim().isEmpty()) {
                        docType = "contract";
                    }
                    
                    // Maps the document category type to the exact user upload subfolders requested by the user
                    String category = "contract";
                    if ("panCard".equalsIgnoreCase(docType)) {
                        category = "PAN Card (Permanent Account Number)";
                    } else if ("aadhaarCard".equalsIgnoreCase(docType)) {
                        category = "Aadhaar Card (UIDAI)";
                    } else if ("workPermit".equalsIgnoreCase(docType)) {
                        category = "Work Permit _ Contract Agreement";
                    } else if ("payslip".equalsIgnoreCase(docType)) {
                        category = "Payslips";
                    }
                    
                    // Resolves local server directory path to write the file
                    File docDir = new File("frontend/user_uploads/documents/" + category);
                    if (!docDir.exists()) {
                        docDir.mkdirs(); // Creates the directory tree if it does not exist yet
                    }
                    
                    // Deduces extension based on content type or original filename header
                    String ext = "pdf";
                    if (contentType != null) {
                        if (contentType.contains("png")) {
                            ext = "png";
                        } else if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                            ext = "jpg";
                        } else if (contentType.contains("gif")) {
                            ext = "gif";
                        } else if (contentType.contains("webp")) {
                            ext = "webp";
                        }
                    } else if (fileNameHeader != null && fileNameHeader.contains(".")) {
                        ext = fileNameHeader.substring(fileNameHeader.lastIndexOf(".") + 1);
                    }
                    
                    // Sanitizes employee worker ID for file name compliance
                    String sanitizedWorkerId = workerId.replaceAll("[^a-zA-Z0-9-]", "_");
                    String filename = String.format("%s_%s_doc.%s", sanitizedWorkerId, docType, ext);
                    if ("payslip".equalsIgnoreCase(docType)) {
                        filename = String.format("%s_payslip_%d.%s", sanitizedWorkerId, System.currentTimeMillis(), ext);
                    }
                    File outFile = new File(docDir, filename);
                    
                    // Reads input stream payload and writes raw bytes to destination file path
                    InputStream is = t.getRequestBody();
                    byte[] bytes = is.readAllBytes();
                    Files.write(outFile.toPath(), bytes);
                    
                    // Builds relative URL pointing to the static user uploads location served directly from the root
                    String relativeUrl = "user_uploads/documents/" + category + "/" + filename;
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

            if (path.equals("/api/upload-announcement-attachment") && "POST".equalsIgnoreCase(t.getRequestMethod())) {
                try {
                    String contentType = t.getRequestHeaders().getFirst("Content-Type");
                    String fileNameHeader = t.getRequestHeaders().getFirst("X-File-Name");
                    
                    File annDir = new File("frontend/user_uploads/announcements");
                    if (!annDir.exists()) {
                        annDir.mkdirs();
                    }
                    
                    String ext = "png";
                    if (contentType != null) {
                        if (contentType.contains("png")) {
                            ext = "png";
                        } else if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                            ext = "jpg";
                        } else if (contentType.contains("gif")) {
                            ext = "gif";
                        } else if (contentType.contains("webp")) {
                            ext = "webp";
                        } else if (contentType.contains("pdf")) {
                            ext = "pdf";
                        } else if (contentType.contains("mp4")) {
                            ext = "mp4";
                        } else if (contentType.contains("webm")) {
                            ext = "webm";
                        } else if (contentType.contains("ogg")) {
                            ext = "ogg";
                        } else if (contentType.contains("quicktime")) {
                            ext = "mov";
                        }
                    } else if (fileNameHeader != null && fileNameHeader.contains(".")) {
                        ext = fileNameHeader.substring(fileNameHeader.lastIndexOf(".") + 1);
                    }
                    
                    String uniqueName = java.util.UUID.randomUUID().toString() + "." + ext;
                    File outFile = new File(annDir, uniqueName);
                    
                    InputStream is = t.getRequestBody();
                    byte[] bytes = is.readAllBytes();
                    Files.write(outFile.toPath(), bytes);
                    
                    String relativeUrl = "user_uploads/announcements/" + uniqueName;
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
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (path.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (path.endsWith(".webp")) {
                contentType = "image/webp";
            } else if (path.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (path.endsWith(".mp4")) {
                contentType = "video/mp4";
            } else if (path.endsWith(".webm")) {
                contentType = "video/webm";
            } else if (path.endsWith(".ogg")) {
                contentType = "video/ogg";
            } else if (path.endsWith(".mov")) {
                contentType = "video/quicktime";
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
