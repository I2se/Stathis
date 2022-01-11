package fr.sothis.stats.commands.stats;

import com.mattmalec.pterodactyl4j.DataType;
import com.mattmalec.pterodactyl4j.application.entities.ApplicationServer;
import com.mattmalec.pterodactyl4j.application.entities.Node;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.PteroClient;
import com.mattmalec.pterodactyl4j.client.entities.Utilization;
import fr.sothis.stats.Main;
import fr.sothis.stats.commands.manager.Command;
import fr.sothis.stats.commands.manager.CommandExist;
import fr.sothis.stats.commands.manager.CommandInfo;
import fr.sothis.stats.commands.manager.utils.Perms;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

@CommandExist
@CommandInfo(
        name = "stats",
        description = "Donne toutes les informations concernant Pterodactyl",
        permission = Perms.OWNER
)
public class StatsCommand extends Command {

    private final PteroApplication app = Main.getApiApp();
    private final PteroClient client = Main.getApiCli();

    @Override
    public void execute(SlashCommandEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.blue);
        embedBuilder.setTitle("Statistiques Realcraft Ptero");
        double cpu = 00.00;
        double ram = 00.00;
        double disk = 00.00;
        for (ApplicationServer server : app.retrieveServers().execute()) {
            ClientServer serverClient = client.retrieveServerByIdentifier(server.getIdentifier()).execute();
            Utilization info = serverClient.retrieveUtilization().execute();
            double cal1 = info.getMemory() / (DataType.GB.getMbValue() * Math.pow(2, 20));
            double cal2 = info.getDisk() / (DataType.GB.getMbValue() * Math.pow(2, 20));
            embedBuilder.addField(server.getName(), "IP : " + server.getDefaultAllocation().retrieve().execute().getPort() + " \n" +
                    "Owner : " + server.getOwner().retrieve().execute().getUserName() + " \n" +
                    "Status : " + info.getState().name() + " \n" +
                    "CPU : " + Math.round(info.getCPU()) + "% \n" +
                    "RAM : " + Math.round(cal1) + "GB / " + Integer.parseInt(server.getLimits().getMemory()) / 1024 + "GB \n" +
                    "Disk : " + Math.round(cal2) + "GB / " + Integer.parseInt(server.getLimits().getDisk()) / 1024 + "GB", true);
            cpu = cpu + info.getCPU();
            ram = ram + info.getMemory() / (DataType.GB.getMbValue() * Math.pow(2, 20));
            disk = disk + info.getDisk() / (DataType.GB.getMbValue() * Math.pow(2, 20));
        }
        Node node = app.retrieveNodesByName("alpha", false).execute().get(0);
        int allomemo = Integer.parseInt(node.getAllocatedMemory()) / 1024;
        int allodisk = Integer.parseInt(node.getAllocatedDisk()) / 1024;
        int memo = Integer.parseInt(node.getMemory()) / 1024;
        int disc = Integer.parseInt(node.getDisk()) / 1024;
        embedBuilder.setDescription("Nombre de serveurs : " + app.retrieveServers().execute().size() + " \n" +
                "Nombre de users : " + app.retrieveUsers().execute().size() + " \n" +
                "CPU : " + Math.round(cpu) + "% / 1200% \n" +
                "RAM : " + allomemo + "GB / " + memo + "GB   - Usage : " + Math.round(ram) + "GB \n" +
                "Disk : " + allodisk + "GB / " + disc+ "GB   - Usage : " + Math.round(disk) + "GB");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
