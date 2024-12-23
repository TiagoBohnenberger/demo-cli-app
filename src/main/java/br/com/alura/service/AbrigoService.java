package br.com.alura.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.alura.command.CommandOption;
import br.com.alura.core.CLICommand;
import br.com.alura.core.ConsoleReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@CLICommand
@ApplicationScoped
public class AbrigoService {

    private final HttpClient client;
    private final ConsoleReader consoleReader;

    @Inject
    private AbrigoService(HttpClient httpClient, ConsoleReader consoleReader) {
        this.client = httpClient;
        this.consoleReader = consoleReader;
    }

    @CommandOption(1)
    public void listarTodos() throws IOException, InterruptedException {
        String uri = "http://localhost:8080/abrigos";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String responseBody = response.body();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Abrigos cadastrados:");
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String nome = jsonObject.get("nome").getAsString();
            System.out.println(id + " - " + nome);
        }
    }

    @CommandOption(2)
    public void cadastrar() throws IOException, InterruptedException {
        System.out.println("Digite o nome do abrigo:");
        String nome = new Scanner(System.in).nextLine();
        System.out.println("Digite o telefone do abrigo:");
        String telefone = new Scanner(System.in).nextLine();
        System.out.println("Digite o email do abrigo:");
        String email = new Scanner(System.in).nextLine();

        JsonObject json = new JsonObject();
        json.addProperty("nome", nome);
        json.addProperty("telefone", telefone);
        json.addProperty("email", email);

        String uri = "http://localhost:8080/abrigos";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("POST", BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Abrigo cadastrado com sucesso!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Erro ao cadastrar o abrigo:");
            System.out.println(responseBody);
        }

    }

    @CommandOption(3)
    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = new Scanner(System.in).nextLine();

        String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID ou nome não cadastrado!");
            return;
        }
        String responseBody = response.body();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Pets cadastrados:");
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String tipo = jsonObject.get("tipo").getAsString();
            String nome = jsonObject.get("nome").getAsString();
            String raca = jsonObject.get("raca").getAsString();
            int idade = jsonObject.get("idade").getAsInt();
            System.out.println(id + " - " + tipo + " - " + nome + " - " + raca + " - " + idade + " ano(s)");
        }
    }

    @CommandOption(4)
    public void importarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        String nomeArquivo = new Scanner(System.in).nextLine();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + nomeArquivo);
            return;
        }
        String line;
        while ((line = reader.readLine()) != null) {
            String[] campos = line.split(",");
            String tipo = campos[0];
            String nome = campos[1];
            String raca = campos[2];
            int idade = Integer.parseInt(campos[3]);
            String cor = campos[4];
            Float peso = Float.parseFloat(campos[5]);

            JsonObject json = new JsonObject();
            json.addProperty("tipo", tipo.toUpperCase());
            json.addProperty("nome", nome);
            json.addProperty("raca", raca);
            json.addProperty("idade", idade);
            json.addProperty("cor", cor);
            json.addProperty("peso", peso);

            String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method("POST", BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Pet cadastrado com sucesso: " + nome);
            } else if (statusCode == 404) {
                System.out.println("Id ou nome do abrigo não encontrado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Erro ao cadastrar o pet: " + nome);
                System.out.println(responseBody);
                break;
            }
        }
        reader.close();
    }
}