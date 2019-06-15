package com.haksoy.pertem.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haksoy.pertem.R;
import com.haksoy.pertem.model.Measurement;
import com.haksoy.pertem.tools.Constant;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ghyeok.stickyswitch.widget.StickySwitch;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasurementFragment extends Fragment {

    @BindView(R.id.swcSex)
    StickySwitch mStickySwitch;
    @BindView(R.id.edtSize)
    EditText edtSize;
    @BindView(R.id.edtWeight)
    EditText edtWeight;
    @BindView(R.id.btnQuery)
    Button btnQuery;
    @BindView(R.id.txtExp)
    TextView txtExp;
    private Unbinder unbinder;

    public MeasurementFragment() {
        // Required empty public constructor
    }

    String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measurement, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlVariable()) {
                    hideKeyboard(getActivity());
                    txtExp.setText(getActivity().getResources().getString(R.string.size_weight_index_message, getIndex(Float.valueOf(edtSize.getText().toString()), Float.valueOf(edtWeight.getText().toString()))));
                    eligibleControl(Integer.valueOf(edtSize.getText().toString()), Integer.valueOf(edtWeight.getText().toString()));
                }
            }
        });

        edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    btnQuery.performClick();
                }
                return false;
            }
        });

    }

    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE );
        View f = activity.getCurrentFocus();
        if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
            imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
        else
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
    }
    private void eligibleControl(final int size, final int weight) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef;

        if (mStickySwitch.getDirection() == StickySwitch.Direction.LEFT) {
            mRef = database.getReference(Constant.ROOT_MEASUREMENT_BOY);
        } else mRef = database.getReference(Constant.ROOT_MEASUREMENT_GIRL);

        mRef.keepSynced(true);
        maxSizeControl(size, weight, mRef);

    }

    private void maxSizeControl(final int size, final int weight, final DatabaseReference mRef) {
        mRef.child(Constant.MEASUREMENT_MAX_SIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minSizeControl(size, weight, dataSnapshot.getValue(Integer.class), mRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void minSizeControl(final int size, final int weight, final int maxSize, final DatabaseReference mRef) {
        mRef.child(Constant.MEASUREMENT_MIN_SIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (sizeControl(size, dataSnapshot.getValue(Integer.class), maxSize)) {
                    weightControl(size, weight, mRef);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean sizeControl(int size, int minSize, int maxSize) {
        boolean result = true;
        if (size < minSize) {
            txtExp.setText(txtExp.getText() + "\n" + "Boyunuz " + (minSize - size) + " cm Eksik");
            result = false;
        } else if (size > maxSize) {
            txtExp.setText(txtExp.getText() + "\n" + "Boyunuz " + (size - maxSize) + " cm Fazla");
            result = false;
        }
        return result;
    }

    private void weightControl(final int size, final int weight, DatabaseReference mRef) {

        mRef.child(String.valueOf(size)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                weightControl(weight, dataSnapshot.getValue(Measurement.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void weightControl(int size, Measurement measurement) {

        if (size < measurement.low) {
            txtExp.setText(txtExp.getText() + "\n" + "Kilonuz " + (measurement.low - size) + " kg Eksik");

        } else if (size > measurement.high) {
            txtExp.setText(txtExp.getText() + "\n" + "Kilonuz " + (size - measurement.high) + " kg Fazla");
        } else txtExp.setText(txtExp.getText() + "\n" + "Boy-Kilo Durumunuz Uygun");
    }

    private String getIndex(float size, float weight) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(weight / ((size / 100) * (size / 100)));
    }

    private boolean controlVariable() {
        boolean result = true;
        if (edtSize.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.warn_size_is_empty), Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (edtWeight.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.warn_weight_is_empty), Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
