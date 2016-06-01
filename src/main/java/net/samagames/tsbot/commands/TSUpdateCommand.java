package net.samagames.tsbot.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import net.samagames.tsbot.TSBot;
import net.samagames.tsbot.redis.TeamSpeakLinkBean;

import java.util.UUID;

/**
 * Created by Rigner for project SamaBot.
 */
public class TSUpdateCommand extends AbstractCommand
{
    public TSUpdateCommand(TSBot tsBot)
    {
        super(tsBot);
    }

    @Override
    public boolean run(String[] args)
    {
        if (args.length != 3)
            return false;
        try
        {
            UUID uuid = UUID.fromString(args[2]);
            TeamSpeakLinkBean bean = this.bot.getDatabaseConnector().getLinkInfo(uuid);
            if (bean == null)
            {
                this.bot.getPubsub().respondError(args[0], "NOT_LINKED");
                return true;
            }
            Client client = this.bot.getTs3Api().getClientByUId(bean.getIdentity());
            if (client == null)
            {
                this.bot.getPubsub().respondError(args[0], "NOT_ONLINE");
                return true;
            }
            TSLinkCommand.updateRankForPlayer(this.bot, uuid, client);
            this.bot.getPubsub().respond(args[0], "OK");
        }
        catch (Exception ignored)
        {
            this.bot.getPubsub().respondError(args[0], ignored.getMessage());
            return true;
        }
        return true;
    }
}
