/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shen_7_barbarianhorde;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author ShadowX
 */
public class Sewers extends BasicGameState {
//    Bolts bolt2;
    static Player playerguy2;
    static Music music2;
    
    private static TiledMap sewerMap;
    private static AppGameContainer app;
    private static Camera camera;
    public static int counter = 0;

    private static final int SIZE = 32;

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 750;
    public Sewers(int xSize, int ySize) {
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gc.setTargetFrameRate(60);
        gc.setShowFPS(false);
        
        playerguy2 = new Player(642,32);
	sewerMap = new TiledMap("res/sewers.tmx");
        music2 = new Music("res/music.ogg");
	camera = new Camera(gc, sewerMap);

	Blocked2.blocked2 = new boolean[sewerMap.getWidth()][sewerMap.getHeight()];
	for (int xAxis = 0; xAxis < sewerMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < sewerMap.getHeight(); yAxis++) {
		int tileID = sewerMap.getTileId(xAxis, yAxis, 1);
		String value = sewerMap.getTileProperty(tileID,"blocked", "false");
		if ("true".equals(value)) {
			Blocked2.blocked2[xAxis][yAxis] = true;
		}
            }
	}
    	
        Trapped.trapped = new boolean[sewerMap.getWidth()][sewerMap.getHeight()];
	for (int xAxis = 0; xAxis < sewerMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < sewerMap.getHeight(); yAxis++) {
		int tileID = sewerMap.getTileId(xAxis, yAxis, 2);
		String value = sewerMap.getTileProperty(tileID,"trapped", "false");
		if ("true".equals(value)) {
			Trapped.trapped[xAxis][yAxis] = true;
		}
            }
	}
        
        music2.loop();
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
	camera.centerOn((int) playerguy2.x, (int) playerguy2.y);
	camera.drawMap();
	camera.translateGraphics();

	playerguy2.sprite.draw((int) playerguy2.x, (int) playerguy2.y);
	g.drawString("Health: " + (int)(playerguy2.health), camera.cameraX + 10, camera.cameraY + 10);
        
        g.drawString("x: " + (int)playerguy2.x + "y: " +(int)playerguy2.y ,playerguy2.x, playerguy2.y - 10);
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        counter += delta;
	Input input = gc.getInput();
	float fdelta = delta * playerguy2.speed;
	playerguy2.setpdelta(fdelta);

	double rightlimit = (sewerMap.getWidth() * SIZE) - (SIZE * 0.75);
	float projectedright = playerguy2.x + fdelta + SIZE;
	boolean cangoright = projectedright < rightlimit;

//	if (input.isKeyDown(Input.KEY_SPACE)) {
//            if (playerguy2.bolts > 0) {
//                if (bolt2.isIsVisible() == false) {
//                    bolt2.setX((int) playerguy2.x);
//                    bolt2.setY((int) playerguy2.y - 10);
//                    bolt2.setIsVisible(true);
//                    bolt2.setTimeExists(35);
//                    playerguy2.bolts -= 1;
//                }
//            }
//            if (playerguy2.sprite == playerguy2.right) {
//                bolt2.xmove = 10;
//                bolt2.ymove = 0;
//            } else if (playerguy2.sprite == playerguy2.left) {
//                bolt2.xmove = -10;
//                bolt2.ymove = 0;
//            } else if (playerguy2.sprite == playerguy2.up) {
//                bolt2.xmove = 0;
//                bolt2.ymove = -10;
//            } else if (playerguy2.sprite == playerguy2.down) {
//                bolt2.xmove = 0;
//                bolt2.ymove = 10;
//            }
//        } else 
        if (input.isKeyDown(Input.KEY_UP)) {
            playerguy2.sprite = playerguy2.up;
            float fdsc = (float) (fdelta - (SIZE * .15));
            if (!(isBlocked(playerguy2.x, playerguy2.y - fdelta) || isBlocked((float) (playerguy2.x + SIZE + 1.5), playerguy2.y - fdelta))) {
                playerguy2.sprite.update(delta);
                playerguy2.y -= fdelta;
            }
	} else if (input.isKeyDown(Input.KEY_DOWN)) {
            playerguy2.sprite = playerguy2.down;
            if (!isBlocked(playerguy2.x, playerguy2.y + SIZE + fdelta) && !isBlocked(playerguy2.x + SIZE - 1, playerguy2.y + SIZE + fdelta)) {
		playerguy2.sprite.update(delta);
		playerguy2.y += fdelta;
            }
	} else if (input.isKeyDown(Input.KEY_LEFT)) {
            playerguy2.sprite = playerguy2.left;
            if (!(isBlocked(playerguy2.x - fdelta, playerguy2.y) || isBlocked(playerguy2.x - fdelta, playerguy2.y + SIZE - 1))) {
		playerguy2.sprite.update(delta);
		playerguy2.x -= fdelta;
            }
	} else if (input.isKeyDown(Input.KEY_RIGHT)) {
            playerguy2.sprite = playerguy2.right;
            if (cangoright && (!(isBlocked(playerguy2.x + SIZE + fdelta, playerguy2.y) || isBlocked(playerguy2.x + SIZE + fdelta, playerguy2.y + SIZE - 1)))) {
		playerguy2.sprite.update(delta);
		playerguy2.x += fdelta;
            }
	}
//        if (bolt2.isIsVisible()) {
//            if (bolt2.getTimeExists() > 0) {
//                bolt2.setX(bolt2.x += bolt2.xmove);
//                bolt2.setY(bolt2.y += bolt2.ymove);
//                bolt2.hitbox.setX(bolt2.getX());
//                bolt2.hitbox.setY(bolt2.getY());
//                bolt2.countdown();
//            } else {
//                bolt2.setIsVisible(false);
//            }
//        }
        
	playerguy2.rect.setLocation(playerguy2.getplayershitboxX(), playerguy2.getplayershitboxY());
                

	playerguy2.time -= counter/1000;
	if(playerguy2.health <= 0){
            sbg.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	}
    }

    public int getID() {
    	return 4;
    }
    
    private boolean isBlocked(float tx, float ty) {
        int xBlock = (int) tx / SIZE;
    	int yBlock = (int) ty / SIZE;
	return Blocked2.blocked2[xBlock][yBlock];
    }
    
    private boolean isTrapped(float tx, float ty) {
        int xTrap = (int) tx / SIZE;
    	int yTrap = (int) ty / SIZE;
	return Trapped.trapped[xTrap][yTrap];
    }
    
    @Override
    public void keyReleased(int key, char c) {
        switch (key) {
            case Input.KEY_R:
                
                break;
            
            default:
                break;
        }
    }
}
//(03:37:46 PM) Crater Cookies:
//}else if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
//			playerguy.sprite = playerguy.up;
//			float fdsc = (float) (fdelta - (SIZE * .15));
//			if (!(isBlocked(playerguy.x, playerguy.y - fdelta) || isBlocked((float) (playerguy.x + SIZE + 1.5), playerguy.y - fdelta))) {
//				playerguy.sprite.update(delta);
//				playerguy.y -= fdelta;
//                                
//			}if (isTrap(playerguy.x, playerguy.y - fdelta) || isTrap((float) (playerguy.x + SIZE + 1.5), playerguy.y - fdelta)) {
//				playerguy.health -= 4;
//                                System.out.println("Ouch" + " X:" + playerguy.x + " Y:" + playerguy.y);
//                            }
//                }
//(03:37:59 PM) Crater Cookies:
//else if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
//			playerguy.sprite = playerguy.down;
//			if (!isBlocked(playerguy.x, playerguy.y + SIZE*2 + fdelta) && !isBlocked(playerguy.x + SIZE - 1, playerguy.y + SIZE*2 + fdelta)) {
//				playerguy.sprite.update(delta);
//				playerguy.y += fdelta;
//                        }if (isTrap(playerguy.x, playerguy.y - fdelta) || isTrap(playerguy.x + SIZE - 1, playerguy.y - fdelta)) {
//				playerguy.health -= 4;
//                                System.out.println("Ouch" + " X:" + playerguy.x + " Y:" + playerguy.y);
//                            }
//		}
//(03:38:08 PM) Crater Cookies:
//else if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
//			playerguy.sprite = playerguy.left;
//                        if (!(isBlocked(playerguy.x - fdelta, playerguy.y) || isBlocked(playerguy.x - fdelta, playerguy.y + SIZE - 1))) {
//				playerguy.sprite.update(delta);
//				playerguy.x -= fdelta;
//                        }if (isTrap(playerguy.x - fdelta, playerguy.y) || isTrap(playerguy.x - fdelta, playerguy.y + SIZE - 1)) {
//				playerguy.health -= 4;
//                                System.out.println("Ouch" + " X:" + playerguy.x + " Y:" + playerguy.y);
//                            }
//		}
//(03:38:16 PM) Crater Cookies:
//else if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
//			playerguy.sprite = playerguy.right;
//                        if (cangoright && (!(isBlocked(playerguy.x + SIZE + fdelta, playerguy.y) || isBlocked(playerguy.x + SIZE + fdelta, playerguy.y + SIZE - 1)))) {
//				playerguy.sprite.update(delta);
//				playerguy.x += fdelta;
//                                
//			}if (isTrap(playerguy.x + SIZE + fdelta, playerguy.y) || isTrap(playerguy.x + SIZE + fdelta, playerguy.y + SIZE - 1)) {
//				playerguy.health -= 4;
//                                System.out.println("Ouch" + " X:" + playerguy.x + " Y:" + playerguy.y);
//                            }
//                } 