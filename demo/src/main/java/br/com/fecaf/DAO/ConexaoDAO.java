import gnu.io.*;
import java.io.*;
import java.util.*;

public class ArduinoRXTX {
    public static void main(String[] args) {
        try {
            // Lista as portas seriais disponíveis
            Enumeration<?> portas = CommPortIdentifier.getPortIdentifiers();
            System.out.println("Portas disponíveis:");
            while (portas.hasMoreElements()) {
                CommPortIdentifier pid = (CommPortIdentifier) portas.nextElement();
                System.out.println(" - " + pid.getName());
            }

            // Substitua conforme seu sistema:
            // Windows → "COM3"
            // Linux → "/dev/ttyUSB0" ou "/dev/ttyACM0"
            String portaArduino = "COM3";
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portaArduino);

            if (portId.isCurrentlyOwned()) {
                System.out.println("Erro: porta em uso!");
                return;
            }

            CommPort commPort = portId.open("LeitorArduino", 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(
                    9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
                );

                InputStream in = serialPort.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                System.out.println("Conectado ao Arduino! Lendo dados...");

                String linha;
                while ((linha = reader.readLine()) != null) {
                    if (linha.startsWith("GAS:")) {
                        String valor = linha.substring(4);
                        System.out.println("Leitura de gás: " + valor + " PPM");
                    } else {
                        System.out.println("Recebido: " + linha);
                    }
                }
            } else {
                System.out.println("A porta não é serial!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}