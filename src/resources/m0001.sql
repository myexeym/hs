DROP TABLE IF EXISTS patient;
DROP TYPE IF EXISTS sex;
CREATE TYPE sex AS ENUM ('W', 'M');
CREATE TABLE patient (
    id SERIAL,
    fio text,
    sex sex,
    birthday date,
    address text,
    oms text);