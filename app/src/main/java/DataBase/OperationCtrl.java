package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class OperationCtrl {

    private final Context context;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public OperationCtrl(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    private void close(){
        db=dataBaseHelper.getReadableDatabase();
        if(db !=null && db.isOpen()){
            db.close();
        }
    }

    public boolean insert(Operation operation){
        long id=0;
        db = dataBaseHelper.getWritableDatabase();

        ContentValues params = new ContentValues();
        params.put(DataBaseHelper.KEY_TYPE_OPERATION, operation.getType());
        params.put(DataBaseHelper.KEY_LIBELLE_OPERATION, operation.getLibelle());
        params.put(DataBaseHelper.KEY_DATE_OPERATION, operation.getDate());
        params.put(DataBaseHelper.KEY_SOMME_OPERATION, operation.getSomme());

        id=db.insert(DataBaseHelper.TABLE_OPERATION,null,params);
        close();

        return id >0;

    }


    public boolean update(long rowid, Operation operation){
        db = dataBaseHelper.getWritableDatabase();

        ContentValues params = new ContentValues();
        params.put(DataBaseHelper.KEY_TYPE_OPERATION, operation.getType());
        params.put(DataBaseHelper.KEY_LIBELLE_OPERATION, operation.getLibelle());
        params.put(DataBaseHelper.KEY_DATE_OPERATION, operation.getDate());
        params.put(DataBaseHelper.KEY_SOMME_OPERATION, operation.getSomme());

        long result = 0;
        result= db.update(DataBaseHelper.TABLE_OPERATION, params,DataBaseHelper.KEY_ID+"="+rowid,null);
        close();

        return result>0;

    }

    public boolean delete(long rowid){
        db=dataBaseHelper.getReadableDatabase();
        long result =0;
        result= db.delete(DataBaseHelper.TABLE_OPERATION,DataBaseHelper.KEY_ID+ "="+rowid,null);
        close();
        return result>0;
    }

    public ArrayList<Operation> getAllOperation(){
        ArrayList<Operation> operations = new ArrayList<>();
        db=dataBaseHelper.getReadableDatabase();
        String query="SELECT * FROM "+DataBaseHelper.TABLE_OPERATION+";";

        Cursor c = db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                long id = c.getLong(c.getColumnIndex(DataBaseHelper.KEY_ID));

                String type = c.getString(c.getColumnIndex(DataBaseHelper.KEY_TYPE_OPERATION));
                String libelle = c.getString(c.getColumnIndex(DataBaseHelper.KEY_LIBELLE_OPERATION));
                String date = c.getString(c.getColumnIndex(DataBaseHelper.KEY_DATE_OPERATION));
                Float somme = c.getFloat(c.getColumnIndex(DataBaseHelper.KEY_SOMME_OPERATION));
                long id_op = c.getLong(c.getColumnIndex(DataBaseHelper.KEY_ID));

                operations.add( new Operation(type, libelle, somme, date));

            }while(c.moveToNext());
        }
        return operations;
    }
}

