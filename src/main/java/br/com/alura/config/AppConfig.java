package br.com.alura.config;

import java.net.http.HttpClient;
import java.util.Properties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import br.com.alura.core.ConsoleReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@ApplicationScoped
class AppConfig {

    @Default
    @Produces
    @ApplicationScoped
    HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }

    @Default
    @Produces
    @Singleton
    ConsoleReader consoleReader() {
        return ConsoleReader.instance();
    }

    @Default
    @Produces
    @Singleton
    Gson gson() {
        return new GsonBuilder().create();
    }

    @Default
    @Produces
    @Singleton
    Properties properties() {
        return new Properties();
    }
}