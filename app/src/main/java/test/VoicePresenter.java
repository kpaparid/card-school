package test;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.example.marmi.cardschool.data.WordController;

import java.util.Locale;

public class VoicePresenter {

    protected VoiceView cV;
    public WordController wordController;
    public String target = "en";
    private String mode;
    public String from;
    public String to;
    public Context context;
    public TextToSpeech t1;
    public TextToSpeech t2;
    boolean play = false;
    private long delayMillis;


    public VoicePresenter(Context context, VoiceView cV, WordController wc) {

        this.context = context;
        this.cV = cV;
        if (cV.getArguments() != null) {
            from = cV.getArguments().getString("nfrom");
            to = cV.getArguments().getString("nto");
            mode =cV.getArguments().getString("mode");
            wordController = (WordController) cV.getArguments().getSerializable("wc");
        }
        System.out.println(wordController.getFullWordText());
        t2=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.GERMAN);
                }
            }
        });



    }



    public void backClick() {

        wordController.moveToPrevious();
        cV.origUI(wordController);
        cV.Orig = true;
    }


    public String getTarget(){
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }


    public void textClick() {
        delayMillis = 3000;
        if(!cV.Orig){

            System.out.println("orig");
            wordController.moveToNext();
            cV.origUI(wordController);

            if(play == true){
                t1.speak(wordController.getArticle()+" "+wordController.getWordText(), TextToSpeech.QUEUE_FLUSH, null);
            }
            cV.Orig = true;

        }else {
            delayMillis +=3000;
            System.out.println("trans");
            cV.transUI(wordController,target);
            if(play == true){
                t2.speak(wordController.getEn_translated(), TextToSpeech.QUEUE_FLUSH, null);
            }
            cV.Orig = false;
        }
        if(play == true){


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    textClick();
                }
            }, delayMillis);   //5 seconds
        }


    }

    public WordController getWordController() {
        return wordController;
    }

    public void destroy() {
        t1.stop();
        t2.stop();
        play = false;

    }
}
