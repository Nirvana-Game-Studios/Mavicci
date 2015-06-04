package tk.nirvanagamestudios.mavicci.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import tk.nirvanagamestudios.mavicci.models.TexturedModel;
import tk.nirvanagamestudios.mavicci.renderEngine.DisplayManager;
import tk.nirvanagamestudios.mavicci.terrains.Terrain;

public class Player extends Entity {
  private static final float RUN_SPEED = 20;
  private static final float TURN_SPEED = 160;
  private static final float GRAVITY = -50;
  private static final float JUMP_POWER = 30;

  private float currentSpeed = 0;
  private float currentTurnSpeed = 0;
  private float upwardsSpeed = 0;

  public boolean isInAir = false;
  public boolean isMoving = false;

  public Player(TexturedModel model, Vector3f position, float rotationX,
      float rotationY, float rotationZ, float scale) {
    super(model, position, rotationX, rotationY, rotationZ, scale);
  }
  
  public Player(Entity e){
	  super(e.getModel(),e.getPosition(),e.getRotX(),e.getRotY(),e.getRotZ(),e.getScale());
  }

  public void move(Terrain terrain) {
    checkInputs();
    super.increaseRotation(0,
        currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
    float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
    float dx = (float) (distance * Math
        .sin(Math.toRadians(super.getRotY())));
    float dz = (float) (distance * Math
        .cos(Math.toRadians(super.getRotY())));
    super.increasePosition(dx, 0, dz);
    upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
    super.increasePosition(0,
        upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
    float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
    if (super.getPosition().y < terrainHeight) {
      upwardsSpeed = 0;
      isInAir = false;
      super.getPosition().y = terrainHeight;
    }
  }

  private void jump() {
    if (!isInAir) {
      this.upwardsSpeed = JUMP_POWER;
      isInAir = true;
    }
  }

  private void checkInputs() {
    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
      this.currentSpeed = RUN_SPEED;
      isMoving = true;
    } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
      this.currentSpeed = -RUN_SPEED;
      isMoving = true;
    } else {
      this.currentSpeed = 0;
      isMoving = false;
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
      this.currentTurnSpeed = -TURN_SPEED;
    } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
      this.currentTurnSpeed = TURN_SPEED;
    } else {
      this.currentTurnSpeed = 0;
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
      jump();
    }
  }
  
  public boolean getIsMoving(){
	  return isMoving;
  }
}