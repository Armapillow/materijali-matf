package UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {

    private static final int BUF_SIZE = 1024;
    public  static final int PORT     = 12321;
    private static final String path  = "studenti.txt";
    private static final Map<String, Student> students = new HashMap<>();

    public static void main(String[] args) {

        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.err.println("Server started on port " + PORT);
            parseFile(path);
//            for (var entry : students.entrySet()) {
//                System.out.println(entry.getKey() + "\t" + entry.getValue());
//            }
            byte[] buffer = new byte[BUF_SIZE];

            while (true) {

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(request);

                String result = new String(request.getData(), 0, request.getLength(), StandardCharsets.US_ASCII);
                result = result.trim();
                System.err.println("GOT: " + result);

                String responseStr   = getResponse(result);
                byte[] responseBytes = responseStr.getBytes();

                DatagramPacket response = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        request.getAddress(), request.getPort());
                serverSocket.send(response);

            }

        } catch (IOException ex) {
            System.err.println("Server error");
            System.err.println(ex.getMessage());
        }
    }

    private static String getResponse(String message) {
        String[] fields = message.split(" ");
        String index    = fields[0].trim();
        String command  = fields[1].trim();

        String response = "";
        if (!students.containsKey(index)) {
            response = "Zahtev nije validan!";
        } else {
            Student stud = students.get(index);
            switch (command) {
                case    "ime"    -> response = stud.getName();
                case    "prosek" -> response = stud.getGrade();
                default          -> response = "Zahtev nije validan!";
            }
        }

//        response = response + "\n";
        return response;
    }

    private static void parseFile(String path) {

        try (BufferedReader input = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = input.readLine()) != null) {
                String[] fields = line.split(",");

                String indeks   = fields[0].trim();
                String ime      = fields[1].trim();
                String ocena    = fields[2].trim();

                Student student = new Student(indeks, ime, ocena);
                students.put(indeks, student);
            }
            System.err.println("File successfully read!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Student {
        private String index;
        private String grade;
        private String name;

        Student(String index, String name, String grade) {
            this.index = index;
            this.grade = grade;
            this.name  = name;
        }

        public String getGrade() {
            return grade;
        }

        public String getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return index + ": " + name + ": " + grade;
        }
    }
}
