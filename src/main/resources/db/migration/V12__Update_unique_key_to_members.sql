DROP INDEX UK_members_email ON members;

ALTER TABLE members ADD CONSTRAINT UK_members_email_login_type UNIQUE (email, login_type);
