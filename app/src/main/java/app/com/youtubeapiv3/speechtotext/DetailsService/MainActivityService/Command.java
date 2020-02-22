package app.com.youtubeapiv3.speechtotext.DetailsService.MainActivityService;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private List<String> command;
    private boolean isNeededArgumentFlag;

    public Command(String word, boolean isNeededArgumentFlag)
    {
        command = new ArrayList<>();
        String[] words = word.split(" ");
        for(int i = 0; i < words.length; i++)
        {
            this.command.add(words[i]);
        }
        this.isNeededArgumentFlag = isNeededArgumentFlag;
    }

    public List<String> getCommand() {
        return this.command;
    }

    public boolean isNeededArgument()
    {
        return this.isNeededArgumentFlag;
    }

    public void setIsNeededArgumentFlag(boolean isNeededArgumentFlag)
    {
        this.isNeededArgumentFlag = isNeededArgumentFlag;
    }

    public String getCommandPrefix() {
        return command.get(0);
    }
}