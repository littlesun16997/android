package fi.jamk.fragmentsexercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

public class MenuGallery extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Gallery");

        ViewFlipper flipper = (ViewFlipper) getView().findViewById(R.id.flipper1);

        /** Start Flipping */
        flipper.setAutoStart(true);
        flipper.startFlipping();
    }
}
