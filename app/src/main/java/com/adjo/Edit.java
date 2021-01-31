package com.adjo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

import DataBase.Operation;
import DataBase.OperationCtrl;

import static com.adjo.R.id.depense;

public class Edit extends AppCompatActivity {

    private DatePicker DP_date;
    private EditText ET_date, ET_libelle, ET_somme;
    private Button B_valider;
    private RadioButton RB_Investissement, RB_Depense, RB_VenteComptant, RB_VenteCredit, RB_Remboursement;
    private RadioGroup RG_typeOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        DP_date =  findViewById(R.id.calendar);
        ET_date = findViewById(R.id.date);
        ET_libelle = findViewById(R.id.operation);
        ET_somme = findViewById(R.id.somme);
        B_valider = findViewById(R.id.btn_valider);

        RB_Depense = findViewById(depense);
        RB_Investissement = findViewById(R.id.investissement);
        RB_VenteComptant = findViewById(R.id.vente_comptant);
        RB_VenteCredit = findViewById(R.id.vente_credit);
        RB_Remboursement = findViewById(R.id.remboursement);

        RG_typeOperation = findViewById(R.id.radio_group);

        RB_Investissement.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    // Choix de la date au clic du champ date
    public void date(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
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
                    }
                });
        builder.setNegativeButton("Annuler", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int
                            i) {
                        DatePicker datePicker = form.findViewById(R.id.calendar);
                        ET_date.setText("");
                        toast("Annuler");
                    }
                });
        builder.show();
    }

    private void toast(String msg) {
        Toast.makeText(Edit.this, msg, Toast.LENGTH_LONG).
                show();
    }

    public void valider(View view) {

        if (ET_libelle.getText().toString().trim().isEmpty()) {
            ET_libelle.setHint("Opération !!");
            ET_libelle.setHintTextColor(Color.RED);
            toast("Inserez le nom de l'opération");
        }
        else if (ET_somme.getText().toString().trim().isEmpty()) {
            ET_somme.setHint("Somme !!");
            ET_somme.setHintTextColor(Color.RED);
            toast("Inserez la somme de l'opération");
        }
        else if (ET_date.getText().toString().trim().isEmpty()) {
            ET_date.setHint("Date !!!");
            ET_date.setHintTextColor(Color.RED);
            toast("Inserez la date de l'opération");
        }
        //Creation de l'operation
        else {

            Operation operation = new Operation();

            String type_operation = new String();

            switch (RG_typeOperation.getCheckedRadioButtonId()) {
                case R.id.depense:
                    operation.setType("Dépense");
                    break;
                case R.id.investissement:
                    operation.setType("Investissement");
                    break;
                case R.id.vente_comptant:
                    operation.setType("Vente au comptant");
                    break;
                case R.id.vente_credit:
                    operation.setType("Vente à crédit");
                    break;
                case R.id.remboursement:
                    operation.setType("Remboursement");
                    break;
                default:
                    break;
            }

            String[] tab = ET_date.getText().toString().split("/");

            operation.setLibelle(ET_libelle.getText().toString());
            operation.setDate(tab[2]+"-"+tab[1]+"-"+tab[0]);
            operation.setSomme(Float.parseFloat(ET_somme.getText().toString()));

            OperationCtrl operationCtrl = new OperationCtrl(getApplicationContext());

            operationCtrl.insert(operation);

            toast("Opération Ajoutée");

            finish();
        }
    }
}
