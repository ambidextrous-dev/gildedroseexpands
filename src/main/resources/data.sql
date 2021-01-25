DROP TABLE IF EXISTS item;

CREATE TABLE item (
  name VARCHAR(300) PRIMARY KEY,
  description VARCHAR(600) NOT NULL,
  price DECIMAL NOT NULL
);

INSERT INTO item (name, description, price) VALUES
  ('Spiced Mead', 'Spiced Mead from the Northern Tribes', 25.50),
  ('Irish Ale', 'Ale from Ireland', 11),
  ('Bread', 'Fresh Baked Bread', 3.60);


DROP TABLE IF EXISTS inventory;

CREATE TABLE inventory (
  name VARCHAR(300) PRIMARY KEY,
  quantity INT NOT NULL
);

INSERT INTO inventory (name, quantity) VALUES
  ('Spiced Mead', 30),
  ('Irish Ale', 110),
  ('Bread', 5);