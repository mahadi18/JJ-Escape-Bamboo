package com.mygdx.jjescape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by Mahadi on 22-11-16.
 */
public class ScreenPlay extends ScreenAdapter implements Screen{

    SpriteBatch batch;
    BitmapFont font;

    Texture kuetianImage, bg, bambooImage;
    private Sound deadSound;
    private Music bgAudio;
    private OrthographicCamera camera;
    private Rectangle kuetian;
    private Array<Rectangle> bamboos;
    private long lastbambooTime;
    public Integer counter = 0;

    jjescape game;
    Texture img;

   // OrthographicCamera camera;

    public ScreenPlay(final jjescape gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show () {
        batch = new SpriteBatch();

        // loading images
        bambooImage = new Texture(Gdx.files.internal("bash.png"));
        kuetianImage = new Texture(Gdx.files.internal("st.png"));
        bg = new Texture(Gdx.files.internal("kuet.jpg"));

        // loading Sounds
        deadSound = Gdx.audio.newSound(Gdx.files.internal("bang.mp3"));
        bgAudio = Gdx.audio.newMusic(Gdx.files.internal("gun.mp3"));

        // start playing background music

        bgAudio.setLooping(true);
        bgAudio.play();

        // creating the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();

        // Create the default font
        font = new BitmapFont();
        // Scale it up
        font.getData().setScale(5);
        // Set the filter
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        // creating the kuetian

        kuetian = new Rectangle();
        kuetian.x = 800/2 - 124;
        kuetian.y = 0;
        kuetian.width = 124;
        kuetian.height = 121;

        // create the random bamboo arry

        bamboos = new Array<Rectangle>();
        randombamboo();


    }

    private void randombamboo() {

        Rectangle bamboo = new Rectangle();
        bamboo.x = MathUtils.random(0, 800-32);
        bamboo.y = 480;

        bamboo.width = 39;
        bamboo.height = 72;
        bamboos.add(bamboo);
        lastbambooTime = TimeUtils.nanoTime();


    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        batch.setProjectionMatrix(camera.combined);


        batch.begin();
        batch.draw(bg, 0, 0, 800, 480);
        //draw(batch, "Score: ", 50, 400);
        batch.draw(kuetianImage, kuetian.x, kuetian.y);

        for(Rectangle bamboo: bamboos){
            batch.draw(bambooImage,bamboo.x,bamboo.y);
        }
        batch.end();

        if(Gdx.input.isTouched()){
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touchPos);
            kuetian.x = touchPos.x - 124/2;

        }

        if(TimeUtils.nanoTime() - lastbambooTime > 1000000000) randombamboo();


        Iterator<Rectangle> iter = bamboos.iterator();
        while (iter.hasNext()){
            Rectangle bamboo = iter.next();
            bamboo.y -= 200 * Gdx.graphics.getDeltaTime();
            if(bamboo.y + 124 < 0) iter.remove();
            if(bamboo.overlaps(kuetian)){
                //bgAudio.stop();
                deadSound.play();
                iter.remove();

                  
            }
            else{
                counter+=10;
                //font.setColor(Color.MAGENTA);
                //font.draw(batch, "Score: "+ counter, 50, 400);
                //game.font.draw(game.batch, "Score: " + counter, 50, 400);
            }

        }


    }
}
