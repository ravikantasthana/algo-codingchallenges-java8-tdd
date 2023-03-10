package multithreading.performanceoptimization;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    private static final String INPUT_FILE = "multithreading/optimizing-for-throughput/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 2;

    public static void main(String[] args) throws URISyntaxException, IOException {
        FileResourceUtils fileResourceUtils = new FileResourceUtils();
        String bookText = new String(Files.readAllBytes(Paths.get(fileResourceUtils.getFileFromResource(INPUT_FILE))));
        startHttpServer(bookText);
    }

    private static void startHttpServer(String bookText) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/search", new WordCountHandler(bookText));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executor);
        httpServer.start();
    }

    private static class FileResourceUtils {

        public URI getFileFromResource(String filePath) throws URISyntaxException{
            return getClass().getClassLoader().getResource(filePath).toURI();
        }
    }

    private record WordCountHandler(String bookText) implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String[] qKeyValue = query.split("=");
            String action = qKeyValue[0];
            String word = qKeyValue[1];

            if (!action.equals("word")) {
                exchange.sendResponseHeaders(400,0);
                return;
            }

            long count = Arrays.stream(bookText.split(" ")).filter(w -> w.equalsIgnoreCase(word)).count();
//            long count = countWord(bookText);

            byte[] response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while (index >= 0) {
                index = bookText.indexOf(word, index);

                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
