package com.ducky.duckythewizard.model;

import com.ducky.duckythewizard.controller.CollisionHandler;
import com.ducky.duckythewizard.model.config.GameConfig;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Objects;

public class AnimatedSprite extends Sprite
{
    public Image[] frames;
    public double duration;
//    private double positionX;
//    private double positionY;
//    private double velocityX;
//    private double velocityY;
//    private double width;
//    private double height;

    public enum MovementState {
        IDLE,
        WALK,
        FLY
    }
    private String skin;
    private Player player;
    private AnimatedSprite.MovementState state;
    private CollisionHandler collisionHandler;

    private Image[] imageArrayFlyRight;
    private Image[] imageArrayFlyLeft;
    private Image[] imageArrayIdleLookRight;
    private Image[] imageArrayIdleLookLeft;
    private Image[] imageArrayWalkRight;
    private Image[] imageArrayWalkLeft;

    private boolean spriteLooksLeft = false;

    public AnimatedSprite(CollisionHandler collisionHandler, String skin, Player player) {
        this.skin = skin;
        this.player = player;
        this.player.resetPlayerTimer();
        this.state = AnimatedSprite.MovementState.FLY;
        this.collisionHandler = collisionHandler;
        this.initializeImageArrays();
        this.frames = imageArrayFlyRight;
    }
    public Image getFrame(double time)
    {
//        System.out.println("time: " + time);
//        System.out.println("frames.length: " + frames.length);
//        System.out.println("duration: " + duration);
//        System.out.println((int)((time % (frames.length * duration)) / duration));
        int index = (int)((time % (frames.length * duration)) / duration);
        return frames[index];
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

//    public double getVelocityX() {
//        return velocityX;
//    }
//
//    public double getVelocityY() {
//        return velocityY;
//    }

//    public void setPosition(double x, double y)
//    {
//        positionX = x;
//        positionY = y;
//    }

//    public void setVelocity(double x, double y)
//    {
//        velocityX = x;
//        velocityY = y;
//    }

//    public void addVelocity(double x, double y)
//    {
//        velocityX += x;
//        velocityY += y;
//        //System.out.println("addVelocity, x: " + velocityX);
//    }

//    public void update(double time)
//    {
//        positionX += velocityX * time;
//        positionY += velocityY * time;
//    }

    @Override
    public void update(double time)
    {
        this.player.reducePlayerTimer();
        // if collision --> revert position and adjust velocity, depending on direction in which collision occurred
        positionX += velocityX * time;
        if(collisionHandler.isCollision(this.getBoundary())){
            positionX -= velocityX * time;  // revert position
            velocityX = velocityX * (-1);   // invert velocity
        }
        positionY += velocityY * time;

        if(collisionHandler.isCollision(this.getBoundary())){
            positionY -= velocityY * time;  // revert position
            if (velocityY > 0){             // if Ducky was falling, stop falling
                velocityY = 0;
            }
            else {
                velocityY = 100;            // if Ducky was flying upwards, invert velocity
            }
        }

        // set animation frames according to movement
        // WALK
        if (velocityY == 0 && velocityX != 0) {
            if (velocityX > 0) {
                this.spriteLooksLeft = false;
                this.frames = imageArrayWalkRight;
            } else if (velocityX < 0) {
                this.spriteLooksLeft = true;
                this.frames = imageArrayWalkLeft;
            }
        }
        // IDLE
        else if (velocityX == 0 && velocityY == 0) {
            if (this.spriteLooksLeft) {
                this.frames = imageArrayIdleLookLeft;
            } else {
                this.frames = imageArrayIdleLookRight;
            }
        }
        // FLY
        else {
            if (velocityX > 0) {
                this.spriteLooksLeft = false;
                this.frames = imageArrayFlyRight;
            } else if (velocityX < 0){
                this.spriteLooksLeft = true;
                this.frames = imageArrayFlyLeft;
            } else {
                // ducky is just falling
                if (this.spriteLooksLeft) {
                    this.frames = imageArrayFlyLeft;
                } else {
                    this.frames = imageArrayFlyRight;
                }
            }
        }
    }

    public void setState(AnimatedSprite.MovementState state) {
        this.state = state;
    }

    public int rescaleImgWidth(Image image) {
        int imgWidth = (int) image.getWidth();
        double calculation = imgWidth * GameConfig.SPRITE_SCALE_FACTOR;
        //TO DO Scalefactor should come from game configuration
        imgWidth = (int) calculation;

        return imgWidth;
    }

    public int rescaleImgHeight(Image image) {
        int imgHeight = (int) image.getHeight();
        double calculation = imgHeight * GameConfig.SPRITE_SCALE_FACTOR;
        //TO DO Scalefactor should come from game configuration
        imgHeight = (int) calculation;

        return imgHeight;
    }

    private void initializeImageArrays() {
        imageArrayFlyRight = new Image[6];
        imageArrayFlyLeft = new Image[6];

        imageArrayIdleLookRight = new Image[4];
        imageArrayIdleLookLeft = new Image[4];

        imageArrayWalkRight = new Image[6];
        imageArrayWalkLeft = new Image[6];

        // FLY
        for (int i = 0; i < imageArrayFlyRight.length; i++) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/ducky/duckythewizard/images/" + this.skin + "/fly/fly_" + i + ".png")));

            imageArrayFlyRight[i] = scaleImage(image, rescaleImgWidth(image), rescaleImgHeight(image), true, false);
        }
        for (int i = 0; i < imageArrayFlyRight.length; i++) {
            imageArrayFlyLeft[i] = scaleImage(imageArrayFlyRight[i], imageArrayFlyRight[i].getWidth(), imageArrayFlyRight[i].getHeight(), true, true);
        }

        // IDLE
        for (int i = 0; i < imageArrayIdleLookRight.length; i++) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/ducky/duckythewizard/images/" + this.skin + "/idle/idle_" + i + ".png")));
            imageArrayIdleLookRight[i] = scaleImage(image, rescaleImgWidth(image), rescaleImgHeight(image), true, false);
        }
        for (int i = 0; i < imageArrayIdleLookRight.length; i++) {
            imageArrayIdleLookLeft[i] = scaleImage(imageArrayIdleLookRight[i], imageArrayIdleLookRight[i].getWidth(), imageArrayIdleLookRight[i].getHeight(), true, true);
        }

        // WALK
        for (int i = 0; i < imageArrayWalkRight.length; i++) {
            Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/com/ducky/duckythewizard/images/" + this.skin + "/walk/walk_" + i + ".png")));
            imageArrayWalkRight[i] = scaleImage(image, rescaleImgWidth(image), rescaleImgHeight(image), true, false);
        }
        for (int i = 0; i < imageArrayWalkRight.length; i++) {
            imageArrayWalkLeft[i] = scaleImage(imageArrayWalkRight[i], imageArrayWalkRight[i].getWidth(), imageArrayWalkRight[i].getHeight(), true, true);
        }
    }

    private Image scaleImage(Image source, double targetWidth, double targetHeight, boolean preserveRatio, boolean mirror) {
        int width = (int) targetWidth;
        int height = (int) targetHeight;

        return scaleImage(source, width, height, preserveRatio, mirror);
    }

    private Image scaleImage(Image source, int targetWidth, int targetHeight, boolean preserveRatio, boolean mirror) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        if (mirror) {
            imageView.setScaleX(-1);
        }
        return imageView.snapshot(parameters, null);
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,frames[0].getWidth(),frames[0].getHeight());
    }

    public boolean intersects(int row, int column, double cellHeight, double cellWidth) {
        double xPosition = column * cellWidth;
        double yPosition = row * cellHeight;
        Rectangle2D imageBoundary = new Rectangle2D(xPosition,yPosition,cellWidth,cellHeight);

        //System.out.println("  X: " + i.getX() + ", Y: " + i.getY() + ", Width: " + i.getFitWidth() + ", Height: " + i.getFitHeight());
        return imageBoundary.intersects(this.getBoundary());
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
}
