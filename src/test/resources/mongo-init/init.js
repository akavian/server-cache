const data = cat('/docker-entrypoint-initdb.d/data.json');
const docs = JSON.parse(data);
db.myCollection.insertMany(docs);