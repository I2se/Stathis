package fr.sothis.stats;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import fr.sothis.stats.commands.manager.CommandRegistry;
import fr.sothis.stats.commands.stats.StatsCommand;
import fr.sothis.stats.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class Main {

    private static JDA jda;
    private static PteroApplication apiApp;
    private static PteroClient apiCli;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        LoggerContext loggerContextRefec = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLoggerRefec = loggerContextRefec.getLogger("org.reflections");
        rootLoggerRefec.setLevel(Level.OFF);

        try {
            jda = JDABuilder.createDefault(Config.TOKEN)
                    .build()
                    .awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        apiApp = PteroBuilder.createApplication("https://panel.realcraft.fr", Config.PTERO_APP);
        apiCli = PteroBuilder.createClient("https://panel.realcraft.fr", Config.PTERO_CLI);

        CommandRegistry commandRegistry = new CommandRegistry(jda);
        commandRegistry.registerDefaults();
        commandRegistry.registerCommand(StatsCommand.class);
        commandRegistry.updateDiscord();

        jda.addEventListener(commandRegistry);
        printInitialize(commandRegistry);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if(message.equals("stop")) {
                jda.shutdown();
                System.exit(0);
            }
        }
    }

    private void printInitialize(CommandRegistry commandRegistry) {
        LOGGER.info("===================================");
        LOGGER.info("Start Bot " + jda.getSelfUser().getAsTag());
        LOGGER.info("Register Events :");
        jda.getEventManager().getRegisteredListeners().forEach(o -> {
            LOGGER.info("- " + o.getClass().getSimpleName());
        });
        LOGGER.info("Register Commands :");
        commandRegistry.commands.forEach((s, command) -> {
            String letter = String.valueOf(s.charAt(0));
            String maj = letter.toUpperCase();
            String name = s.replace(s.charAt(0), maj.toCharArray()[0]);
            LOGGER.info("- " + name + " : " + command.getClass().getSimpleName());
        });
        LOGGER.info("{} is ready", jda.getSelfUser().getAsTag());
        LOGGER.info("===================================");
    }

    public static PteroApplication getApiApp() {
        return apiApp;
    }

    public static PteroClient getApiCli() {
        return apiCli;
    }
}
