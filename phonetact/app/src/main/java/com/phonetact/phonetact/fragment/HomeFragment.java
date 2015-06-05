package com.phonetact.phonetact.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phonetact.R;

/**
 * Created by hp on 30/05/2015.
 */
public class HomeFragment extends Fragment {
    protected static final String ARG_PAGE = "ARG_PAGE";
    LayoutInflater inflate;
    private int mPage;

    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater;
        View view = inflater.inflate(R.layout.fragment_home, container,
                false);
        /*TextView txt = (TextView) view.findViewById(R.id.number);
        txt.setText(""+mPage);*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }
}
