import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    public Arena(int width, int height){
        this.height = height;
        this.width = width;
        hero = new Hero(10, 10);
        walls = createWalls();
        coins = createCoins();
        monsters = createMonsters();
    }
    public void draw(TextGraphics screen) { // Function to draw arena
        screen.setBackgroundColor(TextColor.Factory.fromString("#008000"));
        screen.fillRectangle(new TerminalPosition(0,0), new TerminalSize(width, height), ',');
        hero.draw(screen);
        for(Wall wall : walls)
            wall.draw(screen);
        for(Coin coin : coins)
            coin.draw(screen);
        for(Monster monster : monsters)
            monster.draw(screen);
    }
    // Functions for position
    public Position moveUp() {
        return new Position(hero.getPosition().getX(), hero.getPosition().getY() - 1);
    }
    public Position moveDown() {
        return new Position(hero.getPosition().getX(), hero.getPosition().getY() + 1);
    }
    public Position moveLeft() {
        return new Position(hero.getPosition().getX()-1, hero.getPosition().getY());
    }
    public Position moveRight() {
        return new Position(hero.getPosition().getX()+1, hero.getPosition().getY());
    }
    // Functions for Hero movement
    public void moveHero(Position position){
        if(canHeroMove(position))
            hero.setPosition(position);

        retrieveCoins();
    }
    private boolean canHeroMove(Position pos){
        boolean cond1 = (pos.getX() >= 0 && pos.getX() < width);
        boolean cond2 = (pos.getY() >= 0 && pos.getY() < height);
        boolean cond3;
        cond3 = (!walls.contains(new Wall(pos.getX(), pos.getY())));
        if (cond1) if (cond2) if (cond3) return true;
        return false;
    }

    private List<Wall> createWalls(){
        List<Wall> walls = new ArrayList<>();

        for(int c=0; c < width; c++){
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height-1));
        }

        for(int r=0; r < height; r++){
            walls.add(new Wall(0, r));
            walls.add(new Wall(width-1, r));
        }
        return walls;
    }
    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for(int i=0; i<5; i++){
            Coin newcoin = new Coin(random.nextInt(width-2) + 1,
                    random.nextInt(height-2)+1);
            if(!coins.contains(newcoin) && !newcoin.getPosition().equals(hero.getPosition()))
                coins.add(newcoin);
        }
        return coins;
    }

    private List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for(int i=0; i<3; i++){
            Monster newmonster = new Monster(random.nextInt(width-2) + 1,
                    random.nextInt(height-2)+1);
            if(!monsters.contains(newmonster) && !newmonster.getPosition().equals(hero.getPosition()))
                monsters.add(newmonster);
        }
        return monsters;
    }
    private void retrieveCoins(){ // Collecting coins adds one monster and a new coin
        for(Coin coin : coins){
            if(hero.getPosition().equals(coin.getPosition())) {
                coins.remove(coin);
                System.out.println("Coin Collected!");
                score++;
                Random random = new Random();
                for(int i=0; i<2; i++){
                    Monster newmonster = new Monster(random.nextInt(width-2) + 1,
                            random.nextInt(height-2)+1);
                    if(!monsters.contains(newmonster) && !newmonster.getPosition().equals(hero.getPosition()))
                        monsters.add(newmonster);
                }
                Coin newcoin = new Coin(random.nextInt(width-2) + 1,
                        random.nextInt(height-2)+1);
                if(!coins.contains(newcoin) && !newcoin.getPosition().equals(hero.getPosition()))
                    coins.add(newcoin);
                break;
            }
        }
    }
    public void moveMonsters(){
        for(Monster monster : monsters){
            monster.setPosition(monster.move(this));
        }
    }

    public boolean verifyMonsterCollisions(){
        for(Monster monster : monsters){
            if(monster.getPosition().equals(hero.getPosition())){
                System.out.println("Game Over!\nWell played!");
                System.out.println("\nScore: " + score);
                return true;
            }
        }
        return false;
    }
    private class Hero extends Element{
        private Hero(int x, int y) {
            super(x,y);
        }

        public void draw(TextGraphics screen){
            screen.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
            screen.enableModifiers(SGR.BOLD);
            screen.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "ʘ‿ʘ");
        }
    }
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;

    public int getHeight() { return height; }
    public int getWidth() { return width; }
    private int height;
    private int width;
    private int score = 0;
}