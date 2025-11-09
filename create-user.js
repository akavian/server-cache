const dbName = process.env.MONGODB_DB || "server-cache-mongo";
const appUser = process.env.MONGODB_APP_USER || "appuser";
const appPass = process.env.MONGODB_APP_PASS || "appPass";

const db = new Mongo().getDB(dbName);

db.createUser({
  user: appUser,
  pwd: appPass,
  roles: [{ role: "readWrite", db: dbName }],
});