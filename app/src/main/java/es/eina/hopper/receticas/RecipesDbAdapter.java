package es.eina.hopper.receticas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class RecipesDbAdapter {

    private static final String TAG = "RecipesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public static final String USERS_KEY_ROWID = "id";
    public static final String USERS_KEY_NAME = "name";
    //public static final String USERS_KEY_PASSWORD = "password";

    public static final String RECIPES_KEY_ROWID = "id";
    public static final String RECIPES_KEY_NAME = "name";
    public static final String RECIPES_KEY_TOTAL_TIME = "total_time";
    public static final String RECIPES_KEY_PERSON = "person";
    public static final String RECIPES_KEY_IMAGE = "image";
    public static final String RECIPES_KEY_USER = "user";

    public static final String UTENSILS_KEY_ROWID = "id";
    public static final String UTENSILS_KEY_NAME = "name";

    public static final String INGREDIENTS_KEY_ROWID = "id";
    public static final String INGREDIENTS_KEY_NAME = "name";

    public static final String STEPS_KEY_STEP = "step";
    public static final String STEPS_KEY_TIME = "time";
    public static final String STEPS_KEY_INFORMATION = "information";

    public static final String USE_KEY_QUANTITY = "step";




    /*
     * Database creation sql statements
     */
    private static final String DATABASE_CREATE_USERS =
            "create table users (" +
                    "id BIGINT (6) auto_increment ," +
                    "name VARCHAR (32) unique not null," +
                    "PRIMARY KEY (id)" +
                    ");";

    private static final String DATABASE_CREATE_RECIPES =
            "create table recipes (" +
                    "id BIGINT (6) auto_increment ," +
                    "name VARCHAR (32) not null," +
                    "total_time BIGINT (6) not null," +
                    "person BIGINT (6) not null," +
                    "image BLOB," +
                    "user BIGINT (6)," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (user) references users(id)" +
                    ");";

    private static final String DATABASE_CREATE_UTENSILS =
            "create table utensils (" +
                    "id BIGINT (6) auto_increment," +
                    "name VARCHAR (32) not null," +
                    "PRIMARY KEY (id)" +
                    ");";

    private static final String DATABASE_CREATE_INGREDIENTS =
            "create table ingredients (" +
                    "id BIGINT (6) auto_increment," +
                    "name VARCHAR (32) not null," +
                    "PRIMARY KEY (id)" +
                    ");";

    private static final String DATABASE_CREATE_STEPS =
            "create table steps (" +
                    "step BIGINT (6) ," +
                    "time BIGINT (6) not null," +
                    "information VARCHAR (500) not null," +
                    "recipe BIGINT (6) ," +
                    "PRIMARY KEY (step, recipe)," +
                    "FOREIGN KEY (recipe) references recipes(id)" +
                    ");";

    private static final String DATABASE_CREATE_USE1 =
            "create table use_1 (" +
                    "recipe BIGINT (6) ," +
                    "utensil BIGINT (6) ," +
                    "PRIMARY KEY (recipe, utensil)," +
                    "FOREIGN KEY (recipe) references recipes(id),\n" +
                    "FOREIGN KEY (utensil) references utensils(id)\n" +
                    ");";

    private static final String DATABASE_CREATE_USE2 =
            "create table use_2 (" +
                    "recipe BIGINT (6) ," +
                    "ingredient BIGINT (6) ," +
                    "quantity BIGINT (6) not null," +
                    "PRIMARY KEY (recipe, ingredient)," +
                    "FOREIGN KEY (recipe) references recipes(id)," +
                    "FOREIGN KEY (ingredient) references ingredients(id)" +
                    ");";

    private static final String DATABASE_CREATE_USE3 =
            "create table use_3 (" +
                    "step BIGINT (6) ," +
                    "recipe BIGINT (6) ," +
                    "quantity BIGINT (6) not null," +
                    "ingredient BIGINT (6) ," +
                    "PRIMARY KEY (step,recipe, ingredient)," +
                    "FOREIGN KEY (recipe) references steps(recipe)," +
                    "FOREIGN KEY (step) references steps(step)," +
                    "FOREIGN KEY (ingredient) references ingredients(id)" +
                    ");";

    private static final String DATABASE_CREATE_USE4 =
            "create table use_4 (" +
                    "step BIGINT (6) ," +
                    "recipe BIGINT (6) ," +
                    "utensil BIGINT (6) ," +
                    "PRIMARY KEY (step,recipe, utensil)," +
                    "FOREIGN KEY (recipe) references steps(recipe)," +
                    "FOREIGN KEY (step) references steps(step)," +
                    "FOREIGN KEY (utensil) references utensils(id)" +
                    ");";

    private static final String DATABASE_NAME = "prsoft";

    public static final String DATABASE_TABLE_USERS = "users";
    public static final String DATABASE_TABLE_RECIPES = "recipes";
    public static final String DATABASE_TABLE_UTENSILS = "utensils";
    public static final String DATABASE_TABLE_INGREDIENTS = "ingredients";
    public static final String DATABASE_TABLE_STEPS = "steps";
    // Relación receta usa utensilios
    public static final String DATABASE_TABLE_USE1 = "use_1";
    // Relación receta usa ingredientes
    public static final String DATABASE_TABLE_USE2 = "use_2";
    // Relación: receta y paso usa ingredientes
    public static final String DATABASE_TABLE_USE3 = "use_3";
    // Relación: receta y paso usa utensilios
    public static final String DATABASE_TABLE_USE4 = "use_4";

    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_USERS);
            db.execSQL(DATABASE_CREATE_RECIPES);
            db.execSQL(DATABASE_CREATE_UTENSILS);
            db.execSQL(DATABASE_CREATE_INGREDIENTS);
            db.execSQL(DATABASE_CREATE_STEPS);
            db.execSQL(DATABASE_CREATE_USE1);
            db.execSQL(DATABASE_CREATE_USE2);
            db.execSQL(DATABASE_CREATE_USE3);
            db.execSQL(DATABASE_CREATE_USE4);

            db.execSQL("INSERT INTO `users` (`id`, `name`, `password`) VALUES " +
                    "(1, 'test', 'test'), " +
                    "(2, 'admin', 'admin');");

            db.execSQL("INSERT INTO `recipes` (`id`, `name`, `total_time`, `person`, `user`) VALUES " +
                    " (1, 'Macarroncicos a la maña', 15, 1, 1), " +
                    "(1,'Pasta al pesto', 20, 1, 1);" );

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS recipes");
            db.execSQL("DROP TABLE IF EXISTS utensils");
            db.execSQL("DROP TABLE IF EXISTS ingredients");
            db.execSQL("DROP TABLE IF EXISTS steps");
            db.execSQL("DROP TABLE IF EXISTS use1");
            db.execSQL("DROP TABLE IF EXISTS use2");
            db.execSQL("DROP TABLE IF EXISTS use3");
            db.execSQL("DROP TABLE IF EXISTS use4");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public RecipesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public RecipesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Return a Cursor over the list of users in database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllUsers () {

        return mDb.query(DATABASE_TABLE_USERS, new String[] {USERS_KEY_ROWID, USERS_KEY_NAME}, null, null, null, null, null);

    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchUser(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_USERS, new String[] {USERS_KEY_NAME,
                                }, USERS_KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    /**
     * Return a Cursor over the list of all recipes
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllRecipes () {

        return mDb.query(DATABASE_TABLE_RECIPES, new String[] {RECIPES_KEY_ROWID, RECIPES_KEY_NAME,
                RECIPES_KEY_USER, RECIPES_KEY_PERSON, RECIPES_KEY_TOTAL_TIME, RECIPES_KEY_IMAGE}, null, null, null, null, null);

    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchRecipe(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_RECIPES, new String[] {RECIPES_KEY_ROWID, RECIPES_KEY_NAME,
                                RECIPES_KEY_USER, RECIPES_KEY_PERSON, RECIPES_KEY_TOTAL_TIME, RECIPES_KEY_IMAGE}, RECIPES_KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }




}