package barqsoft.footballscores.widget;

/**
 * Created by graingersoftware on 11/3/15.
 */
public class WidgetItem {

    public String leftTeam;
    public String rightTeam;
    public String score;
    public WidgetItem(String leftTeam, String rightTeam, String score) {
        this.leftTeam = leftTeam;
        this.rightTeam = rightTeam;
        this.score = score;
    }
}
