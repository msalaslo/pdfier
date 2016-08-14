CREATE DATABASE pdfa;

GRANT ALL ON pdfa.* TO pdfauser@'%' IDENTIFIED BY 'ppdfauser';
GRANT ALL ON pdfa.* TO pdfauser@localhost IDENTIFIED BY 'ppdfauser';

USE pdfa;

CREATE TABLE products (
  id INTEGER PRIMARY KEY,
  description varchar(255),
  price decimal(15,2)
);
CREATE INDEX products_description ON products(description);