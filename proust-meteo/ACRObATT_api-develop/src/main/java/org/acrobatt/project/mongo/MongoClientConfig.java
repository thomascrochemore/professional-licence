package org.acrobatt.project.mongo;

/**
 * A set of variables and "configurations" for MongoDB
 */
public final class MongoClientConfig {

    private static String HOST;
    private static int PORT;
    private static String DATABASE;
    private static String COLLECTION;
    private static boolean NEED_AUTH;
    private static String LOGIN;
    private static String PASSWORD;

    private MongoClientConfig() {}

    public static String getHost() { return HOST; }
    public static void setHost(String HOST) { MongoClientConfig.HOST = HOST; }

    public static int getPort() { return PORT; }
    public static void setPort(int PORT) { MongoClientConfig.PORT = PORT; }

    public static String getDatabase() { return DATABASE; }
    public static void setDatabase(String DATABASE) { MongoClientConfig.DATABASE = DATABASE; }

    public static String getCollection() { return COLLECTION; }
    public static void setCollection(String COLLECTION) { MongoClientConfig.COLLECTION = COLLECTION; }

    public static boolean isNeedAuth() { return NEED_AUTH; }
    public static void setNeedAuth(boolean needAuth) { NEED_AUTH = needAuth; }

    public static String getLogin() { return LOGIN; }
    public static void setLogin(String LOGIN) { MongoClientConfig.LOGIN = LOGIN; }

    public static String getPassword() { return PASSWORD; }
    public static void setPassword(String PASSWORD) { MongoClientConfig.PASSWORD = PASSWORD; }

    /*
    public static final String COLLECTION = "dumpcol";
    //public static final String COLLECTION = "dumpcol_testprod";

    //public final static String ENV = "prod";
    //public final static String ENV = "testprod";
    public final static String ENV = "local";

    public static final String LOCAL_ENV = "local";
    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int LOCAL_PORT = 27017;
    public static final String LOCAL_DATABASE = "proust-datadump";
    public static final boolean LOCAL_NEED_AUTH = false;
    public static final String LOCAL_LOGIN = null;
    public static final String LOCAL_PASSWORD = null;

    public static final String PROD_ENV = "prod";
    public static final String PROD_HOST = "sterne.iutrs.unistra.fr";
    public static final String PROD_DATABASE = "proust-datadump";
    public static final int PROD_PORT = 49905;
    public static final boolean PROD_NEED_AUTH = true;
    public static final String PROD_LOGIN = "prstdump";
    public static final String PROD_PASSWORD = "prstpwd";

    public static final String TESTPROD_ENV = "testprod";
    public static final String TESTPROD_HOST = "127.0.0.1";
    public static final String TESTPROD_DATABASE = "local";
    public static final int TESTPROD_PORT = 27017;
    public static final boolean TESTPROD_NEED_AUTH = false;
    public static final String TESTPROD_LOGIN = null;
    public static final String TESTPROD_PASSWORD = null;

    public static String getHost() throws IOException {
        if (ENV.equals(LOCAL_ENV))      return LOCAL_HOST;
        if (ENV.equals(PROD_ENV))       return PROD_HOST;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_HOST;
        throw new IOException("Env not valid");
    }
    public static String getDatabase() throws IOException {
        if (ENV.equals(LOCAL_ENV))      return LOCAL_DATABASE;
        if (ENV.equals(PROD_ENV))       return PROD_DATABASE;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_DATABASE;
        throw new IOException("Env not valid");
    }
    public static int getPort() throws IOException {
        if (ENV.equals(LOCAL_ENV))      return LOCAL_PORT;
        if (ENV.equals(PROD_ENV))       return PROD_PORT;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_PORT;
        throw new IOException("Env not valid");
    }
    public static boolean isNeedAuth() throws IOException {
        if (ENV.equals(LOCAL_ENV)) return LOCAL_NEED_AUTH;
        if (ENV.equals(PROD_ENV)) return PROD_NEED_AUTH;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_NEED_AUTH;
        throw new IOException("Env not valid");
    }
    public static String getLogin() throws IOException {
        if (ENV.equals(LOCAL_ENV)) return LOCAL_LOGIN;
        if (ENV.equals(PROD_ENV)) return PROD_LOGIN;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_LOGIN;
        throw new IOException("Env not valid");
    }
    public static String getPassword() throws IOException {
        if (ENV.equals(LOCAL_ENV)) return LOCAL_PASSWORD;
        if (ENV.equals(PROD_ENV)) return PROD_PASSWORD;
        if (ENV.equals(TESTPROD_ENV))   return TESTPROD_PASSWORD;
        throw new IOException("Env not valid");
    }
    */
}
