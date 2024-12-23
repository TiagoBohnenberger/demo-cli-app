package br.com.alura.command;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import br.com.alura.core.Eager;
import br.com.alura.util.Functions;
import br.com.alura.util.Try;
import lombok.extern.log4j.Log4j2;

import static br.com.alura.util.Functions.println;


@Log4j2
@Eager(priority = 2)
@ApplicationScoped
class MainConsole {

    private final UserInputHandler userInputHandler;

    @Inject
    private MainConsole(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    private void welcome() {
        Try.with("* ADOPET APP //")
                .apply(Functions::printAnsiArt)
                .orElse(Functions::println);

        println("Escolha uma opção:");
        println("1 -> Listar abrigos cadastrados");
        println("2 -> Cadastrar novo abrigo");
        println("3 -> Listar pets do abrigo");
        println("4 -> Importar pets do abrigo");
        println("5 -> Sair");
        println("Digite o número da opção e pressione Enter");
        println();

        userInputHandler.start();
    }


    @PostConstruct
    private void postConstruct() {
        welcome();
    }

}