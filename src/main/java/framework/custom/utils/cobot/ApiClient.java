package framework.custom.utils.cobot;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.net.HttpURLConnection;

import java.net.URI;

import java.net.URISyntaxException;

import java.net.URL;



public class ApiClient {

    public void consumeApi(String apiUrl, String jsonInputString) {

        int maxAttempts = 1;

        int attempt = 1;

        boolean connectionSuccessful = false;



        while (attempt <= maxAttempts && !connectionSuccessful) {

            try {

                URI uri = new URI(apiUrl);

                URL url = uri.toURL();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(3000); // Establecer el tiempo máximo de espera en milisegundos

                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-Type", "application/json");

                connection.setRequestProperty("Accept", "application/json");

                connection.setDoOutput(true);



                // Enviar el JSON en el cuerpo de la solicitud

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(jsonInputString.getBytes());

                outputStream.flush();



                // Obtener el código de respuesta HTTP

                int responseCode = connection.getResponseCode();

                System.out.println("Código de respuesta: " + responseCode);



                // Leer la respuesta del servidor

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;

                StringBuilder response = new StringBuilder();



                while ((line = reader.readLine()) != null) {

                    response.append(line);

                }

                reader.close();



                // Imprimir la respuesta del servidor

                System.out.println("Respuesta: " + response.toString());



                connection.disconnect();

                connectionSuccessful = true;

            } catch (URISyntaxException e) {

                e.printStackTrace();

            } catch (IOException e) {

                System.out.println("Intento " + attempt + ": Error de conexión. Reintentando...");

                attempt++;

            }

        }



        if (!connectionSuccessful) {

            System.out.println("No se pudo establecer la conexión después de " + maxAttempts + " intentos.");

        }

    }

}