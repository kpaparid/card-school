package fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import test.ItemData;


public class FragmentLanguage extends Fragment {
    LayoutInflater inflator;
    private FragmentLanguageListener listener;
    private View v;
    private ArrayList<ItemData> list;
    private NewAdapter adapter;
    private ImageView img;
    private final AdapterView.OnItemSelectedListener adapterListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String targetLang = "";
            switch (i) {
                case 0: {
                    targetLang = "en";
                    img.setImageResource(R.drawable.en);
                    break;
                }
                case 1: {
                    targetLang = "el";
                    img.setImageResource(R.drawable.gr);
                    break;
                }
                case 2: {
                    targetLang = "sr";
                    img.setImageResource(R.drawable.se);
                    break;
                }
                case 3: {
                    targetLang = "hr";
                    img.setImageResource(R.drawable.hr);
                    break;
                }
                case 4: {
                    targetLang = "Error";
                    img.setImageResource(R.drawable.en);
                    break;
                }
            }

            listener.onInputLanguage(targetLang);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        inflator = inflater;
        v = inflater.inflate(R.layout.spinner, container, false);
        img = v.findViewById(R.id.frag_img);
        new Init().execute();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof FragmentLanguageListener) {
            listener = (FragmentLanguageListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentLanguageListener");
        }
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

    public interface FragmentLanguageListener {
        void onInputLanguage(CharSequence input);
    }

    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size() - 1;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.spinner, null);
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt;
            ImageView image;
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.flag, null);

            }
            txt = convertView.findViewById(R.id.txt);
            image = convertView.findViewById(R.id.flag_img);
            txt.setText(list.get(position).text);
            image.setImageResource(list.get(position).getImageId());
            return convertView;
        }
    }

    public class Init extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            list = new ArrayList<>();
            list.add(new ItemData("English", R.drawable.en));
            list.add(new ItemData("Greek", R.drawable.gr));
            list.add(new ItemData("Serbish", R.drawable.se));
            list.add(new ItemData("Croatian", R.drawable.hr));
            list.add(new ItemData("dummy", R.drawable.gr));
            adapter = new NewAdapter();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Spinner sp = v.findViewById(R.id.spinner2);
            sp.setAdapter(adapter);
            sp.setSelection(list.size() - 1);
            sp.setOnItemSelectedListener(adapterListener);
        }
    }

}