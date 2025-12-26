const dbName = process.env.MONGO_DATABASE || "server-cache-mongo";
const authDbname = process.env.MONGO_AUTH_DB || "admin";
const appUser = process.env.MONGO_USERNAME || "appUser";
const appPass = process.env.MONGO_PASSWORD || "appPass";

const db = new Mongo().getDB(authDbname);

db.createUser({
  user: appUser,
  pwd: appPass,
  roles: [{ role: "readWrite", db: dbName }],
});