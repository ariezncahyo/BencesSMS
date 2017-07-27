package com.anashidayat.kirimsms;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AndroidSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AndroidSetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AndroidSetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AndroidSetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AndroidSetFragment newInstance(String param1, String param2) {
        AndroidSetFragment fragment = new AndroidSetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_android_set, container, false);
        final SharedPreferences pref=getActivity().getSharedPreferences(MyStatic.mypref,MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        final EditText edtport=(EditText) rootView.findViewById(R.id.edtport);
        final EditText edturl=(EditText) rootView.findViewById(R.id.edturl);
        edtport.setText(pref.getString("portandroid",""));
        edturl.setText(pref.getString("urlsendreport",""));
        Button button=(Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("portandroid",edtport.getText().toString());
                editor.putString("urlsendreport",edturl.getText().toString());
                editor.commit();
                Toast.makeText(getActivity(), "Setting berhasil", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

}
