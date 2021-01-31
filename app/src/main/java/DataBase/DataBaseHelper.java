package DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;

    private final static String DATABASE_NAME="adjo_db";
    private final static int VERSION = 1;
    public final static String TABLE_OPERATION="operations";
    public final static String KEY_ID="id";
    public final static String KEY_TYPE_OPERATION="type_operation";
    public final static String KEY_LIBELLE_OPERATION="libelle_operation";
    public final static String KEY_DATE_OPERATION="date_operation";
    public final static String KEY_SOMME_OPERATION="somme_operation";


    private static final String CREATE_OPERATION=
            "CREATE TABLE " + TABLE_OPERATION+ " ( "+
                    KEY_ID+" integer primary key autoincrement, "+
                    KEY_TYPE_OPERATION+" VARCHAR(50) not null, "+
                    KEY_LIBELLE_OPERATION+" VARCHAR(50) not null, "+
                    KEY_DATE_OPERATION+" NUMERIC not null, "+
                    KEY_SOMME_OPERATION+" NUMERIC not null);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_OPERATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_OPERATION);
    }
}
