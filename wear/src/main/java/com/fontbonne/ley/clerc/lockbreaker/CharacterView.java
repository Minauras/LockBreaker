package com.fontbonne.ley.clerc.lockbreaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class CharacterView extends View {

    private Context mContext;
    private Bitmap eyes;
    private Bitmap nose;
    private Bitmap backhair;
    private Bitmap eyebrows;
    private Bitmap hair;
    private Bitmap head;
    private Bitmap mouth;
    private Bitmap pants;
    private Bitmap shoes;
    private Bitmap skin;
    private Bitmap tshirt;

    private float headLeft, headTop, headRight, headBottom;

    private float xPos;
    private float yPos;

    private float scale;
    private String name;

    private int count;

    boolean isWaldo;
    boolean genre;
    private int eyesIdx, noseIdx, eyebrowsIdx, hairIdx, headSkinIdx, mouthIdx, pantsIdx, shoesIdx, tshirtIdx;


    public CharacterView(Context context, float x, float y, float scale, String name, boolean isWaldo){
        super(context);
        mContext = context;
        this.isWaldo = isWaldo;
        count = 0;
        this.name = name;
        this.scale = scale;
        this.xPos = x;
        this.yPos = y;


        Random rnd = new Random(name.hashCode());

        genre = rnd.nextBoolean();
        //Log.d("AZERTY", String.valueOf(genre));


        noseIdx = rnd.nextInt(2);
        //Log.d("AZERTY noseIdx", String.valueOf(noseIdx));
        eyebrowsIdx = rnd.nextInt(2);

        //Log.d("AZERTY eyebrowsIdx", String.valueOf(eyebrowsIdx));

        headSkinIdx = rnd.nextInt(3);
        //Log.d("AZERTY headSkinIdx", String.valueOf(headSkinIdx));

        mouthIdx = rnd.nextInt(5);
        //Log.d("AZERTY mouthIdx", String.valueOf(mouthIdx));

        pantsIdx = rnd.nextInt(12);
        //Log.d("AZERTY pantsIdx", String.valueOf(pantsIdx));

        shoesIdx = rnd.nextInt(4);
        //Log.d("AZERTY shoesIdx", String.valueOf(shoesIdx));

        tshirtIdx = rnd.nextInt(12);
        //Log.d("AZERTY tshirtIdx", String.valueOf(tshirtIdx));

        if (genre){
            eyesIdx = rnd.nextInt(3);
            //Log.d("AZERTY eyesIdx", String.valueOf(eyesIdx));

            hairIdx = rnd.nextInt(6);
            //Log.d("AZERTY hairIdx", String.valueOf(hairIdx));

        }else{
            eyesIdx = rnd.nextInt(3)+3;
            //Log.d("AZERTY eyesIdx", String.valueOf(eyesIdx));

            hairIdx = rnd.nextInt(12)+6;
            //Log.d("AZERTY hairIdx", String.valueOf(hairIdx));

        }

        switch (noseIdx){
            case 0:
                nose = BitmapFactory.decodeResource(getResources(), R.mipmap.nose0);
                break;
            case 1:
                nose = BitmapFactory.decodeResource(getResources(), R.mipmap.nose1);
                break;
            default:
                nose = BitmapFactory.decodeResource(getResources(), R.mipmap.nose1);
                break;
        }
        switch (eyesIdx){
            case 0:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes0);
                break;
            case 1:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes1);
                break;
            case 2:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes2);
                break;
            case 3:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes3);
                break;
            case 4:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes4);
                break;
            case 5:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes5);
                break;
            default:
                eyes = BitmapFactory.decodeResource(getResources(), R.mipmap.eyes5);
                break;
        }
        switch (eyebrowsIdx){
            case 0:
                eyebrows = BitmapFactory.decodeResource(getResources(), R.mipmap.eyebrows0);
                break;
            case 1:
                eyebrows = BitmapFactory.decodeResource(getResources(), R.mipmap.eyebrows1);
                break;
            default:
                eyebrows = BitmapFactory.decodeResource(getResources(), R.mipmap.eyebrows1);
                break;
        }

        switch (headSkinIdx){
            case 0:
                head = BitmapFactory.decodeResource(getResources(), R.mipmap.head0);
                skin = BitmapFactory.decodeResource(getResources(), R.mipmap.skin0);
                break;
            case 1:
                head = BitmapFactory.decodeResource(getResources(), R.mipmap.head1);
                skin = BitmapFactory.decodeResource(getResources(), R.mipmap.skin1);
                break;
            case 2:
                head = BitmapFactory.decodeResource(getResources(), R.mipmap.head2);
                skin = BitmapFactory.decodeResource(getResources(), R.mipmap.skin2);
                break;
            default:
                head = BitmapFactory.decodeResource(getResources(), R.mipmap.head1);
                skin = BitmapFactory.decodeResource(getResources(), R.mipmap.skin1);
                break;
        }

        switch (mouthIdx){
            case 0:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth0);
                break;
            case 1:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth1);
                break;
            case 2:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth2);
                break;
            case 3:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth3);
                break;
            case 4:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth4);
                break;
            default:
                mouth = BitmapFactory.decodeResource(getResources(), R.mipmap.mouth2);
                break;
        }

        switch (shoesIdx){
            case 0:
                shoes = BitmapFactory.decodeResource(getResources(), R.mipmap.shoes0);
                break;
            case 1:
                shoes = BitmapFactory.decodeResource(getResources(), R.mipmap.shoes1);
                break;
            case 2:
                shoes = BitmapFactory.decodeResource(getResources(), R.mipmap.shoes2);
                break;
            case 3:
                shoes = BitmapFactory.decodeResource(getResources(), R.mipmap.shoes3);
                break;
            default:
                shoes = BitmapFactory.decodeResource(getResources(), R.mipmap.shoes1);
                break;
        }


        switch (pantsIdx){
            case 0:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants0);
                break;
            case 1:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants1);
                break;
            case 2:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants2);
                break;
            case 3:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants3);
                break;
            case 4:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants4);
                break;
            case 5:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants5);
                break;
            case 6:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants6);
                break;
            case 7:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants7);
                break;
            case 8:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants8);
                break;
            case 9:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants9);
                break;
            case 10:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants10);
                break;
            case 11:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants11);
                break;
            default:
                pants = BitmapFactory.decodeResource(getResources(), R.mipmap.pants11);
                break;
        }

        switch (tshirtIdx){
            case 0:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt0);
                break;
            case 1:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt1);
                break;
            case 2:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt2);
                break;
            case 3:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt3);
                break;
            case 4:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt4);
                break;
            case 5:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt5);
                break;
            case 6:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt6);
                break;
            case 7:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt7);
                break;
            case 8:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt8);
                break;
            case 9:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt9);
                break;
            case 10:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt10);
                break;
            case 11:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt11);
                break;
            default:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt0);
                break;
        }

        switch (hairIdx){
            case 0:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair0);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair0);

                break;
            case 1:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair1);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair1);

                break;
            case 2:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair2);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair2);

                break;
            case 3:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair3);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair5);

                break;
            case 4:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair4);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair4);

                break;
            case 5:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair5);
                backhair = BitmapFactory.decodeResource(getResources(), R.mipmap.backhair3);

                break;
            case 6:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair6);
                break;
            case 7:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair7);
                break;
            case 8:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair8);
                break;
            case 9:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair9);
                break;
            case 10:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair10);
                break;
            case 11:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair11);
                break;
            case 12:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair12);
                break;
            case 13:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair13);
                break;
            case 14:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair14);
                break;
            case 15:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair15);
                break;
            case 16:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair16);
                break;
            case 17:
                hair = BitmapFactory.decodeResource(getResources(), R.mipmap.hair17);
                break;
            default:
                tshirt = BitmapFactory.decodeResource(getResources(), R.mipmap.tshirt0);
                break;
        }




    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //canvas.drawColor(getResources().getColor(R.color.colorPrimaryDark));

        if (genre){
            float[] backhairPos = {19.426F*scale, 9.189F*scale, 123.195F*scale, 100.865F*scale};
            RectF backhairRect =  new RectF(xPos+backhairPos[0], yPos+backhairPos[1], xPos+backhairPos[2], yPos+backhairPos[3]);
            canvas.drawBitmap(backhair, null, backhairRect, null);
        }


        float[] skinPos = {1.097F*scale,171.784F*scale, 141.22F*scale, 307.348F*scale};
        RectF skinRect =  new RectF(xPos+skinPos[0], yPos+skinPos[1], xPos+skinPos[2], yPos+skinPos[3]);
        canvas.drawBitmap(skin, null, skinRect, null);

        float[] pantsPos = {24.949F*scale,182.684F*scale, 123.195F*scale, 283.828F*scale};
        RectF pantsRect =  new RectF(xPos+pantsPos[0], yPos+pantsPos[1], xPos+pantsPos[2], yPos+pantsPos[3]);
        canvas.drawBitmap(pants, null, pantsRect, null);

        float[] tshirtPos = {0F*scale,92.121F*scale, 142.585F*scale, 201.802F*scale};
        RectF tshirtRect =  new RectF(xPos+tshirtPos[0], yPos+tshirtPos[1], xPos+tshirtPos[2], yPos+tshirtPos[3]);
        canvas.drawBitmap(tshirt, null, tshirtRect, null);

        float[] headPos = {23.159F*scale, 2.71F*scale, 123.159F*scale, 105.905F*scale};
        headLeft = xPos+headPos[0];
        headTop = yPos+headPos[1];
        headRight = xPos+headPos[2];
        headBottom = yPos+headPos[3];
        RectF headRect =  new RectF(xPos+headPos[0], yPos+headPos[1], xPos+headPos[2], yPos+headPos[3]);
        canvas.drawBitmap(head, null, headRect, null);

        if (hairIdx < 6 ){
            float[] hairPos = {19.426F*scale,0F*scale, 123.159F*scale, 93.246F*scale};
            RectF hairRect =  new RectF(xPos+hairPos[0], yPos+hairPos[1], xPos+hairPos[2], yPos+hairPos[3]);
            canvas.drawBitmap(hair, null, hairRect, null);
        }else if (hairIdx < 12){
            float[] hairPos= {27.157F*scale,-31.801F*scale, 131.306F*scale, 67.628F*scale};
            RectF hairRect =  new RectF(xPos+hairPos[0], yPos+hairPos[1], xPos+hairPos[2], yPos+hairPos[3]);
            canvas.drawBitmap(hair, null, hairRect, null);
        }else{
            float[] hairPos = {27.157F*scale,-4.139F*scale, 117.394F*scale, 64.492F*scale};
            RectF hairRect =  new RectF(xPos+hairPos[0], yPos+hairPos[1], xPos+hairPos[2], yPos+hairPos[3]);
            canvas.drawBitmap(hair, null, hairRect, null);
        }
        //float[] hairPunkPos = {27.157F*scale,-31.801F*scale, 131.306F*scale, 67.628F*scale};
        //float[] hairFlatPos = {27.157F*scale,-4.139F*scale, 117.394F*scale, 64.492F*scale};


        float[] eyesPos = {37.782F*scale,41.73F*scale, 95.042F*scale, 60.912F*scale};
        //float[] eyesMPos = {37.395F*scale,45.841F*scale, 94.389F*scale, 60.885F*scale};
        RectF eyesRect =  new RectF(xPos+eyesPos[0], yPos+eyesPos[1], xPos+eyesPos[2], yPos+eyesPos[3]);
        canvas.drawBitmap(eyes, null, eyesRect, null);

        if (noseIdx == 0){
            float[] nosePos = {62.654F*scale,61.885F*scale, 68.573F*scale, 67.064F*scale};
            RectF noseRect =  new RectF(xPos+nosePos[0], yPos+nosePos[1], xPos+nosePos[2], yPos+nosePos[3]);
            canvas.drawBitmap(nose, null, noseRect, null);
        }else{
            float[] nosePos =  {60.218F*scale,59.164F*scale, 71.009F*scale, 69.166F*scale};
            RectF noseRect =  new RectF(xPos+nosePos[0], yPos+nosePos[1], xPos+nosePos[2], yPos+nosePos[3]);
            canvas.drawBitmap(nose, null, noseRect, null);
        }

        if (mouthIdx == 0) {
            float[] mouthPos0 = {55.099F * scale, 69.913F * scale, 77.716F * scale, 80.405F * scale};
            RectF mouthRect = new RectF(xPos + mouthPos0[0], yPos + mouthPos0[1], xPos + mouthPos0[2], yPos + mouthPos0[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }else if (mouthIdx == 1) {
            float[] mouthPos = {57.453F * scale, 74.244F * scale, 77.045F * scale, 80.703F * scale};
            RectF mouthRect = new RectF(xPos + mouthPos[0], yPos + mouthPos[1], xPos + mouthPos[2], yPos + mouthPos[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }else if (mouthIdx == 2) {
            float[] mouthPos = {52.45F * scale, 72.871F * scale, 83.332F * scale, 77.446F * scale};
            RectF mouthRect = new RectF(xPos + mouthPos[0], yPos + mouthPos[1], xPos + mouthPos[2], yPos + mouthPos[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }else if (mouthIdx == 3) {
            float[] mouthPos = {61.22F*scale,69.913F*scale, 74.562F*scale, 83.255F*scale};
            RectF mouthRect =  new RectF(xPos+mouthPos[0], yPos+mouthPos[1], xPos+mouthPos[2], yPos+mouthPos[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }else if (mouthIdx == 4) {
            float[] mouthPos = {57.395F*scale,73.243F*scale, 78.387F*scale, 79.702F*scale};
            RectF mouthRect =  new RectF(xPos+mouthPos[0], yPos+mouthPos[1], xPos+mouthPos[2], yPos+mouthPos[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }else {
            float[] mouthPos = {55.099F*scale,69.913F*scale, 77.716F*scale, 80.405F*scale};
            RectF mouthRect =  new RectF(xPos+mouthPos[0], yPos+mouthPos[1], xPos+mouthPos[2], yPos+mouthPos[3]);
            canvas.drawBitmap(mouth, null, mouthRect, null);
        }
        //float[] mouth0Pos = {55.099F*scale,69.913F*scale, 77.716F*scale, 80.405F*scale};
        //float[] mouth1Pos = {57.453F*scale,74.244F*scale, 77.045F*scale, 80.703F*scale};
        //float[] mouth2Pos = {52.45F*scale,72.871F*scale, 83.332F*scale, 77.446F*scale};
        //float[] mouth3Pos = {61.22F*scale,69.913F*scale, 74.562F*scale, 83.255F*scale};
        //float[] mouth4Pos = {57.395F*scale,73.243F*scale, 78.387F*scale, 79.702F*scale};




        float[] shoesPos = {15.003F*scale,290.383F*scale, 127.244F*scale, 315.796F*scale};
        RectF shoesRect =  new RectF(xPos+shoesPos[0], yPos+shoesPos[1], xPos+shoesPos[2], yPos+shoesPos[3]);
        canvas.drawBitmap(shoes, null, shoesRect, null);


        float[] eyebrowPos = {31.784F*scale,36.875F*scale, 101.032F*scale, 48.104F*scale};
        RectF eyebrowsRect =  new RectF(xPos+eyebrowPos[0], yPos+eyebrowPos[1], xPos+eyebrowPos[2], yPos+eyebrowPos[3]);
        canvas.drawBitmap(eyebrows, null, eyebrowsRect, null);


        //canvas.drawBitmap(backhair, 0, 0, null);
        //canvas.drawBitmap(mouth, 0, 0, null);
        //canvas.drawBitmap(eyes, 0, 0, null);
        //canvas.drawBitmap(hair, 0, 0, null);
        //canvas.drawBitmap(eyebrows, 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean out = super.onTouchEvent(event);
        //mGestureDetector.onTouchEvent(event);
        float x  = event.getRawX();
        float y  = event.getRawY();
        count += 1;
        int action = event.getActionMasked();
        Log.d("AZERTY", String.valueOf(action));
        if (action == 0 && x > headLeft && x < headRight && y > headTop && y < headBottom){
            if (isWaldo){
                Toast.makeText(mContext, "WIN", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "LOSE", Toast.LENGTH_SHORT).show();
            }
        }
        return out;
    }
}

