DROP TABLE IF EXISTS patient;
-- DROP TYPE IF EXISTS sex;
-- CREATE TYPE gender AS ENUM ('woman', 'man');
CREATE TABLE patient (
    id SERIAL,
    full_name text,
    gender text,
    birthday date,
    address text,
    policy_number text);