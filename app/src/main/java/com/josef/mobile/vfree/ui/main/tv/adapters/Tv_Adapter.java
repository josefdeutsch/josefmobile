package com.josef.mobile.vfree.ui.main.tv.adapters;

import android.animation.LayoutTransition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Tv_Adapter extends RecyclerView.Adapter<Tv_Adapter.MyViewHolder> {

    public String firstPhrase = "I developed Joseph because I was looking for alternative ways to present my art. During my work I was confronted more and more with the idea of digital space. What exactly is digital space and how does it appear?";
    public String vision = "The way of reinterpreting an exhibition space fascinates me and has motivated me so much to implement and complete Joseph according to my ideas. In the end, Joseph himself became a work of art and the metaphor: \"The expression travels through space\" is the message that comes from the heart of the matter.To show that this can be seen by humans and thus by a work of art, we have to accept that digital art is real. The authenticity of a thing primarily determines its value and that is precisely why we have to admit it. The world is in a steady emergent process what was morally right today is where possible wrong tomorrow, but nothing like that with art, it goes timelessly through time. A solid component that can be reinterpreted over and over again. But every story is technologically influenced and if we think about it further, I am convinced that there is not only a digital work of art but also a digital space, because every thing is an instance of space.Then the willingness to authenticate a thing is the acceptance of the room itself. I built Joseph because I love that thought. Digital art is true. I'm sure the more our world emerges digitally, the more we will believe in it.";

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.title.setText("Notification #" + position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //final TextView title;
      //  final View toggle;
      //  final TextView body;
      //  final TextView fphrase;

        public MyViewHolder(View itemView) {
            super(itemView);

            // this.title = itemView.findViewById(R.id.title);
          //  this.toggle = itemView.findViewById(R.id.toggle);
          //  this.body = itemView.findViewById(R.id.body);
           // this.fphrase = itemView.findViewById(R.id.firstPhrase);

         //   body.setText(vision);
         //   toggle.setOnClickListener(view -> body.setVisibility(body.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
// sample code snippet to set the text content on the ExpandableTextView
            ExpandableTextView expTv1 = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
            expTv1.setText(vision);
            CardView card = itemView.findViewById(R.id.card);
            card.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

            ConstraintLayout contents = itemView.findViewById(R.id.contents);
            contents.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            contents.getLayoutTransition().setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        }
    }
}




