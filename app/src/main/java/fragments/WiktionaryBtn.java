package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.marmi.cardschool.R;

import java.lang.reflect.Field;


public class WiktionaryBtn extends Fragment {

    private NestedListener nl;
    private final View.OnClickListener wiktionaryListener = new View.OnClickListener() {
        public void onClick(View v) {
            nl.nestedListenerClicked("Wiki");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wiktionary, container, false);
        ImageView wiktionary = v.findViewById(R.id.wiktionary);
        wiktionary.setOnClickListener(wiktionaryListener);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nl = (NestedListener) getParentFragment();

    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public interface NestedListener {
        void nestedListenerClicked(String mode);
    }

}