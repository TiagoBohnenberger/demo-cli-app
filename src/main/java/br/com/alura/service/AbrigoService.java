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
import java.util.List;
import java.util.Properties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.alura.command.CommandOption;
import br.com.alura.core.CLICommand;
import br.com.alura.core.ConsoleReader;
import br.com.alura.domain.Abrigo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import static br.com.alura.util.Functions.println;


@Slf4j
@CLICommand
@ApplicationScoped
public class AbrigoService {
    private final String baseUrl;
    private final HttpClient client;
    private final ConsoleReader consoleReader;

    @Inject
    public AbrigoService(HttpClient client, ConsoleReader consoleReader, Properties properties) {
        this.client = client;
        this.consoleReader = consoleReader;
        this.baseUrl = properties.getProperty("routes.abrigo.base.url", "http://localhost:8080/abrigos");
    }

    @CommandOption(1)
    public void listarTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        HttpResponse<List<Abrigo>> response = client
                .send(request, JsonBody.handle(new TypeToken<List<Abrigo>>() {}.getType()));

        List<Abrigo> abrigos = response.body();

        if (abrigos.isEmpty()) {
            println("Não há abrigos cadastrados");
            return;
        }

        println("Abrigos cadastrados:");
        abrigos.forEach(System.out::println);
    }

    @CommandOption(2)
    public void cadastrar() throws IOException, InterruptedException {
        println("Digite o nome do abrigo:");
        String nome = consoleReader.nextLine();

        println("Digite o telefone do abrigo:");
        String telefone = consoleReader.nextLine();

        println("Digite o email do abrigo:");
        String email = consoleReader.nextLine();

        CadastroAbrigoRequest cadastroAbrigo = new CadastroAbrigoRequest(nome, telefone, email);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(JsonBody.publish(cadastroAbrigo))
                .build();

        var response = client.send(request, JsonBody.handle(Object.class));
        int statusCode = response.statusCode();

        var responseBody = response.body();
        if (statusCode == 200) {
            println("Abrigo cadastrado com sucesso!");
            println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            println("Erro ao cadastrar o abrigo:");
            println(responseBody);
        }

    }

    @CommandOption(3)
    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        println("Digite o id ou nome do abrigo:");
        String idOuNome = consoleReader.nextLine();

        String uri = baseUrl + idOuNome + "/pets";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            println("ID ou nome não cadastrado!");
            return;
        }
        String responseBody = response.body();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        println("Pets cadastrados:");
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String tipo = jsonObject.get("tipo").getAsString();
            String nome = jsonObject.get("nome").getAsString();
            String raca = jsonObject.get("raca").getAsString();
            int idade = jsonObject.get("idade").getAsInt();
            println(id + " - " + tipo + " - " + nome + " - " + raca + " - " + idade + " ano(s)");
        }
    }

    @CommandOption(4)
    public void importarPetsDoAbrigo() throws IOException, InterruptedException {
        println("Digite o id ou nome do abrigo:");
        String idOuNome = consoleReader.nextLine();

        println("Digite o nome do arquivo CSV:");
        String nomeArquivo = consoleReader.nextLine();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            println("Erro ao carregar o arquivo: " + nomeArquivo);
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

            String uri = baseUrl + idOuNome + "/pets";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .method("POST", BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                println("Pet cadastrado com sucesso: " + nome);
            } else if (statusCode == 404) {
                println("Id ou nome do abrigo não encontrado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                println("Erro ao cadastrar o pet: " + nome);
                println(responseBody);
                break;
            }
        }
        reader.close();
    }
}