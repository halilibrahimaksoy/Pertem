package com.haksoy.pertem.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haksoy.pertem.R;
import com.haksoy.pertem.adapter.AnnounceAdapter;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnounceFragment extends Fragment implements INotifyAction {

    @BindView(R.id.lstMainAnnounce)
    ListView lstMainAnnounce;
    @BindView(R.id.txtEmptyMessage)
    TextView txtEmptyMessage;
    Unbinder unbinder;
    private List<Announce> announceList;
    private List<Announce> originalValue;
    INotifyAction notifyAction;
    private AnnounceAdapter mAdapter;

    public AnnounceFragment() {
        // Required empty public constructor
    }

    public AnnounceFragment(INotifyAction notifyAction) {
        this.notifyAction = notifyAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announce, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (notifyAction != null)
            notifyAction.onNotified(Enums.SetNotifyAction, (INotifyAction) this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        announceList = new ArrayList<>();
        mAdapter = new AnnounceAdapter(getActivity(), announceList);
        lstMainAnnounce.setAdapter(mAdapter);
        FirebaseClient.getInstance().getAnnounceList(this);
        lstMainAnnounce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (notifyAction != null)
                    notifyAction.onNotified(Enums.AnnounceDetail, announceList.get(position).getDesc());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNotified(Object key, Object value) {
        if (key == Enums.Search) {
            filter((String) value);
        } else if (key == Enums.GetAnnounce) {
            announceList = (List<Announce>) value;
            mAdapter.setData(announceList);
            if (txtEmptyMessage != null)
                if (announceList.size() == 0)
                    txtEmptyMessage.setVisibility(View.VISIBLE);
                else
                    txtEmptyMessage.setVisibility(View.GONE);
        }
    }

    public void filter(String filterString) {

        if (originalValue == null)
            originalValue = new ArrayList<>(announceList);

        if (filterString == null || filterString.isEmpty())
            mAdapter.setData(originalValue);
        else {
            List<Announce> filteredList = new ArrayList<>();

            for (Announce announce : originalValue) {
                if (announce.getTitle().toLowerCase().contains(filterString.toLowerCase()))
                    filteredList.add(announce);
            }
            mAdapter.setData(filteredList);
        }
    }
}
