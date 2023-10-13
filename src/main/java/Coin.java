import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Coin extends Element{
    public Coin(int x, int y) {super(x,y);}
    public void draw(TextGraphics screen) {
        screen.setForegroundColor(TextColor.Factory.fromString("#999933"));
        screen.enableModifiers(SGR.BOLD);
        screen.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "◙");
    }
    public boolean equals(Object o){
        if(o == null || this.getClass() != o.getClass()) return false;
        return(this == o || this.getPosition().equals(((Coin) o).getPosition()));
    }
}