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

    public static final String USE_KEY_RECIPE = "recipe";
    public static final String USE2_KEY_INGREDIENT = "ingredient";
    public static final String USE_KEY_QUANTITY = "quantity";
    public static final String USE1_KEY_UTENSIL = "utensil";




    /*
     * Database creation sql statements
     */
    private static final String DATABASE_CREATE_USERS =
            "create table users (" +
                    "id INTEGER primary key autoincrement ," +
                    "name VARCHAR (32) not null unique " +
                    ");";

    private static final String DATABASE_CREATE_RECIPES =
            "create table recipes (" +
                    "id INTEGER primary key autoincrement ," +
                    "name VARCHAR (32) not null," +
                    "total_time BIGINT (6) not null," +
                    "person BIGINT (6) not null," +
                    "image BLOB," +
                    "user BIGINT (6)," +
                    "FOREIGN KEY (user) references users(id)" +
                    ");";


    private static final String DATABASE_CREATE_UTENSILS =
            "create table utensils (" +
                    "id BIGINT (6) primary key autoincrement," +
                    "name VARCHAR (32) not null," +
                    "UNIQUE (name)" +
                    "PRIMARY KEY (id)" +
                    ");";

    private static final String DATABASE_CREATE_INGREDIENTS =
            "create table ingredients (" +
                    "id INTEGER primary key autoincrement," +
                    "name VARCHAR (32) not null," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE (name)" +
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
                    "quantity varchar (30) not null," +
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
            /*db.execSQL(DATABASE_CREATE_UTENSILS);
            db.execSQL(DATABASE_CREATE_INGREDIENTS);
            db.execSQL(DATABASE_CREATE_STEPS);
            db.execSQL(DATABASE_CREATE_USE1);
            db.execSQL(DATABASE_CREATE_USE2);
            db.execSQL(DATABASE_CREATE_USE3);
            db.execSQL(DATABASE_CREATE_USE4); */

            db.execSQL("INSERT INTO `users` (`id`, `name`) VALUES " +
                    "(1, 'angel'), " +
                    "(2, 'fernando');");

            db.execSQL("INSERT INTO `recipes` (`id`, `name`, `total_time`, `person`, `user`) VALUES " +
                    " (1, 'Macarroncicos a la maña', 15, 2, 1), " +
                    "(2,'Pasta al pesto', 20, 2, 2), (3,'Hamburguesa vegana', 20, 2, 2);" );
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
     * Open the recipes database. If it cannot be opened, try to create a new
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
     * Return a Cursor positioned at the user that matches the given rowId
     *
     * @param rowId id of user to retrieve
     * @return Cursor positioned to matching user, if found
     * @throws SQLException if user could not be found/retrieved
     */
    public Cursor fetchUser(long rowId) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_USERS, new String[] {USERS_KEY_NAME}, USERS_KEY_ROWID + "=" + rowId, null, null, null, null, null);

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
     * Return a Cursor positioned at the recipe that matches the given rowId
     *
     * @param rowId id of recipe to retrieve
     * @return Cursor positioned to matching recipe, if found
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

    /**
     *  Return a Cursor over the list of ingredients of the selected recipe
     *
     * @param recipeId id of recipe you want to retrieve the ingredients from
     * @return Cursor over the list of ingredients
     */
    public Cursor fetchRecipeIngredients(long recipeId) throws SQLException {

        String q = "SELECT u.quantity, i.name FROM use_2 u, ingredients i WHERE u.recipe = ? AND u.ingredient = i.id";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId)});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     *  Return a Cursor over the list of utensils of the selected recipe
     *
     * @param recipeId id of recipe you want to retrieve the utensils from
     * @return Cursor over the list of ingredients
     */
    public Cursor fetchRecipeUtensils(long recipeId) throws SQLException {

        String q = "SELECT ut.name FROM use_1 us, utensils ut WHERE us.recipe = ? AND us.utensil = ut.id";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId)});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     *  Return a Cursor over the list of steps of the given recipe
     *
     * @param recipeId id of recipe you want to retrieve the steps from
     * @return Cursor over the list of steps
     */
    public Cursor fetchSteps(long recipeId) throws SQLException {

        String q = "SELECT id, step, time, intormation FROM steps s WHERE s.recipe = ? ORDER BY step";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId)});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    /**
     *  Return a Cursor over the list of ingredients of the selected recipe
     *
     * @param recipeId id of recipe you want to retrieve the ingredients from
     * @return Cursor over the list of ingredients
     */
    public Cursor fetchStepIngredients(long recipeId, long stepId) throws SQLException {

        String q = "SELECT u.quantity, i.name FROM use_3 u, ingredients i WHERE u.recipe = ? AND u.ingredient = i.id";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId)});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    /**
     *  Return a Cursor over the list of utensils of the selected recipe
     *
     * @param recipeId id of recipe you want to retrieve the utensils from
     * @return Cursor over the list of ingredients
     */
    public Cursor fetchStepUtensils(long recipeId, long stepId) throws SQLException {

        String q = "SELECT ut.name FROM use_4 us, utensils ut WHERE us.recipe = ? AND us.utensil = ut.id";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId)});

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     *  Inserts a recipe in the database
     *
     * @param name name of the recipe you want to insert
     * @param total_time time you spend on the recipe
     * @param person number of persons the recipe is indicated for
     * @param image recipe's image
     * @param user recipe's creator
     * @return rowId of the inserted recipe
     */
    public long insertRecipe(String name, long total_time, long person, byte[] image, long user) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(RECIPES_KEY_NAME, name);
        cv.put(RECIPES_KEY_TOTAL_TIME, total_time);
        cv.put(RECIPES_KEY_PERSON, person);
        cv.put(RECIPES_KEY_IMAGE, image);
        cv.put(RECIPES_KEY_USER, user);

        Long rowId = mDb.insert(DATABASE_TABLE_RECIPES, null, cv);

        return rowId;

    }


    /**
     *  Inserts an ingredient in the database and links it to a recipe.
     *  If the ingredient already existed, it only links it
     *
     * @param name name of the ingredient you want to insert
     * @param recipeRowId rowId of the recipe you want to link it to

     * @return rowId of the inserted recipe
     */
    public long insertIngredient(String name, long recipeRowId, long quantity) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(INGREDIENTS_KEY_NAME, name);

        Long rowId = mDb.insert(DATABASE_TABLE_INGREDIENTS, null, cv);

        if (rowId==-1){
            String q = "SELECT id FROM ingredients i WHERE i.name = ? ";

            Cursor mCursor =
                    mDb.rawQuery(q, new String[] {name});

            rowId = mCursor.getLong(mCursor.getColumnIndex(INGREDIENTS_KEY_ROWID));
        }

        cv.clear();
        cv.put(USE_KEY_RECIPE, recipeRowId);
        cv.put(USE2_KEY_INGREDIENT, rowId);
        cv.put(USE_KEY_QUANTITY, quantity);

        mDb.insert(DATABASE_TABLE_USE2, null, cv);

        return rowId;

    }

    /**
     *  Inserts a utensil in the database and liks it to a recipe.
     *  If the utensil already existed, it only links it
     *
     * @param name name of the utensil you want to insert
     * @param recipeRowId rowId of the recipe you want to link it to

     * @return rowId of the inserted recipe
     */
    public long insertUtensil(String name, long recipeRowId) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(UTENSILS_KEY_NAME, name);

        Long rowId = mDb.insert(DATABASE_TABLE_UTENSILS, null, cv);

        if (rowId==-1){
            String q = "SELECT u.id FROM utensils u WHERE u.name = ? ";

            Cursor mCursor =
                    mDb.rawQuery(q, new String[] {name});

            rowId = mCursor.getLong(mCursor.getColumnIndex(INGREDIENTS_KEY_ROWID));
        }

        cv.clear();
        cv.put(USE_KEY_RECIPE, recipeRowId);
        cv.put(USE1_KEY_UTENSIL, rowId);

        mDb.insert(DATABASE_TABLE_USE1, null, cv);

        return rowId;

    }

    /**
     *  Inserts a utensil in the database and liks it to a recipe.
     *  If the utensil already existed, it only links it
     *
     * @param time time of the step to insert
     * @param information information to give about the step
     * @param recipeRowId rowId of the recipe you want to link it to

     * @return rowId of the inserted recipe
     */
    public long insertStep(long time, String information, long recipeRowId) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(STEPS_KEY_TIME, time);
        cv.put(STEPS_KEY_INFORMATION, information);
        cv.put(RECIPES_KEY_ROWID, recipeRowId);

        Long rowId = mDb.insert(DATABASE_TABLE_STEPS, null, cv);

        return rowId;

    }



}