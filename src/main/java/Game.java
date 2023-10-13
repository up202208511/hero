import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.*;
import java.io.IOException;

import static com.googlecode.lanterna.input.KeyType.*;

public class Game {
    public Game(){
        try {
            TerminalSize terminalSize = new TerminalSize(80, 40);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);

            screen.setCursorPosition(null);
            screen.startScreen();
            screen.doResizeIfNecessary();
        } catch (IOException e){
            e.printStackTrace();
        }
        arena = new Arena(80, 40);
    }
    private void draw() throws IOException { // make screen
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }
    private void processKey(com.googlecode.lanterna.input.KeyStroke key){ // move Hero as player uses keys
        if ((key.getKeyType()) == ArrowUp) arena.moveHero(arena.moveUp());
        else if ((key.getKeyType()) == ArrowDown) arena.moveHero(arena.moveDown());
        else if ((key.getKeyType()) == ArrowLeft) arena.moveHero(arena.moveLeft());
        else if ((key.getKeyType()) == ArrowRight) arena.moveHero(arena.moveRight());
    }

    public void run() { // game function to run all
        try {
            while(true) {
                draw();
                com.googlecode.lanterna.input.KeyStroke key = screen.readInput();
                processKey(key);
                if(arena.verifyMonsterCollisions()){
                    screen.close();
                    break;
                }
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == ('q'))
                    screen.close();
                if (key.getKeyType() == KeyType.EOF)
                    break;
                arena.moveMonsters();
                if(arena.verifyMonsterCollisions()){
                    screen.close();
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private Screen screen;
    private Arena arena;
}
