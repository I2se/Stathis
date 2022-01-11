package fr.sothis.stats.commands.manager.arg.discord;

import fr.sothis.stats.commands.manager.arg.ArgReader;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class GuildChannelAR implements ArgReader<GuildChannel> {

    @Override
    public GuildChannel read(OptionMapping optionValue) {
        return optionValue.getAsGuildChannel();
    }
}
