package com.adjo;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import DataBase.Operation;
import DataBase.OperationCtrl;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemLongClickListener {
    private final Context context;
    private final String[] data;
    private ArrayList<Operation> liste_operation;
    /*private final Float[] somme;
    private final Float[] pourcentage;*/




    public CustomAdapter(Context context, String[] data) {
        super(context, R.layout.row_stat_item, data);
        this.context = context;
        this.data = data;
    }

    public CustomAdapter(Context context) {
        super(context, R.layout.row_stat_item);
        this.context = context;
        this.data = new String[0];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_stat_item, parent, false);
        TextView TV_libelle = (TextView) rowView.findViewById(R.id.list_item_stat_libelle);
        TextView TV_somme = (TextView) rowView.findViewById(R.id.list_item_stat_somme);
        TextView TV_pourcentage = (TextView) rowView.findViewById(R.id.list_item_stat_pourcentage);
        ImageView IV_icone = (ImageView) rowView.findViewById(R.id.list_item_stat_img);

        String[] dataTab = data[position].split("/");

        TV_libelle.setText(dataTab[0]);
        TV_somme.setText(dataTab[2]);
        if (dataTab.length == 4)
            TV_pourcentage.setText(dataTab[3]+"%");
        else  TV_pourcentage.setText("");

        String s = dataTab[1];

        System.out.println(s);

        switch (s) {
            case "Investissement":
                IV_icone.setImageResource(R.drawable.invest);
                break;
            case "Dépense":
                IV_icone.setImageResource(R.drawable.dep);
                break;
            case "Vente au comptant":
                IV_icone.setImageResource(R.drawable.vente_com);
                break;
            case "Vente à crédit":
                IV_icone.setImageResource(R.drawable.vente_cred);
                break;
            case "Remboursement":
                IV_icone.setImageResource(R.drawable.rembourse);
                break;
        }

        return rowView;

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int choix = position;

        liste_operation = new OperationCtrl(getContext()).getAllOperation();

        final Operation operation = liste_operation.get(position);

        AlertDialog.Builder builder = new
                AlertDialog.Builder(getContext());
        builder.setTitle("Suppression d'une opération");
        builder.setMessage("Voulez-vous vraiment supprimer cette opération ?");
                builder.setPositiveButton("Supprimer", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new OperationCtrl(getContext()).delete(operation.getId());
                            }
                        });
        builder.setNegativeButton("Annuler",null);
        builder.show();
        return false;
    }
}
