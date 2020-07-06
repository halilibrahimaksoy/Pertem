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
import com.haksoy.pertem.adapter.ProcurementAdapter;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.model.Procurement;
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
public class ProcurementFragment extends Fragment implements INotifyAction {

    @BindView(R.id.lstMainProcurement)
    ListView lstMainProcurement;
    @BindView(R.id.txtEmptyMessage)
    TextView txtEmptyMessage;
    Unbinder unbinder;
    private List<Procurement> procurementList;
    private List<String> keys;
    INotifyAction notifyAction;
    private List<Procurement> originalValue;
    private ProcurementAdapter mAdapter;

    public ProcurementFragment() {
        // Required empty public constructor
    }

    public ProcurementFragment(INotifyAction notifyAction) {
        this.notifyAction = notifyAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procurement, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (notifyAction != null)
            notifyAction.onNotified(Enums.SetNotifyAction, (INotifyAction) this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        procurementList = new ArrayList<>();
        keys = new ArrayList<>();
        mAdapter = new ProcurementAdapter(getActivity(), procurementList);

        lstMainProcurement.setAdapter(mAdapter);
        FirebaseClient.getInstance().getProcurementList(this);
        lstMainProcurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (notifyAction != null)
                    notifyAction.onNotified(Enums.ProcurementDetail, procurementList.get(position).getDesc());
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
        } else if (key == Enums.GetProcurement) {
            procurementList = (List<Procurement>) value;
            mAdapter.setData(procurementList);
            if (txtEmptyMessage != null)
                if (procurementList.size() == 0)
                    txtEmptyMessage.setVisibility(View.VISIBLE);
                else
                    txtEmptyMessage.setVisibility(View.GONE);
        }
    }


    public void filter(String filterString) {

        if (originalValue == null)
            originalValue = new ArrayList<>(procurementList);

        if (filterString == null || filterString.isEmpty())
            mAdapter.setData(originalValue);
        else {
            List<Procurement> filteredList = new ArrayList<>();

            for (Procurement procurement : originalValue) {
                if (procurement.getTitle().toLowerCase().contains(filterString.toLowerCase()))
                    filteredList.add(procurement);
            }
            mAdapter.setData(filteredList);
        }
    }
}
