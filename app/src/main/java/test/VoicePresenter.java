package test;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.example.marmi.cardschool.data.WordController;

import java.util.Locale;

public class VoicePresenter {

    public WordController wordController;
    public String target = "en";
    public String from;
    public String to;
    public Context context;
    public TextToSpeech t1;
    public TextToSpeech t2;
    protected VoiceView cV;
    boolean play = false;


    public VoicePresenter(Context context, VoiceView cV) {

        this.context = context;
        this.cV = cV;
        if (cV.getArguments() != null) {
            from = cV.getArguments().getString("nfrom");
            to = cV.getArguments().getString("nto");
            wordController = (WordController) cV.getArguments().getSerializable("wc");
        }
        t2 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.ENGLISH);
                    t2.setSpeechRate((float) (0.6));
                }
            }
        });
        t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
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


    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
        this.t2 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale language = null;
                    switch (target) {
                        case "en": {
                            language = Locale.ENGLISH;
                            t2.setSpeechRate((float) (0.6));
                            break;
                        }
                        case "el": {
                            language = new Locale("el");
                            t2.setSpeechRate((float) (1));
                            break;
                        }
                        case "hr": {
                            language = new Locale("sr");
                            t2.setSpeechRate((float) (1));
                            break;
                        }
                        case "sr": {
                            language = new Locale("sr");
                            t2.setSpeechRate((float) (1));
                        }
                    }
                    t2.setLanguage(language);
                }
            }
        });
    }

    public void textClick() {
        long delayMillis = 3000;
        if (!cV.Orig) {
            wordController.moveToNext();
            cV.origUI(wordController);
            if (play) {
                t1.speak(wordController.getArticle() + " " + wordController.getWordText(), TextToSpeech.QUEUE_FLUSH, null);
            }
            cV.Orig = true;

        } else {
            delayMillis += 3000;
            cV.transUI(wordController, target);
            if (play) {
                t2.speak(wordController.getTranslated(target), TextToSpeech.QUEUE_FLUSH, null);
            }
            cV.Orig = false;
        }
        if (play) {
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
