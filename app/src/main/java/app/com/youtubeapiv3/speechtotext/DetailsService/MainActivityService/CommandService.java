package app.com.youtubeapiv3.speechtotext.DetailsService.MainActivityService;

import java.util.ArrayList;
import java.util.List;

import app.com.youtubeapiv3.activites.MainActivity;


public class CommandService {
    private List<Command> commands;



    MainActivity context ;


    public CommandService(MainActivity context)
    {
        this.context = context;
        this.commands = new ArrayList<Command>();
        this.commands.add(new Command("skończ", false));
        this.commands.add(new Command("stop", false));
        this.commands.add(new Command("zacznij", false));
        this.commands.add(new Command("start", false));

        this.commands.add(new Command("następny", false));
        this.commands.add(new Command("next", false));
        this.commands.add(new Command("poprzedni", false));
        this.commands.add(new Command("previous", false));
        this.commands.add(new Command("playlist", false));
        this.commands.add(new Command("plejlista", false));

        this.commands.add(new Command("głośniej", false));
        this.commands.add(new Command("louder", false));
        this.commands.add(new Command("ciszej", false));
        this.commands.add(new Command("quieter", false));
        this.commands.add(new Command("wyłącz się", false));
        this.commands.add(new Command("turn off", false));
        this.commands.add(new Command("dodaj piosenkę do ulubionych", false));
        this.commands.add(new Command("add song to favorites", false));
        this.commands.add(new Command("odtwórz ulubione", false));
        this.commands.add(new Command("play favorite", false));
        this.commands.add(new Command("odtwórz album", false));
        this.commands.add(new Command("play album", false));
        this.commands.add(new Command("odtwórz", true));
        this.commands.add(new Command("play", true));
        this.commands.add(new Command("znajdź", true));
        this.commands.add(new Command("szukaj", true));
        this.commands.add(new Command("find", true));
    }

    void checkCommand(ArrayList<String> textToCheck){
        String commandPrefix = "";
        for (Command element: commands) {
            commandPrefix = element.getCommandPrefix();
            for (String text:textToCheck) {
                text = text.toLowerCase();
                if(text.startsWith(commandPrefix)){
                    findCommand(commandPrefix,text);
                    return;
                }
            }

        }

    }

    /*
    angielskie z malej
    polskie z duzej
     */
    private void findCommand(String commandPrefix, String textToCheck) {

        switch(commandPrefix) {
            case "szukaj":
                if(textToCheck.length() < 7)
                    return;
                context.searchModeDetected(textToCheck.substring(7, textToCheck.length()));
                return;
            case "find" :
                context.searchModeDetected(textToCheck.substring(5, textToCheck.length()));
                return;
            case "znajdź":
                context.searchModeDetected(textToCheck.substring(7, textToCheck.length()));
                return;
            default:
                break;
        }

        if(commandPrefix.equals("playlist")||commandPrefix.equals("playlista")){
            context.changeTabDetected("playlist");
            return;
        }

        if(commandPrefix.equals("następny")||commandPrefix.equals("next")){
            context.changeTabDetected("next");
            return;
        }
        else if(commandPrefix.equals("poprzedni")||commandPrefix.equals("previous")){
            context.changeTabDetected("previous");
            return;
        }






    }


}
