package es.eina.hopper.receticas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
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
    public static final String RECIPES_KEY_OWNER = "owner";
    public static final String RECIPES_KEY_CREATOR = "creator";

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
                    "name VARCHAR (32) not null unique," +
                    "total_time BIGINT (6) not null," +
                    "person BIGINT (6) not null," +
                    "image BLOB," +
                    "owner BIGINT (6)," +
                    "creator VARCHAR (32)," +
                    "FOREIGN KEY (creator) references users(id)" +
                    ");";


    private static final String DATABASE_CREATE_UTENSILS =
            "create table utensils (" +
                    "id INTEGER primary key autoincrement," +
                    "name VARCHAR (32) not null unique" +
                    ");";

    private static final String DATABASE_CREATE_INGREDIENTS =
            "create table ingredients (" +
                    "id INTEGER primary key autoincrement," +
                    "name VARCHAR (32) not null unique" +
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
            db.execSQL(DATABASE_CREATE_UTENSILS);
            db.execSQL(DATABASE_CREATE_INGREDIENTS);
            db.execSQL(DATABASE_CREATE_STEPS);
            db.execSQL(DATABASE_CREATE_USE1);
            db.execSQL(DATABASE_CREATE_USE2);
            db.execSQL(DATABASE_CREATE_USE3);
            db.execSQL(DATABASE_CREATE_USE4);

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS recipes");
            db.execSQL("DROP TABLE IF EXISTS utensils");
            db.execSQL("DROP TABLE IF EXISTS ingredients");
            db.execSQL("DROP TABLE IF EXISTS use_1");
            db.execSQL("DROP TABLE IF EXISTS use_2");
            db.execSQL("DROP TABLE IF EXISTS use_3");
            db.execSQL("DROP TABLE IF EXISTS use_4");
            db.execSQL("DROP TABLE IF EXISTS steps");
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
     * @param rowId id of the user to retrieve
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
     * Return a Cursor positioned at the user that matches the given rowId
     *
     * @param name name of the user to retrieve
     * @return Cursor positioned to matching user, if found
     * @throws SQLException if user could not be found/retrieved
     */
    public Cursor fetchUserId(String name) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_USERS, new String[] {USERS_KEY_NAME, USERS_KEY_ROWID}, USERS_KEY_NAME + "= '" + name + "'", null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the user that matches the given rowId
     *
     * @param name name of the user to retrieve
     * @return Cursor positioned to matching user, if found
     * @throws SQLException if user could not be found/retrieved
     */
    public long insertUser(String name) throws SQLException {

        ContentValues cv = new ContentValues();
        cv.put(USERS_KEY_NAME, name);

        Long rowId = mDb.insertWithOnConflict(DATABASE_TABLE_USERS, null, cv, mDb.CONFLICT_IGNORE);

        return rowId;
    }


    /**
     * Return a Cursor over the list of all recipes
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllRecipes (String user) {

        Cursor aux = fetchUserId(user);
        Long ownerId = aux.getLong(aux.getColumnIndex(USERS_KEY_ROWID));

        return mDb.query(DATABASE_TABLE_RECIPES, new String[] {RECIPES_KEY_ROWID, RECIPES_KEY_NAME,
                RECIPES_KEY_OWNER, RECIPES_KEY_CREATOR, RECIPES_KEY_PERSON, RECIPES_KEY_TOTAL_TIME, RECIPES_KEY_IMAGE}, RECIPES_KEY_OWNER + "=" + ownerId , null, null, null, null);
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
                                RECIPES_KEY_OWNER, RECIPES_KEY_CREATOR, RECIPES_KEY_PERSON, RECIPES_KEY_TOTAL_TIME, RECIPES_KEY_IMAGE}, RECIPES_KEY_ROWID + "=" + rowId, null,
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

        String q = "SELECT step, time, information FROM steps s WHERE s.recipe = ? ORDER BY step";

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

        String q = "SELECT u.quantity, i.name FROM use_3 u, ingredients i WHERE u.recipe = ? AND u.ingredient = i.id AND u.step = ?";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId), Long.toString(stepId)});

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

        String q = "SELECT ut.name FROM use_4 us, utensils ut WHERE us.recipe = ? AND us.utensil = ut.id AND us.step = ?";

        Cursor mCursor =
                mDb.rawQuery(q, new String[] {Long.toString(recipeId), Long.toString(stepId)});

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
     * @param creator recipe's creator
     * @param owner recipe's owner
     * @return rowId of the inserted recipe
     */
    public long insertRecipe(String name, long total_time, long person, String image, long owner, String creator) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(RECIPES_KEY_NAME, name);
        cv.put(RECIPES_KEY_TOTAL_TIME, total_time);
        cv.put(RECIPES_KEY_PERSON, person);
        cv.put(RECIPES_KEY_IMAGE, Base64.decode(image, Base64.DEFAULT));
        cv.put(RECIPES_KEY_CREATOR, creator);
        cv.put(RECIPES_KEY_OWNER, owner);


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
    public long insertIngredient(String name, long recipeRowId, String quantity) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_INGREDIENTS, new String[] {INGREDIENTS_KEY_ROWID}, INGREDIENTS_KEY_NAME + "= '" + name + "'", null, null, null, null, null);


        long rowId;

        if (!mCursor.moveToFirst()){

            ContentValues cv = new ContentValues();
            cv.put(INGREDIENTS_KEY_NAME, name);

            rowId = mDb.insert(DATABASE_TABLE_INGREDIENTS, null, cv);

        }
        else{
            rowId = mCursor.getLong(mCursor.getColumnIndex(INGREDIENTS_KEY_ROWID));
        }

        ContentValues cv = new ContentValues();
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

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_UTENSILS, new String[] {UTENSILS_KEY_ROWID}, UTENSILS_KEY_NAME + "= '" + name + "'", null, null, null, null, null);

        long rowId;



        if (!mCursor.moveToFirst()){
            ContentValues cv = new ContentValues();
            cv.put(UTENSILS_KEY_NAME, name);

            rowId = mDb.insert(DATABASE_TABLE_UTENSILS, null, cv);
        }
        else{
            rowId = mCursor.getLong(mCursor.getColumnIndex(INGREDIENTS_KEY_ROWID));
        }

        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(USE_KEY_RECIPE, recipeRowId);
        cv.put(USE1_KEY_UTENSIL, rowId);

        mDb.insert(DATABASE_TABLE_USE1, null, cv);

        return rowId;

    }

    /**
     *  Inserts a step in the database and liks it to a recipe.
     * @param time time of the step to insert
     * @param information information to give about the step
     * @param recipeRowId rowId of the recipe you want to link it to
     * @return rowId of the inserted recipe
     */
    public long insertStep(long step, long time, String information, long recipeRowId) throws SQLException {


        ContentValues cv = new ContentValues();
        cv.put(STEPS_KEY_STEP, step);
        cv.put(STEPS_KEY_TIME, time);
        cv.put(STEPS_KEY_INFORMATION, information);
        cv.put(USE_KEY_RECIPE, recipeRowId);

        Long rowId = mDb.insert(DATABASE_TABLE_STEPS, null, cv);

        return rowId;

    }


    /**
     *  Links an ingredient to a step
     *
     * @param name name of the ingredient you want to insert
     * @param recipeRowId rowId of the recipe you want to link it to
     * @param quantity ingredient's quantity
     * @param step step the ingredient owns to
     * @return rowId of the inserted ingredient
     */
    public long insertStepIngredient(String name, long recipeRowId, String quantity, long step) throws SQLException {


        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_INGREDIENTS, new String[] {INGREDIENTS_KEY_ROWID}, INGREDIENTS_KEY_NAME + "= '" + name + "'", null, null, null, null, null);
        mCursor.moveToFirst();

        long ingredientRowId = mCursor.getLong(mCursor.getColumnIndex(INGREDIENTS_KEY_ROWID));

        ContentValues cv = new ContentValues();
        cv.put(USE_KEY_RECIPE, recipeRowId);
        cv.put(USE2_KEY_INGREDIENT, ingredientRowId);
        cv.put(USE_KEY_QUANTITY, quantity);
        cv.put(STEPS_KEY_STEP, step);

        Long rowId = mDb.insert(DATABASE_TABLE_USE3, null, cv);

        return rowId;
    }


    /**
     *  Links a utensil to a step
     *
     * @param name name of the utensil you want to insert
     * @param recipeRowId rowId of the recipe you want to link it to
     * @param step step the ingredient owns to
     * @return rowId of the inserted recipe
     */
    public long insertStepUtensil(String name, long recipeRowId, long step) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_UTENSILS, new String[] {UTENSILS_KEY_ROWID}, UTENSILS_KEY_NAME + "= '" + name + "'", null, null, null, null, null);
        mCursor.moveToFirst();

        long utensilRowId = mCursor.getLong(mCursor.getColumnIndex(UTENSILS_KEY_ROWID));

        ContentValues cv = new ContentValues();
        cv.put(USE_KEY_RECIPE, recipeRowId);
        cv.put(USE1_KEY_UTENSIL, utensilRowId);
        cv.put(STEPS_KEY_STEP, step);

        Long rowId = mDb.insert(DATABASE_TABLE_USE4, null, cv);

        return rowId;
    }



    public void deleteDatabase() {
        mDbHelper.onUpgrade(mDb, 2,2);
    }

    public void deleteRecipe(long rowId){
        mDb.delete(DATABASE_TABLE_USE4, USE_KEY_RECIPE + "=" + rowId, null);
        mDb.delete(DATABASE_TABLE_USE3, USE_KEY_RECIPE + "=" + rowId, null);
        mDb.delete(DATABASE_TABLE_USE2, USE_KEY_RECIPE + "=" + rowId, null);
        mDb.delete(DATABASE_TABLE_USE1, USE_KEY_RECIPE + "=" + rowId, null);
        mDb.delete(DATABASE_TABLE_STEPS, USE_KEY_RECIPE + "=" + rowId, null);
        mDb.delete(DATABASE_TABLE_RECIPES, RECIPES_KEY_ROWID + "=" + rowId, null);

    }

}