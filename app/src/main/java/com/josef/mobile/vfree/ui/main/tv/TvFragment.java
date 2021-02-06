package com.josef.mobile.vfree.ui.main.tv;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.base.BaseFragment;
import com.josef.mobile.vfree.ui.main.tv.adapters.Tv_Adapter;

import org.w3c.dom.Text;

public class TvFragment extends BaseFragment {

//https://github.com/gifffert/ExpandableTextViewProject
    ///https://www.youtube.com/watch?v=gkrZ_xOYQJ4
    TextView vision_article;
    Button vision_expander;

    Boolean expandable = true;
    Boolean expand = true;

    Boolean expandable2 = true;
    Boolean expand2 = true;

    TextView business_arrticle;
    Button business_expander;

    String headerTech = "Joseph is an art project consisting of independently designed digital sculptures";



    String business = "Joseph is a business idea that wants to bring digital art to the smart TV. It consists of a mobile and a smart TV application. In the mobile version, you can select works of art and then sync them with your account. Download JosephTv and stream high quality art on your smart TV. Click on the link below to be referred directly to the app store. Joseph is intended for a wide range of users from different industries, since art has space in every room. Joseph becomes an adaptable design element. The constant succession of new sculptures and the associated speed create a pleasant atmosphere, ideal for bars, lobbies, hotels, but also interesting for private use.\n" +
            "Joseph shows art in the digital space and would like to connect with your space to create a new awesome Metaphor. Controversial analogies are perfect to be part of an external backdrop that is referenced to the digital spac";

    String tech = "I use Zbrush Dynamesh to model the base mesh and OpenGl / GLSL for scenery and shaders. Basically there are no limitations as the sculpture can be bared out into infinity, but in practice it has been shown that a certain size is decisive. The sculpture is reflected over a surface that provides an inductive scale valid for each sculpture, so that the scene is given support and stability. The sculpture rotates horizontally, the pivot point is set below the mesh so that a homogeneous sequence of the fading in and fading out scene is possible. Each scenery is a live rendering consisting of 25k to 50k polygons with orthogonal normals. I have set two light sources, each of which correlates with two different shaders and is mixed in the final output.\n" +
            "The whole scenery is in full HD";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);

        RecyclerView recycler = view.findViewById(R.id.recycler_view);
        recycler.setAdapter(new Tv_Adapter());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}