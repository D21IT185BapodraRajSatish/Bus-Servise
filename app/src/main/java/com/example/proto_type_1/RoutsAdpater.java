package com.example.proto_type_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RoutsAdpater extends ArrayAdapter<Routs> {
    Context mctx;
    int resource;
    List<Routs> routs;

    public RoutsAdpater(Context mctx, int resource, List<Routs> routs) {
        super(mctx, resource, routs);

        this.mctx = mctx;
        this.resource = resource;
        this.routs = routs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mctx);

        View view = inflater.inflate(resource, null);

        TextView textViewRoutNumber = view.findViewById(R.id.customize_Rout_Number);
        TextView textViewSourcePoint = view.findViewById(R.id.customize_Source_Point);
        TextView textViewDestinationPoint = view.findViewById(R.id.customize_Destination_Point);

        Routs routs1 = routs.get(position);

        textViewRoutNumber.setText(routs1.getCustomize_Rout_Number());
        textViewSourcePoint.setText(routs1.getCustomize_Source_Point());
        textViewDestinationPoint.setText(routs1.getCustomize_Destination_Point());

        return view;
    }
}
