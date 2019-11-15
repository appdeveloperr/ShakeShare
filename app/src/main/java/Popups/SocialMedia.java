package Popups;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.project.usmansh.ingrumidreal.R;

public class SocialMedia extends AppCompatDialogFragment {


    ImageView popupSoc_mainImgIV;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_social_media, null);

        popupSoc_mainImgIV = (ImageView)view.findViewById(R.id.popupSoc_mainImgIV);



        builder.setView(view);
        return builder.create();
    }



    public void setImageID(String imgid){


    }



}
