package com.adjo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import DataBase.Operation;
import DataBase.OperationCtrl;

public class HomeFragment extends Fragment {

    public static EditText ET_date;
    public static TextView ET_caisse;
    public static TextView ET_depense;
    public static TextView ET_benef;
    public static TextView ET_depense_jour;
    public static TextView Et_vente_jour;
    public static ListView listView;

    private Operation operation;

    public static HomeFragment newInstance() {
        return (new HomeFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View root =  inflater.inflate(R.layout.home, container, false);

        ET_date = root.findViewById(R.id.date_home);
        ET_caisse = root.findViewById(R.id.somme_caisse);
        ET_depense = root.findViewById(R.id.somme_depense);
        ET_benef = root.findViewById(R.id.somme_benef);
        Et_vente_jour = root.findViewById(R.id.vente_jour);
        ET_depense_jour = root.findViewById(R.id.dep_jour);
        listView = (ListView) root.findViewById(R.id.home_list);

        final OperationCtrl operationCtrl = new OperationCtrl(getContext());

        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYY");
        ET_date.setText(df.format(current));
        df = new SimpleDateFormat("yyyy-MM-dd");
        updateView(operationCtrl, df.format(current));

        ET_caisse.setText(new StringBuilder().append(getInvestissement(operationCtrl)-getDepense(operationCtrl)+getVente(operationCtrl)));
        ET_depense.setText(new StringBuilder().append(getDepense(operationCtrl)));
        ET_benef.setText(new StringBuilder().append(getVente(operationCtrl)-getDepense(operationCtrl)));

        ET_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choix de la date");
                final View form = getLayoutInflater().inflate(R.layout.date,null);
                builder.setView(form);
                builder.setPositiveButton("Valider", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatePicker datePicker = form.findViewById(R.id.calendar);
                                int jour = datePicker.getDayOfMonth();
                                int mois = datePicker.getMonth()+1;
                                int annee = datePicker.getYear();

                                ET_date.setText(new StringBuilder()
                                        .append(jour).append('/')
                                        .append(mois).append('/')
                                        .append(annee));

                                OperationCtrl operationCtrl = new OperationCtrl(getContext());

                                updateView(operationCtrl, annee+"-"+mois+"-"+jour);
                            }
                        });
                builder.setNegativeButton("Annuler", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int
                                    i) {
                                DatePicker datePicker = form.findViewById(R.id.calendar);
                                ET_date.setText("");
                            }
                        });
                builder.show();
            }
        });

        int i = 0;
        long l = 0;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int choix = position;

                final ArrayList<Operation> liste_operation = new OperationCtrl(getContext()).getAllOperation();

                operation = liste_operation.get(position);

                AlertDialog.Builder builder = new
                        AlertDialog.Builder(getContext());
                builder.setTitle("Suppression d'une opération");
                builder.setMessage("Voulez-vous vraiment supprimer cette opération ?");
                builder.setPositiveButton("Supprimer", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                operationCtrl.delete(operation.getId());
                                //liste_operation.delete(operation.getId());

                                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT);

                                //listView.setAdapter(new CustomAdapter(getContext()));
                            }
                        });
                builder.setNegativeButton("Annuler",null);
                builder.show();
            }
        });

        return root;
    }

    private void updateView(OperationCtrl operationCtrl, String date_choisies) {
        ET_depense_jour.setText(new StringBuilder().append(getDepenseJour(operationCtrl, date_choisies)));
        Et_vente_jour.setText(new StringBuilder().append(getVenteJour(operationCtrl, date_choisies)));
        listView.setAdapter(new CustomAdapter(getContext(), getAllData(operationCtrl, date_choisies)));
    }

    public static String[] getAllData (OperationCtrl operationCtrl, String date_choisie)
    {
        ArrayList<Operation> operations = new ArrayList<>();
        ArrayList<String> data_ar = new ArrayList<>();

        operations = operationCtrl.getAllOperation();

        for (Operation op : operations) {
            if (op.getDate().equals(date_choisie))
                data_ar.add(op.getLibelle() + "/" + op.getType() + "/" + op.getSomme());
        }

        String[] data = new String[data_ar.size()];

        for (int i = 0; i < data_ar.size(); i++) {
            data[i] = data_ar.get(i);
        }

        return data;
    }

    public static float getDepense (OperationCtrl operationCtrl) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations = operationCtrl.getAllOperation();

        float somme = 0;

        for (Operation op : operations) {
            if (op.getType().equals("Dépense"))
            somme += op.getSomme();
        }

        return somme;
    }

    public static float getVente (OperationCtrl operationCtrl) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations = operationCtrl.getAllOperation();

        float somme = 0;

        for (Operation op : operations) {
            if (op.getType().contains("Vente"))
            somme += op.getSomme();
        }

        return somme;
    }

    public static float getInvestissement (OperationCtrl operationCtrl) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations = operationCtrl.getAllOperation();

        float somme = 0;

        for (Operation op : operations) {
            if (op.getType().equals("Investissement"))
                somme += op.getSomme();
        }

        return somme;
    }

    public static float getDepenseJour(OperationCtrl operationCtrl, String date_choisie) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations = operationCtrl.getAllOperation();

        float somme = 0;

        for (Operation op : operations) {
            if (op.getType().equals("Dépense") && op.getDate().equals(date_choisie))
                somme += op.getSomme();
        }

        return somme;
    }

    public static float getVenteJour(OperationCtrl operationCtrl, String date_choisie) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations = operationCtrl.getAllOperation();

        float somme = 0;

        for (Operation op : operations) {
            if (op.getType().contains("Vente") && op.getDate().equals(date_choisie))
                somme += op.getSomme();
        }

        return somme;
    }
}
