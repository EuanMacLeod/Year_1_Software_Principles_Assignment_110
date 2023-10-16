import java.io.Serial;
import java.io.Serializable;

public class Tutorial implements Serializable {
    //this class holds all the tutorial information

    @Serial
    private static final long serialVersionUID = 5575895183782648059L;
    public final String mainMenuTutorial = "\nHere Is The Main Menu, This Is Where You Interact With The Game.\n\tYou Can Select An Option To Learn Move About It.\n";
    public final String inventoryMenuTutorial = "\nHere Is The Menu For Your Inventory, From Here You Can Access Everything To Do With The Items That You Own.\n\tSelect An Option To Learn More About It.\n";
    public final String craftingMenuTutorial = "\nFrom Here You Can See All Of The Items That You Are Able To Craft, If You Would Like To Craft One Of Them Then All You Have To Do Is Enter The ID Number Of That Item.\n";
    public final String itemInformationMenuTutorial = "\nHere Is Where You Can Learn About New Items, All The Items You Can Possible Obtain Will Be Displayed.\nTo Learn About How To Obtain An Item All You Have To Do Is Enter It's ID Number.\n";
    public final String itemRemovalMenuTutorial = "\nFrom Here You Can Discard Any Items That You Don't Need.\n";
    public final String viewQuestItemTutorial = "\nHere You Can See The Item That You Need To Create In Order To Progress To The Next Level, Here Is Also Where You Can Complete The Level From If You Have That Item In Your Inventory.\n";
    public final String moveMenuTutorial = "\nHere You Can View All Of The Locations Available To You And, If You Wish, You Can Travel To One Of Those Locations.\n";
    public final String harvestResourceTutorial = "\nHere Is Where You Can Gather Natural Resources.\nDifferent Areas May Have Different Resources To Collect.\nYou Can View How To Obtain A Specific Resource Under The Inventory Menu If You Wish.\n";
    public final String saveGameTutorial = "\nThis Saves Your Current Progress So That You Can Continue Playing Later On.\n\tRemember To Save Your Game Before Exiting Otherwise You May Lose Your Progress.\n";
    public boolean tutorialEnabled;
    public boolean mainMenuTutorialShown;
    public boolean inventoryMenuTutorialShown;
    public boolean craftingMenuTutorialShown;
    public boolean itemInformationMenuTutorialShown;
    public boolean itemRemovalMenuTutorialShown;
    public boolean viewQuestItemTutorialShown;
    public boolean moveMenuTutorialShown;
    public boolean harvestResourceTutorialShown;
    public boolean saveGameTutorialShown;


}
