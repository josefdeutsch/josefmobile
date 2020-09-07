package com.josef.mobile.free.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ui.body.Data;
import com.josef.mobile.free.ui.content.ContentActivity;
import com.josef.mobile.free.ui.detail.Worker;
import com.josef.mobile.free.ui.adapter.ColourButtonAdapter;
import java.util.ArrayList;

import static com.josef.mobile.util.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.util.Config.WORKREQUEST_DOWNLOADID;

public class DialogColorFragment extends DialogBaseFragment implements ColourButtonAdapter.ColourButtonAdapterOnClickHander {

    private RecyclerView mRecyclerView;

    public static DialogColorFragment newInstance(String which, int index) {
        DialogColorFragment fragment = new DialogColorFragment();
        Bundle args = new Bundle();
        args.putString(WORKREQUEST_DOWNLOADID, which);
        args.putInt(VIEWPAGERDETAILKEY, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDownloadId = getArguments().getString(WORKREQUEST_DOWNLOADID);
            index = getArguments().getInt(VIEWPAGERDETAILKEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_detail_dialog, container, false);
        mProgressBar = layoutInflater.findViewById(R.id.progress);
        mRecyclerView = layoutInflater.findViewById(R.id.dialogrecycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        doWork(new Worker() {
            @Override
            public void execute(String input, int index, int query) throws Exception {
                ArrayList<Data> datalist = new ArrayList<>();
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                datalist.add(new Data("#C8C8C8"));
                datalist.add(new Data("#180000"));
                datalist.add(new Data("#ffff00"));
                mRecyclerView.setAdapter(new ColourButtonAdapter(getContext(), DialogColorFragment.this, datalist));
            }
        });
        return layoutInflater;
    }

    @Override
    public void onClick(String string) {
        int query = Integer.parseInt(string);
       /** if (getTargetFragment() == null) return;
        Intent intent = new Intent().putExtra("recylerindex", string);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();**/
        ((ContentActivity)getActivity()).replaceLayout(query);
        }
    }


