package com.gultendogan.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX=0;
	float birdY=0;
	int gameState=0;
	float velocity=0;
	float gravity=0.3f;
	float enemyVelocity = 3;
	Random random;
	int i;
	int score = 0;
	int highScore=0;
	int maxScore=0;
	int scoredEnemy = 0;
	BitmapFont font;
	BitmapFont font2;
	BitmapFont font3;
	BitmapFont font4;

	Circle birdCircle;
	int numberOfEnemies = 4;
	float[] enemyX = new float[numberOfEnemies];

	float[] enemyOffSet = new float[numberOfEnemies];
	float[] enemyOffSet2 = new float[numberOfEnemies];
	float[] enemyOffSet3 = new float[numberOfEnemies];
	float distance=0;

	Circle[] enemyCircle;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;
	

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth()/2;
		random = new Random();

		birdX=Gdx.graphics.getWidth()/2-Gdx.graphics.getHeight()/2;
		birdY=Gdx.graphics.getHeight()/2;

		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircle = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.RED);
		font2.getData().setScale(8);

		font3 = new BitmapFont();
		font3.setColor(Color.WHITE);
		font3.getData().setScale(5);

		font4 = new BitmapFont();
		font4.setColor(Color.WHITE);
		font4.getData().setScale(5);



		for(i=0; i<numberOfEnemies; i++){
			enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

			enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth()/2 + i*distance;

			enemyCircle[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		//yerçekimi
		if(gameState == 1){

			if(enemyX[scoredEnemy] < Gdx.graphics.getWidth()/2-Gdx.graphics.getHeight()/2){
				score++;
				maxScore=score;
				if(maxScore >= highScore){
					highScore=maxScore;
				}

				if(scoredEnemy < numberOfEnemies-1){
					scoredEnemy++;
				}else{
					scoredEnemy = 0;
				}

			}

			//uçma
			if(Gdx.input.justTouched()){
				velocity=-7;
			}

			for(i=0; i<numberOfEnemies; i++){
				if(enemyX[i] < Gdx.graphics.getWidth()/15){
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

				}else{
					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/11);
				batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/11);
				batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/11);

				enemyCircle[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);


			}



			if(birdY >0 ){
				velocity=velocity+gravity;
				birdY=birdY-velocity;
			}else{
				gameState=2;
			}

		}else if(gameState == 0){
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState == 2){

			font2.draw(batch,"Game Over!",750,800);
			font3.draw(batch,"tap the screen to play again.",610,600);
			font4.draw(batch,String.valueOf("High Score:"+highScore),850,450);

			if(Gdx.input.justTouched()){
				gameState=1;
				birdY=Gdx.graphics.getHeight()/2;

				for(i=0; i<numberOfEnemies; i++){
					enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

					enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth()/2 + i*distance;

					enemyCircle[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();
				}

				velocity = 0;
				scoredEnemy=0;
				score=0;

			}
		}


		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/11);
		font.draw(batch,String.valueOf("Score:"+score),100,200);
		batch.end();

		birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(i=0; i<numberOfEnemies; i++){
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30, Gdx.graphics.getHeight()/2+enemyOffSet3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

			if(Intersector.overlaps(birdCircle,enemyCircle[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i])){
				gameState=2;
			}
		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
	}


}
