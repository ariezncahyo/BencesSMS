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
 * Use the {@link ComputerSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComputerSetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ComputerSetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComputerSetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComputerSetFragment newInstance(String param1, String param2) {
        ComputerSetFragment fragment = new ComputerSetFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_computer_set, container, false);
        final SharedPreferences pref=getActivity().getSharedPreferences(MyStatic.mypref,MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        final EditText edturl=(EditText) rootView.findViewById(R.id.edturl);
       edturl.setText(pref.getString("urlkomputer",""));
        Button button=(Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("urlkomputer",edturl
                        .getText().toString());
                editor.commit();
                Toast.makeText(getActivity(), "Setting berhasil", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

}
