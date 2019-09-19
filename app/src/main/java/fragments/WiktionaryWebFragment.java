package fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.marmi.cardschool.R;

public class WiktionaryWebFragment extends Fragment {
    ProgressDialog pd;
    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview;
    private ProgressBar spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_wiktionary, container, false);
//super.onCreate(savedInstanceState);
        webview = v.findViewById(R.id.webview);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar1);


        String word = this.getArguments().getString("message");
        System.out.println("ESTEILA " + word);

        webview.setWebViewClient(new CustomWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://de.wiktionary.org/wiki/" + word);


        return v;
    }

    public Boolean onBack() {
        if (webview.canGoBack()) {
            webview.goBack();
            System.out.println("wiki back page");
            return true;
        } else {
            System.out.println("wiki exit ");
            return false;
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";
            spinner.setVisibility(View.GONE);

            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view, url);

        }

    }



}
