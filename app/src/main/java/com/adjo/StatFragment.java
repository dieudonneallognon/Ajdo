package com.adjo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashSet;

import DataBase.Operation;
import DataBase.OperationCtrl;

public class StatFragment extends Fragment {

    private static  OperationCtrl operationCtrl;
    private static TextView TV_Chiffre_affaire;
    private static TextView TV_Ventes_credit;
    private static TextView TV_Ventes_comptant;
    private static TextView TV_Rentabilite;
    private static TextView TV_Depenses;
    private static ListView listView;

    public  static StatFragment newInstance() {
        return (new StatFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View root = inflater.inflate(R.layout.activity_statistuques, container, false);

        TV_Chiffre_affaire = (TextView) root.findViewById(R.id.tv_chiffre_affaire);
        TV_Ventes_credit = (TextView) root.findViewById(R.id.tv_ventes_credit);
        TV_Ventes_comptant = (TextView) root.findViewById(R.id.tv_ventes_comptant);
        TV_Rentabilite = (TextView) root.findViewById(R.id.tv_rentabilite);
        TV_Depenses = (TextView) root.findViewById(R.id.tv_depense);

        OperationCtrl operationCtrl = new OperationCtrl(getContext());
        updateView(operationCtrl);


        listView = (ListView) root.findViewById(R.id.stat_list);

        listView.setAdapter(new CustomAdapter(getContext(), getAllData(operationCtrl)));

        return  root;
    }

    public static void updateView(OperationCtrl operationCtrl) {

        ArrayList<Operation> operations = new ArrayList<>();
        ArrayList<String> liste_date = new ArrayList<>();

        //operationCtrl = new OperationCtrl(getApplicationContext());

        operations = operationCtrl.getAllOperation();

        float vente_credit = 0;
        float vente_comptant = 0;
        float investissement = 0;
        float depense = 0;

        for (Operation operation : operations) {
            if (operation.getType().equals("Vente à crédit")) vente_credit += operation.getSomme();
            else if (operation.getType().equals("Vente au comptant")) vente_comptant += operation.getSomme();
            else if (operation.getType().equals("Investissement")) investissement += operation.getSomme();
            else if (operation.getType().equals("Dépense")) depense += operation.getSomme();

            liste_date.add(operation.getDate());
        }

        float totalVente = vente_comptant + vente_credit;



        TV_Chiffre_affaire.setText(new StringBuilder().append(totalVente));

        TV_Ventes_comptant.setText(new StringBuilder()
                .append((float)Math.round(100*vente_comptant)/100)
                .append("/")
                .append((float)Math.round(100*vente_comptant/totalVente*100)/100)
                .append("%"));

        TV_Ventes_credit.setText(new StringBuilder()
                .append((float)Math.round(100*vente_credit)/100)
                .append("/")
                .append((float)Math.round(100*vente_credit/totalVente*100)/100)
                .append("%"));

        float benef = vente_comptant + vente_credit - depense;
        float rentabilite = 100*(benef - depense)/depense;

        TV_Rentabilite.setText(new StringBuilder()
        .append((float)Math.round(100*rentabilite)/100)
        .append("%"));

        TV_Depenses.setText(new StringBuilder()
        .append((float)Math.round(depense*100)/100));
    }

    public static String[] getAllData (OperationCtrl operationCtrl)
    {
        ArrayList<Operation> operations = new ArrayList<>();

        operations = operationCtrl.getAllOperation();

        ArrayList<String> liste = new ArrayList<>();
        ArrayList<Float> listSomme = new ArrayList<>();

        for (Operation operation : operations) {
            liste.add(operation.getLibelle()+"/"+operation.getType());
            listSomme.add(operation.getSomme());
        }

        HashSet<String> dinstinctLibelle = new HashSet(liste);

        String[] listeLibelle = new String[dinstinctLibelle.size()];
        String[] listeType = new String[dinstinctLibelle.size()];
        String[] listeData = new String[dinstinctLibelle.size()];

        int i = 0;
        for (String str : dinstinctLibelle) {
            String[] tab = str.split("/");
            listeLibelle[i] = tab[0];
            listeType[i] = tab[1];
            i++;
        }

        for (int j = 0; j < listeLibelle.length; j++) {
            float somme = 0;
            float total_type = 0;
            for (int k = 0; k < operations.size(); k++) {
                if (listeLibelle[j].equals(operations.get(k).getLibelle()) && listeType[j].equals(operations.get(k).getType())) {
                    somme += listSomme.get(k);
                }

                if (operations.get(k).getType().equals(listeType[j])) {
                    total_type += operations.get(k).getSomme();
                }

            }

            float pourcentage = 100 * somme/total_type;
            listeData[j] = listeLibelle[j]+"/"+listeType[j]+"/"+somme+"/"+Math.round(100 * pourcentage)/100;
            //listeLibelle[j] += "/"+somme;
        }
        return listeData;
    }

}
