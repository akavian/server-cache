const dbName = process.env.MONGODB_DATABASE || "server-cache-mongo";
const authDbname = process.env.MONGO_DB_AUTH || dbName;
const appUser = process.env.MONGODB_APP_USER || "appUser";
const appPass = process.env.MONGODB_APP_PASS || "appPass";

const db = new Mongo().getDB(authDbname);

db.createUser({
  user: appUser,
  pwd: appPass,
  roles: [{ role: "readWrite", db: dbName }],
});